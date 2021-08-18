package com.cathay.exam.mvc.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.cathay.exam.mvc.R
import com.cathay.exam.mvc.base.ToolbarFragment
import com.cathay.exam.mvc.data.entity.ExamEntity
import com.cathay.exam.mvc.data.repo.ExamRepo
import com.cathay.exam.mvc.ui.adapter.ExamAdapter
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class HomeFragment : ToolbarFragment() {
    private var data = mutableListOf<ExamEntity>()
    private var examAdapter: ExamAdapter? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var submit: Button

    companion object {
        const val POSITION = "POSITION"
    }

    override fun initContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView = view.findViewById(R.id.recycleView)
        submit = view.findViewById(R.id.submit)
        return view
    }


    override fun initView() {
        super.initView()
        setToolbarTitle("Home")
        hideBackArrow()
        examAdapter = ExamAdapter(data)
        recyclerView.adapter = examAdapter

    }

    override fun listen() {
        super.listen()
        examAdapter?.setOnItemClickListener { i, view, any ->
            val bundle = Bundle()
            bundle.putInt(POSITION, i)
            nav().navigate(R.id.action_navigation_home_to_navigation_answer, bundle)
        }
        submit.setOnClickListener {
            nav().navigate(R.id.action_navigation_home_to_navigation_result)
        }

        setBackPress { requireActivity().finish() }
    }


    override fun onResume() {
        super.onResume()
       getExams()
    }

    // get exam data list and set to ui
    private fun getExams(){
        GlobalScope.launch(Dispatchers.Main) {
            ExamRepo(getRoomDao()).fetchExams()
                .catch {
                    Log.d("Ryan",Log.getStackTraceString(it))
                }.collect {
                    examAdapter?.setData(it)
                }
        }
    }


}
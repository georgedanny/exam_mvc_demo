package com.cathay.exam.mvc.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.cathay.exam.mvc.R
import com.cathay.exam.mvc.base.ToolbarFragment
import com.cathay.exam.mvc.data.entity.ExamEntity
import com.cathay.exam.mvc.data.repo.ExamRepo
import com.cathay.exam.mvc.ui.adapter.ExamAdapter
import com.cathay.exam.mvc.utils.DataUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class ResultFragment : ToolbarFragment() {
    private var examAdapter: ExamAdapter? = null
    private var data = mutableListOf<ExamEntity>()
    private lateinit var tvScore: TextView
    private lateinit var recycleView: RecyclerView
    override fun initContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_result, container, false)
        tvScore = view.findViewById(R.id.score)
        recycleView = view.findViewById(R.id.recycleView)
        return view
    }

    override fun initView() {
        super.initView()
        setToolbarTitle("Result")
        examAdapter = ExamAdapter(data)
        recycleView.adapter = examAdapter
        getExams()
    }

    override fun listen() {
        super.listen()
        examAdapter?.setOnItemClickListener { i, view, any ->
            val bundle = Bundle()
            bundle.putInt(HomeFragment.POSITION, i)
            nav().navigate(R.id.action_navigation_result_to_navigation_result_detail, bundle)
        }

        setNavigationHomeClickListen {
            showAlert()
        }

        setBackPress {
            showAlert()
        }
    }

    // get exam data list and set to ui
    private fun getExams(){
        GlobalScope.launch(Dispatchers.Main) {
            ExamRepo(getRoomDao()).fetchExams()
                .catch {
                    Log.d("Ryan", Log.getStackTraceString(it))
                }.collect {
                    examAdapter?.setData(it as MutableList<ExamEntity>, true)

                    var score = 0
                    it.map { exam ->
                        if (DataUtil.sortOutData(exam.userAns) == DataUtil.sortOutData(exam.ans)) {
                            score += 10
                        }
                    }
                    tvScore.text = getString(R.string.score, score)
                }
        }
    }

    private fun showAlert() {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.msg))
            .setTitle(getString(R.string.title))
            .setNegativeButton(
                R.string.cancel
            ) { dialog, which ->

            }
            .setPositiveButton(R.string.confirm) { dialog, which ->
                GlobalScope.launch(Dispatchers.Main) {
                    ExamRepo(getRoomDao()).resetDefaultTable()
                        .catch {
                            Log.e("Ryan", Log.getStackTraceString(it))
                        }.collect { nav().navigateUp() }
                }
            }
            .show()
    }
}
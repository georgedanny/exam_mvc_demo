package com.cathay.exam.mvc.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.cathay.exam.mvc.R
import com.cathay.exam.mvc.base.ToolbarFragment
import com.cathay.exam.mvc.data.entity.ExamEntity
import com.cathay.exam.mvc.data.repo.ExamRepo
import com.cathay.exam.mvc.utils.DataUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.json.JSONArray

class ResultDetailFragment : ToolbarFragment() {
    private val checkBoxs = mutableListOf<CheckBox>()
    private var position: Int = 0
    private var examEntity: ExamEntity? = null
    private lateinit var btNext: Button
    private lateinit var btPrevious: Button
    private lateinit var tvTopic: TextView
    private lateinit var tvAnswer: TextView
    private lateinit var topicContainer: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            position = getInt(HomeFragment.POSITION, 0)
        }
    }

    override fun initContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_result_detail, container, false)
        btNext = view.findViewById(R.id.next)
        btPrevious = view.findViewById(R.id.previous)
        tvTopic = view.findViewById(R.id.topic)
        tvAnswer = view.findViewById(R.id.answer)
        topicContainer = view.findViewById(R.id.topic_container)
        return view
    }

    override fun initView() {
        super.initView()
        setToolbarTitle("Result Detail")
        getExamData()
    }

    //get exam data from DB
    private fun getExamData(){
        GlobalScope.launch(Dispatchers.Main) {
            ExamRepo(getRoomDao()).fetchExams()
                .catch {
                    Log.d("Ryan", Log.getStackTraceString(it))
                }.collect {
                    examEntity = it[position]
                    examEntity?.let { entity->

                        val options = JSONArray(entity.options)
                        tvTopic.text = entity.topic
                        checkBoxs.clear()
                        topicContainer.removeAllViews()
                        (0 until options.length()).map {
                            val view = layoutInflater.inflate(R.layout.item_choise, null)
                            topicContainer.addView(view)
                            val checkbox = view.findViewById<CheckBox>(R.id.check)
                            val item = view.findViewById<TextView>(R.id.item)
                            checkBoxs.add(checkbox)
                            item.text = (it + 1).toString() + "."
                        }

                        entity.userAns?.apply {
                            val array = split(",")
                            checkBoxs.mapIndexed { index, checkBox ->
                                checkBox.isEnabled = false
                                checkBox.text = options.getString(index)
                            }

                            if (array.isNotEmpty()) {
                                array.mapIndexed { index, s ->
                                    Log.d("Ryan", "s = $s")
                                    if (s == "1" || s == "2" || s == "3" || s == "4") {
                                        checkBoxs[index].isChecked = true
                                    }
                                }
                            }
                        }
                        if (DataUtil.sortOutData(entity.ans) == DataUtil.sortOutData(entity.userAns)){
                            tvAnswer.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
                        }else{
                            tvAnswer.setTextColor(ContextCompat.getColor(requireContext(),R.color.red))
                        }
                        tvAnswer.text = getString(R.string.answer, entity.ans.toString())
                    }
                    updateButtonState(position,it.size)
                }
        }
    }

    override fun listen() {
        super.listen()

        btNext.setOnClickListener {
            position++
            getExamData()
        }

        btPrevious.setOnClickListener {
            position--
            getExamData()
        }
    }

    //update previous and next button state
    private fun updateButtonState(position: Int, size: Int) {
        if (position == 0) {
            btPrevious.visibility = View.GONE
        } else {
            btPrevious.visibility = View.VISIBLE
        }

        if (position == size - 1) {
            btNext.visibility = View.GONE
        } else {
            btNext.visibility = View.VISIBLE
        }
    }
}
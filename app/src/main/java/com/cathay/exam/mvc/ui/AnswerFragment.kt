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
import com.cathay.exam.mvc.R
import com.cathay.exam.mvc.base.ToolbarFragment
import com.cathay.exam.mvc.data.ClickType
import com.cathay.exam.mvc.data.entity.ExamEntity
import com.cathay.exam.mvc.data.repo.ExamRepo
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.json.JSONArray

class AnswerFragment : ToolbarFragment() {
    private var position: Int = 0
    private val checkBoxs = mutableListOf<CheckBox>()
    private var examEntity: ExamEntity? = null
    private lateinit var btNext: Button
    private lateinit var btPrevious: Button
    private lateinit var topic: TextView
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
        val view = inflater.inflate(R.layout.fragment_answer, container, false)
        btNext = view.findViewById(R.id.next)
        btPrevious = view.findViewById(R.id.previous)
        topic = view.findViewById(R.id.topic)
        topicContainer = view.findViewById(R.id.topic_container)
        return view
    }

    override fun initView() {
        super.initView()
        setToolbarTitle("Answer")
        getExamData()
    }


    override fun listen() {
        super.listen()

        btNext.setOnClickListener {
            saveUserAnswer(ClickType.PLUS)
        }

        btPrevious.setOnClickListener {
            saveUserAnswer(ClickType.MINUS)
        }

        setNavigationHomeClickListen {
            saveUserAnswer(ClickType.BACK)
        }

        setBackPress {
            saveUserAnswer(ClickType.BACK)
        }

    }

    //get exam data from DB
    private fun getExamData() {
        GlobalScope.launch(Dispatchers.Main) {
            ExamRepo(getRoomDao()).fetchExams()
                .catch {
                    Log.d("Ryan", Log.getStackTraceString(it))
                }.collect {
                    examEntity = it[position]
                    examEntity?.let { entity ->

                        val options = JSONArray(entity.options)
                        topic.text = entity.topic
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
                    }
                    updateButtonState(position, it.size)
                }
        }
    }

    //save user answer to DB
    private fun saveUserAnswer(clickType: ClickType) {
        GlobalScope.launch(Dispatchers.Main) {
            examEntity?.let {
                val ans = StringBuilder()
                val array = mutableListOf("1", "2", "3", "4")
                checkBoxs.mapIndexed { index, b ->
                    if (b.isChecked) {
                        ans.append(array[index])
                    }
                    if (index < array.size - 1) {
                        ans.append(",")
                    }
                }
                it.userAns = ans.toString()
                ExamRepo(getRoomDao()).saveAnswer(it)
                    .catch { Log.e("Ryan", Log.getStackTraceString(it)) }
                    .collect {
                        when (clickType) {
                            ClickType.BACK -> {
                                nav().navigateUp()
                            }
                            ClickType.MINUS -> {
                                --position
                                getExamData()
                            }
                            ClickType.PLUS -> {
                                ++position
                                getExamData()
                            }
                        }
                    }
            }

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
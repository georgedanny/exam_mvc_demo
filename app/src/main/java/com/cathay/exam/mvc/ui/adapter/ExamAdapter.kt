package com.cathay.exam.mvc.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cathay.exam.mvc.R
import com.cathay.exam.mvc.data.entity.ExamEntity
import com.cathay.exam.mvc.utils.DataUtil.sortOutData


class ExamAdapter(var mutableList: MutableList<ExamEntity>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var onItemClickListener: ((Int, View, Any) -> Unit)? = null
    private var showAns = false
    fun setOnItemClickListener(onItemClickListener: (Int, View, Any) -> Unit) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exam, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ViewHolder
        holder.bind(mutableList[position])
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(position, it, mutableList[position])
        }
    }

    fun setData(list: MutableList<ExamEntity>, isShowAns: Boolean = false) {
        this.mutableList = list
        this.showAns = isShowAns
        notifyDataSetChanged()
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val itemNum: TextView = view.findViewById(R.id.item_num)
        private val answer: TextView = view.findViewById(R.id.answer)
        private val userAnswer: TextView = view.findViewById(R.id.user_answer)
        private val topic: TextView = view.findViewById(R.id.topic)

        @SuppressLint("SetTextI18n")
        fun bind(bean: ExamEntity) {
            itemNum.text = "${(absoluteAdapterPosition + 1)}."
            if (sortOutData(bean.ans) == sortOutData(bean.userAns)) {
                answer.text = ""
                answer.setTextColor(ContextCompat.getColor(view.context, R.color.black))
            } else {
                if (showAns) answer.text = sortOutData(bean.ans)
                answer.setTextColor(ContextCompat.getColor(view.context, R.color.teal_200))
            }
            userAnswer.text = sortOutData(bean.userAns)
            topic.text = bean.topic
        }
    }

    override fun getItemCount(): Int {
        return mutableList.size
    }
}
package com.cathay.exam.mvc.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExamEntity(
    var id: Int = 0,
    var topic: String? = null,
    var userAns: String? = null,
    var ans: String? = null,
    var options: String? = null,

): Parcelable {

}


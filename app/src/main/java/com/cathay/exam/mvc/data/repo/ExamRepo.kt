package com.cathay.exam.mvc.data.repo

import com.cathay.exam.mvc.data.database.ExamRoomDao
import com.cathay.exam.mvc.data.database.RoomDao
import com.cathay.exam.mvc.data.entity.ExamEntity
import com.cathay.exam.mvc.utils.DataUtil
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transform

class ExamRepo(private val roomDao: RoomDao) {

    //get DB exam data
    fun fetchExams(): Flow<MutableList<ExamEntity>> {
        return flow<MutableList<ExamEntity>> {
            val exams = roomDao.getAll()
            if (exams != null) {
                if (exams.isNotEmpty()) {
                    val data = trans(exams)
                    emit(data)
                } else {
                    val data = DataUtil.createData()
                    roomDao.insertAll(data)
                    val initData = trans(data)
                    emit(initData)
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    //rest DB data to default
    fun resetDefaultTable(): Flow<Unit> {
        return flow<Unit> {
            roomDao.clearTable()
            emit(Unit)
        }.flowOn(Dispatchers.IO)
    }

    //Save Ans To DB
    fun saveAnswer(entity: ExamEntity): Flow<Unit> {
        return flow<Unit> {
            Gson().apply {
                val dao = fromJson(toJson(entity), ExamRoomDao::class.java)
                emit(roomDao.update(dao))
            }


        }.flowOn(Dispatchers.IO)
    }

    //Transform List<ExamRoomDao> to MutableList<ExamEntity>
    private fun trans(exams: List<ExamRoomDao>): MutableList<ExamEntity> {
        return exams.map {
            ExamEntity().apply {
                id = it.id
                topic = it.topic
                ans = it.ans
                userAns = it.userAns
                options = it.options
            }
        }.toMutableList()

    }


}





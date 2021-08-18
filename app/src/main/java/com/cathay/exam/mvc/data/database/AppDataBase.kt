package com.cathay.exam.mvc.data.database

import android.content.Context
import androidx.room.*


@Database(entities = [(ExamRoomDao::class)], version = 1)
abstract class AppDataBase :RoomDatabase(){

companion object{
    const val TABLE_NAME = "exam_table"


    @Volatile private var instance: AppDataBase? = null
    private val LOCK = Any()

    operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
        instance ?: buildDatabase(context.applicationContext).also { instance = it}
    }

    private fun buildDatabase(context: Context) = Room.databaseBuilder(context.applicationContext,
        AppDataBase::class.java, "exam.db").build()
}
    abstract fun examInfoDao(): RoomDao
}
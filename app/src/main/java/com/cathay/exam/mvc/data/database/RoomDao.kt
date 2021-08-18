package com.cathay.exam.mvc.data.database

import androidx.room.*
import com.cathay.exam.mvc.data.database.ExamRoomDao

@Dao
interface RoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: ExamRoomDao?): Long

    @Insert
    fun insertAll(item: List<ExamRoomDao>?)


    @Query("SELECT * FROM exam_table WHERE id= :itemId")
    fun findById(itemId: Int): ExamRoomDao?

    @Query("SELECT * FROM exam_table")
    fun getAll(): List<ExamRoomDao>?

    @Delete
    fun delete(item: ExamRoomDao?)

    @Update
    fun update(item: ExamRoomDao?)

    @Update
    fun updateAll(items: List<ExamRoomDao>?)

    @Query("DELETE FROM exam_table")
    fun clearTable()
}
package com.hust.edu.vn.studentmanagement

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface StudentDao {
    @Query("select * from students")
    suspend fun getAllStudents(): MutableList<Student>

    @Query("select * from students where _id = :id")
    suspend fun findStudentById(id: Int): Student

    @Insert
    suspend fun insert(student: Student): Long

    @Update
    suspend fun update(student: Student): Int

//    @Query("update colors set name = :name where _id = :id")
//    suspend fun updateNameById(id: Int, name: String): Int

    @Delete
    suspend fun delete(student: Student): Int

    @Query("delete from students where _id = :id")
    suspend fun deleteById(id: Int): Int
}
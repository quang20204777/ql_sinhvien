package com.hust.edu.vn.studentmanagement

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = true)
    var _id: Int = 0,
    val code: String,
    val name: String,
    @ColumnInfo(name = "birth_day")
    val birthDay: String,
    @ColumnInfo(name = "home_town")
    val homeTown: String
)

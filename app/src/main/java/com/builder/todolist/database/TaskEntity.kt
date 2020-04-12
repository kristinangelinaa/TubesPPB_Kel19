package com.builder.todolist.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Int?,

    @ColumnInfo(name = "title")
    var title: String = "",

    @ColumnInfo(name = "date")
    var date: String = "",

    @ColumnInfo(name = "time")
    var time: String = "",

    @ColumnInfo(name = "note")
    var note: String? = "",

    @ColumnInfo(name = "isFinished")
    var isFinished: Boolean = false
)
package com.builder.todolist.database

import androidx.room.*

@Dao
interface TaskDao {

    @Insert
    fun insertTask(task: TaskEntity)

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE date IN(:date)")
    fun getTodayTasks(date : String) : TaskEntity

    @Delete
    fun deleteTask(task: TaskEntity)

}
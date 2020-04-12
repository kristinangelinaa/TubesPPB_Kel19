package com.builder.todolist.database

import androidx.room.*
import com.builder.todolist.model.TaskEntity

@Dao
interface TaskDao {

    @Insert
    fun insertTask(task: TaskEntity)

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE date IN(:date)")
    fun getTodayTasks(date : String) : List<TaskEntity>

    @Delete
    fun deleteTask(task: TaskEntity)

    @Update
    fun updateTask(task: TaskEntity)

}
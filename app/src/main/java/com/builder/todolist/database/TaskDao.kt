package com.builder.todolist.database

import androidx.room.*
import com.builder.todolist.model.TaskEntity

@Dao
interface TaskDao {

    @Insert
    fun insertTask(task: TaskEntity)

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE date BETWEEN :start and :endOfDay ORDER BY  isFinished ASC, time ASC")
    fun getTodayTasks(start : String, endOfDay : String) : List<TaskEntity>

    @Delete
    fun deleteTask(task: TaskEntity)

    @Update
    fun updateTask(task: TaskEntity)

}
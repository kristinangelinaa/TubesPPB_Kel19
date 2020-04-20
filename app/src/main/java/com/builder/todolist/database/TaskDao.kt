package com.builder.todolist.database

import androidx.room.*
import com.builder.todolist.model.TaskEntity

@Dao
interface TaskDao {

    @Insert
    fun insertTask(task: TaskEntity)

    @Query("SELECT * FROM tasks ORDER BY isFinished ASC, date ASC, time ASC")
    fun getAllTasks(): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE date BETWEEN :start and :endOfDay ORDER BY  isFinished ASC, time ASC")
    fun getTodayTasks(start : String, endOfDay : String) : List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE isFinished = 1")
    fun getFinishedTasks() : List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE isFinished = 0")
    fun getAllUnfinishedTask() : List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE date < :today ORDER BY date ASC, time ASC")
    fun getAllMissedTask(today: String) : List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE date > :today ORDER BY date ASC, time ASC")
    fun getAllUpcomingTask(today: String) : List<TaskEntity>
    @Delete
    fun deleteTask(task: TaskEntity)

    @Delete
    fun deleteTasks(tasks : List<TaskEntity>)

    @Update
    fun updateTask(task: TaskEntity)

}
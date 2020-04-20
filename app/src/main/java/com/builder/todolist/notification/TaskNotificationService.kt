package com.builder.todolist.notification

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.builder.todolist.database.TaskDatabase
import com.builder.todolist.model.TaskEntity
import java.util.*

class TaskNotificationService {
    fun getTodayTasks(): List<TaskEntity> {
        val database = Room.databaseBuilder(
            Application().applicationContext,
            TaskDatabase::class.java,
            "TaskDB"
        ).build()

        val calendar = Calendar.getInstance()

        val todayStart = Calendar.getInstance()
        todayStart.set(Calendar.DATE, calendar.get(Calendar.DATE))
        todayStart.set(Calendar.MONTH, calendar.get(Calendar.MONTH))
        todayStart.set(Calendar.YEAR, calendar.get(Calendar.YEAR))
        todayStart.set(Calendar.HOUR_OF_DAY, 0)
        todayStart.set(Calendar.MINUTE, 0)

        val todayEnd = Calendar.getInstance()
        todayEnd.set(Calendar.DATE, calendar.get(Calendar.DATE))
        todayEnd.set(Calendar.MONTH, calendar.get(Calendar.MONTH))
        todayEnd.set(Calendar.YEAR, calendar.get(Calendar.YEAR))
        todayEnd.set(Calendar.HOUR_OF_DAY, 23)
        todayEnd.set(Calendar.MINUTE, 0)

        Log.d("TodayDateStartValue = ", todayStart.timeInMillis.toString())
        Log.d("TodayDateEndValue = ", todayEnd.timeInMillis.toString())

        return database.taskDao()
            .getTodayTasks(todayStart.timeInMillis.toString(), todayEnd.timeInMillis.toString())
    }
}
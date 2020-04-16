package com.builder.todolist.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.builder.todolist.model.TaskEntity

@Database(entities = [(TaskEntity::class)], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao() : TaskDao

    private lateinit var instance : TaskDatabase

    @Synchronized
    open fun getInstanceDatabase(context: Context?): TaskDatabase? {
        instance = Room.databaseBuilder(context!!.applicationContext, TaskDatabase::class.java, "TaskDB").build()
        return instance
    }
}
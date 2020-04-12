package com.builder.todolist

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Movie
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.builder.todolist.activity.NewTaskActivity
import com.builder.todolist.adapter.TaskAdapter
import com.builder.todolist.database.TaskDatabase
import com.builder.todolist.database.TaskEntity
import com.builder.todolist.model.Task
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var listTask : List<TaskEntity>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTitleTime()

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        main_recycler_view.layoutManager = layoutManager

        SetTaskData().execute()

        btn_new_task.setOnClickListener {
            val intent = Intent(this, NewTaskActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setTitleTime(){
        val calendar = Calendar.getInstance()

        val dataTime = DateFormat.format("EEEE, dd MMM yyy", calendar)
        today_date_tv.text = dataTime
    }

    @SuppressLint("StaticFieldLeak")
    internal inner class SetTaskData : AsyncTask<Void, Void, Unit>() {
        override fun doInBackground(vararg p0: Void?) {
            return getAllTasks()
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)

            val adapter = TaskAdapter(this@MainActivity, listTask)
            main_recycler_view.adapter = adapter

            if (listTask.isEmpty()) {
                main_recycler_view.visibility = View.GONE
                main_no_task.visibility = View.VISIBLE
            } else {
                main_recycler_view.visibility = View.VISIBLE
                main_no_task.visibility = View.GONE
            }
        }

        override fun onPreExecute() {
            super.onPreExecute()

            main_no_task.visibility = View.VISIBLE
            main_recycler_view.visibility = View.GONE
        }
    }

    private fun getAllTasks() {
        val database = Room.databaseBuilder(
            applicationContext,
            TaskDatabase::class.java,
            "TaskDB"
        ).build()

        listTask = database.taskDao().getAllTasks()
    }
}

package com.builder.todolist

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.builder.todolist.activity.NewTaskActivity
import com.builder.todolist.adapter.TaskAdapter
import com.builder.todolist.database.TaskDatabase
import com.builder.todolist.model.TaskEntity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.task_card.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var listTask : List<TaskEntity>
    private val calendar = Calendar.getInstance()
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

        listTask = database.taskDao().getTodayTasks(todayStart.timeInMillis.toString(), todayEnd.timeInMillis.toString())
    }

    companion object {
        var EXTRA_DATA = ""
    }
}

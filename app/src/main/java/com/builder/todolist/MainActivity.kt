package com.builder.todolist

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.builder.todolist.activity.AllTaskActivity
import com.builder.todolist.activity.CompletedTaskActivity
import com.builder.todolist.activity.NewTaskActivity
import com.builder.todolist.activity.SettingActivity
import com.builder.todolist.adapter.TaskAdapter
import com.builder.todolist.database.TaskDatabase
import com.builder.todolist.model.TaskEntity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var listTask: List<TaskEntity>
    private lateinit var listTaskToday: List<TaskEntity>
    private lateinit var allListTask: List<TaskEntity>
    private lateinit var allCompletedTask: List<TaskEntity>

    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTitleTime()

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        main_recycler_view.layoutManager = layoutManager

        SetTaskData().execute()
        GetAllTaskSize().execute()
        GetCompletedTask().execute()

        btn_new_task.setOnClickListener {
            val intent = Intent(this, NewTaskActivity::class.java)
            startActivity(intent)
        }

        main_all_task_card.setOnClickListener {
            val intent = Intent(this, AllTaskActivity::class.java)
            startActivity(intent)
        }

        main_complete_task_card.setOnClickListener {
            val intent = Intent(this, CompletedTaskActivity::class.java)
            startActivity(intent)
        }

        main_btn_setting.setOnClickListener {
            Intent(this, SettingActivity::class.java).apply {
                startActivity(this)
            }
        }

        main_today_task_card.setOnClickListener {
            Toast.makeText(this, "This is your Today Task", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        SetTaskData().execute()
        GetAllTaskSize().execute()
        GetCompletedTask().execute()
    }

    private fun setTitleTime() {
        val dataTime = DateFormat.format("EEEE, dd MMM yyy", calendar)
        today_date_tv.text = dataTime
    }

    @SuppressLint("StaticFieldLeak")
    internal inner class SetTaskData : AsyncTask<Void, Void, Unit>() {
        override fun doInBackground(vararg p0: Void?) {
            return getTodayTasks()
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)

            val adapter = TaskAdapter(this@MainActivity, listTask)
            main_recycler_view.adapter = adapter

            if (listTask.isEmpty()) {
                main_recycler_view.visibility = View.GONE
                main_no_task.visibility = View.VISIBLE
                main_count_task_today.text = listTaskToday.size.toString()
            } else {
                main_recycler_view.visibility = View.VISIBLE
                main_no_task.visibility = View.GONE
                main_count_task_today.text = listTaskToday.size.toString()
            }
        }

        override fun onPreExecute() {
            super.onPreExecute()
            main_no_task.visibility = View.VISIBLE
            main_recycler_view.visibility = View.GONE
        }
    }

    @SuppressLint("StaticFieldLeak")
    internal inner class GetAllTaskSize : AsyncTask<Void, Void, Unit>() {
        override fun doInBackground(vararg p0: Void?) {
            return getAllTask()
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
            main_count_all_task.text = allListTask.size.toString()
        }

        override fun onPreExecute() {
            super.onPreExecute()
            main_count_all_task.text = "0"
        }
    }

    @SuppressLint("StaticFieldLeak")
    internal inner class GetCompletedTask : AsyncTask<Void, Void, Unit>() {
        override fun doInBackground(vararg p0: Void?) {
            return getCompletedTask()
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
            main_count_complete_task.text = allCompletedTask.size.toString()
        }

        override fun onPreExecute() {
            super.onPreExecute()
            main_count_complete_task.text = "0"
        }
    }

    private fun getCompletedTask() {
        val database = Room.databaseBuilder(
            applicationContext,
            TaskDatabase::class.java,
            "TaskDB"
        ).build()

        allCompletedTask = database.taskDao().getFinishedTasks()
    }

    private fun getTodayTasks() {
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
        todayEnd.set(Calendar.MINUTE, 59)

        Log.d("TodayDateStartValue = ", todayStart.timeInMillis.toString())
        Log.d("TodayDateEndValue = ", todayEnd.timeInMillis.toString())

        listTask = database.taskDao()
            .getTodayTasks(todayStart.timeInMillis.toString(), todayEnd.timeInMillis.toString())

        listTaskToday = database.taskDao().getTodayCompletedTasks(
            todayStart.timeInMillis.toString(),
            todayEnd.timeInMillis.toString()
        )
    }

    private fun getAllTask() {
        val database = Room.databaseBuilder(
            applicationContext,
            TaskDatabase::class.java,
            "TaskDB"
        ).build()

        allListTask = database.taskDao().getAllUnfinishedTask()
    }

    companion object {
        var EXTRA_DATA = ""
    }
}

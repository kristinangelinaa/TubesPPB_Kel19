package com.builder.todolist.activity

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.builder.todolist.R
import com.builder.todolist.adapter.TaskAdapter
import com.builder.todolist.database.TaskDatabase
import com.builder.todolist.model.TaskEntity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_all_task_activitty.*
import java.util.*

class AllTaskActivity : AppCompatActivity() {
    private lateinit var allListTaskPassed: List<TaskEntity>
    private lateinit var allListTaskToday: List<TaskEntity>
    private lateinit var allListTaskUpcoming: List<TaskEntity>
    private lateinit var allListTask: List<TaskEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_task_activitty)

        val layoutManagerToday = LinearLayoutManager(this)
        layoutManagerToday.orientation = LinearLayoutManager.VERTICAL
        all_task_recycler_view_today.layoutManager = layoutManagerToday

        val layoutManagerPassed = LinearLayoutManager(this)
        layoutManagerPassed.orientation = LinearLayoutManager.VERTICAL
        all_task_recycler_view_passed.layoutManager = layoutManagerPassed

        val layoutManagerUpcoming = LinearLayoutManager(this)
        layoutManagerUpcoming.orientation = LinearLayoutManager.VERTICAL
        all_task_recycler_view_upcoming.layoutManager = layoutManagerUpcoming

        SetTaskData().execute()

        btn_back_all_task.setOnClickListener {
            finish()
        }

        all_img_delete.setOnClickListener {
            val alertDialog= MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.delete_all_task))
                .setMessage(getString(R.string.are_you_sure_to_delete_all_completed_task))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    DeleteAllData().execute()
                    SetTaskData().execute()
                }
                .setNegativeButton(getString(R.string.no), null)
                .show()

            val title = alertDialog.window!!.findViewById<TextView>(R.id.alertTitle)
            val message = alertDialog.window!!.findViewById<TextView>(android.R.id.message)
            val button1 = alertDialog.window!!.findViewById<Button>(android.R.id.button1)
            val button2 = alertDialog.window!!.findViewById<Button>(android.R.id.button2)

            val typeface1 = ResourcesCompat.getFont(this, R.font.p_regular)
            val typeface2 = ResourcesCompat.getFont(this, R.font.p_semibold)

            title.typeface = typeface2
            message.typeface = typeface1
            button1.typeface = typeface2
            button2.typeface = typeface2
        }
    }

    @SuppressLint("StaticFieldLeak")
    internal inner class SetTaskData : AsyncTask<Void, Void, Unit>() {
        override fun doInBackground(vararg p0: Void?) {
            return getAllTask()
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)

            // Adapter
            val todayTaskAdapter = TaskAdapter(this@AllTaskActivity, allListTaskToday)
            all_task_recycler_view_today.adapter = todayTaskAdapter

            val passedTaskAdapter = TaskAdapter(this@AllTaskActivity, allListTaskPassed)
            all_task_recycler_view_passed.adapter = passedTaskAdapter

            val upcomingTaskAdapter = TaskAdapter(this@AllTaskActivity, allListTaskUpcoming)
            all_task_recycler_view_upcoming.adapter = upcomingTaskAdapter

            // List Task Today
            if (allListTaskToday.isEmpty()) {
                all_task_recycler_view_today.visibility = View.GONE
                all_no_task_today.visibility = View.VISIBLE
            } else {
                all_task_recycler_view_today.visibility = View.VISIBLE
                all_no_task_today.visibility = View.GONE
            }


            // List Task Passed
            if (allListTaskPassed.isEmpty()) {
                all_task_recycler_view_passed.visibility = View.GONE
                all_no_task_past.visibility = View.VISIBLE
            } else {
                all_task_recycler_view_passed.visibility = View.VISIBLE
                all_no_task_past.visibility = View.GONE
            }


            // List Task Upcoming
            if (allListTaskUpcoming.isEmpty()) {
                all_task_recycler_view_upcoming.visibility = View.GONE
                all_no_task_upcoming.visibility = View.VISIBLE
            } else {
                all_task_recycler_view_upcoming.visibility = View.VISIBLE
                all_no_task_upcoming.visibility = View.GONE
            }

            // Show or Gone Delete Button
            if (allListTask.isEmpty()) {
                all_img_delete.visibility = View.GONE
            } else {
                all_img_delete.visibility = View.VISIBLE
            }
        }

        override fun onPreExecute() {
            super.onPreExecute()
            all_no_task_past.visibility = View.VISIBLE
            all_no_task_today.visibility = View.VISIBLE
            all_no_task_upcoming.visibility = View.VISIBLE
            all_task_recycler_view_today.visibility = View.GONE
            all_task_recycler_view_passed.visibility = View.GONE
            all_task_recycler_view_upcoming.visibility = View.GONE
            all_img_delete.visibility = View.GONE
        }
    }

    @SuppressLint("StaticFieldLeak")
    internal inner class DeleteAllData : AsyncTask<Void, Void, Unit>() {
        override fun doInBackground(vararg p0: Void?) {
            return deleteAllTask()
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)

            // Adapter
            val todayTaskAdapter = TaskAdapter(this@AllTaskActivity, allListTaskToday)
            all_task_recycler_view_today.adapter = todayTaskAdapter

            val passedTaskAdapter = TaskAdapter(this@AllTaskActivity, allListTaskPassed)
            all_task_recycler_view_passed.adapter = passedTaskAdapter

            val upcomingTaskAdapter = TaskAdapter(this@AllTaskActivity, allListTaskUpcoming)
            all_task_recycler_view_upcoming.adapter = upcomingTaskAdapter

            // List Task Today
            if (allListTaskToday.isEmpty()) {
                all_task_recycler_view_today.visibility = View.INVISIBLE
                all_no_task_today.visibility = View.VISIBLE
            } else {
                all_task_recycler_view_today.visibility = View.VISIBLE
                all_no_task_today.visibility = View.GONE
            }


            // List Task Passed
            if (allListTaskPassed.isEmpty()) {
                all_task_recycler_view_passed.visibility = View.INVISIBLE
                all_no_task_past.visibility = View.VISIBLE
            } else {
                all_task_recycler_view_passed.visibility = View.VISIBLE
                all_no_task_past.visibility = View.GONE
            }


            // List Task Upcoming
            if (allListTaskUpcoming.isEmpty()) {
                all_task_recycler_view_upcoming.visibility = View.INVISIBLE
                all_no_task_upcoming.visibility = View.VISIBLE
            } else {
                all_task_recycler_view_upcoming.visibility = View.VISIBLE
                all_no_task_upcoming.visibility = View.GONE
            }

            // Show or Gone Delete Button
            if (allListTask.isEmpty()) {
                all_img_delete.visibility = View.GONE
            } else {
                all_img_delete.visibility = View.GONE
            }
        }

        override fun onPreExecute() {
            super.onPreExecute()
            all_no_task_past.visibility = View.VISIBLE
            all_no_task_today.visibility = View.VISIBLE
            all_no_task_upcoming.visibility = View.VISIBLE
            all_task_recycler_view_today.visibility = View.INVISIBLE
            all_task_recycler_view_passed.visibility = View.INVISIBLE
            all_task_recycler_view_upcoming.visibility = View.INVISIBLE
            all_img_delete.visibility = View.GONE
        }
    }

    private fun getAllTask() {
        val database = Room.databaseBuilder(
            applicationContext,
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
        todayEnd.set(Calendar.MINUTE, 59)

        allListTask = database.taskDao().getAllTasks()
        allListTaskPassed = database.taskDao().getAllMissedTask(todayStart.timeInMillis.toString())
        allListTaskToday = database.taskDao()
            .getTodayTasks(todayStart.timeInMillis.toString(), todayEnd.timeInMillis.toString())
        allListTaskUpcoming =
            database.taskDao().getAllUpcomingTask(todayEnd.timeInMillis.toString())
    }

    private fun deleteAllTask() {
        val database = Room.databaseBuilder(
            applicationContext,
            TaskDatabase::class.java,
            "TaskDB"
        ).build()

        database.taskDao().deleteTasks(allListTask)
    }
}

package com.builder.todolist.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.builder.todolist.MainActivity
import com.builder.todolist.MainActivity.Companion.EXTRA_DATA
import com.builder.todolist.R
import com.builder.todolist.database.TaskDatabase
import com.builder.todolist.model.TaskEntity
import kotlinx.android.synthetic.main.activity_edit_task.*
import kotlinx.android.synthetic.main.activity_new_task.*
import java.util.*

class EditTaskActivity : AppCompatActivity() {
    private lateinit var dateSelected: Calendar
    private lateinit var timeSelected: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)

        taskPackage()

        btn_back_edit.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btn_save_task.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            Thread(Runnable {
                updateTask()
            }).start()
        }

        linearLayout_select_date_edit.setOnClickListener {
            handleCalendarButton()
        }

        linearLayout_select_time_edit.setOnClickListener {
            handleTimeButton()
        }
    }

    private fun taskPackage() {
        val taskData = intent.getParcelableExtra<TaskEntity>(EXTRA_DATA)

        if (taskData != null) {
            edit_title_et.setText(taskData.title)
        }
        if (taskData != null) {
            edit_date_tv.text = taskData.date
        }
        if (taskData != null) {
            edit_time_tv.text = taskData.time
        }
        if (taskData != null) {
            edit_note_et.setText(taskData.note)
        }

    }

    private fun updateTask(){
        val database = Room.databaseBuilder(
            applicationContext,
            TaskDatabase::class.java,
            "TaskDB"
        ).build()

        database.taskDao().updateTask(getTaskEntity())
    }

    private fun getTaskEntity() : TaskEntity {
        val taskData = intent.getParcelableExtra<TaskEntity>(EXTRA_DATA)

        return TaskEntity(
            taskData?.id,
            edit_title_et.text.toString(),
            edit_date_tv.text.toString(),
            edit_time_tv.text.toString(),
            edit_note_et.text.toString(),
            isFinished = false
        )
    }

    private fun handleCalendarButton(){
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val date = calendar.get(Calendar.DATE)

        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener {
                _, y, m, d ->

            val calendar1  = Calendar.getInstance()
            calendar1.set(Calendar.YEAR, y)
            calendar1.set(Calendar.MONTH, m)
            calendar1.set(Calendar.DATE, d)

            val dataString = DateFormat.format("EEEE, dd MMM yyyy", calendar1)
            create_date_tv.text = dataString

            dateSelected = calendar1

        }, year, month, date)

        datePickerDialog.show()
    }

    private fun handleTimeButton(){
        val calendar = Calendar.getInstance()

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener {
                _, h, m ->

            val calendar1 = Calendar.getInstance()
            calendar1.set(Calendar.HOUR_OF_DAY, h)
            calendar1.set(Calendar.MINUTE, m)

            val dataString = DateFormat.format("HH:mm", calendar1)
            create_time_tv.text = dataString

            timeSelected = calendar1

        }, hour, minute, true)

        timePickerDialog.show()
    }
}

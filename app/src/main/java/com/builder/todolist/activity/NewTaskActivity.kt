package com.builder.todolist.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.builder.todolist.MainActivity
import com.builder.todolist.R
import com.builder.todolist.database.TaskDatabase
import com.builder.todolist.model.TaskEntity
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.activity_edit_task.*
import kotlinx.android.synthetic.main.activity_new_task.*
import java.util.*

class NewTaskActivity : AppCompatActivity() {
    private lateinit var dateSelected: Calendar
    private lateinit var timeSelected: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_task)

        btn_back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btn_create_task.setOnClickListener {
            if (isDataFill()) {
                sendingDataToDatabase()
                Toast.makeText(this, "Task Saved", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please Fill the Data", Toast.LENGTH_SHORT).show()
                textFieldState()
            }
        }

        task_title_et.editText!!.setOnClickListener {
            task_title_et.error = null
        }

        new_date_picker_et.editText!!.setOnClickListener {
            handleCalendarButton()
            new_date_picker_et.error = null
        }

        new_time_picker_et.editText!!.setOnClickListener {
            handleTimeButton()
            new_time_picker_et.error = null
        }
    }

    private fun textFieldState() {
        if (task_title_et.editText!!.text.isEmpty()) {
            task_title_et.error = getString(R.string.no_title_error_msg)
        } else {
            task_title_et.error = null
        }

        if (new_date_picker_et.editText!!.text.isEmpty()) {
            new_date_picker_et.error = getString(R.string.no_date_error_msg)
        } else {
            new_date_picker_et.error = null
        }

        if (new_time_picker_et.editText!!.text.isEmpty()) {
            new_time_picker_et.error = getString(R.string.no_time_error_msg)
        } else {
            new_time_picker_et.error = null
        }
    }

    private fun handleCalendarButton() {
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val date = calendar.get(Calendar.DATE)

        val datePickerDialog =
            DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, y, m, d ->

                val calendar1 = Calendar.getInstance()
                calendar1.set(Calendar.YEAR, y)
                calendar1.set(Calendar.MONTH, m)
                calendar1.set(Calendar.DATE, d)

                val dataString =
                    android.text.format.DateFormat.format("EEEE, dd MMM yyyy", calendar1)
                new_date_picker_et.editText!!.setText(dataString)

                dateSelected = calendar1

            }, year, month, date)

        datePickerDialog.show()
    }

    private fun handleTimeButton() {
        val calendar = Calendar.getInstance()

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog =
            TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, h, m ->

                val calendar1 = Calendar.getInstance()
                calendar1.set(Calendar.HOUR_OF_DAY, h)
                calendar1.set(Calendar.MINUTE, m)

                val dataString = android.text.format.DateFormat.format("HH:mm", calendar1)
                new_time_picker_et.editText!!.setText(dataString)

                timeSelected = calendar1

            }, hour, minute, true)

        timePickerDialog.show()
    }

    private fun isDataFill(): Boolean {
        return !(task_title_et.editText!!.text.isEmpty() || new_date_picker_et.editText!!.text.isEmpty() || new_time_picker_et.editText!!.text.isEmpty())
    }

    private fun getTaskEntity(): TaskEntity {
        return TaskEntity(
            null,
            task_title_et.editText!!.text.toString(),
            dateSelected.timeInMillis.toString(),
            timeSelected.timeInMillis.toString(),
            create_note_et.editText!!.text.toString(),
            isFinished = false
        )
    }

    private fun sendingDataToDatabase() {
        val database = Room.databaseBuilder(
            applicationContext,
            TaskDatabase::class.java,
            "TaskDB"
        ).build()

        Thread {
            database.taskDao().insertTask(getTaskEntity())
        }.start()
    }
}

package com.builder.todolist.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.room.Room
import com.builder.todolist.MainActivity
import com.builder.todolist.R
import com.builder.todolist.database.TaskDatabase
import com.builder.todolist.database.TaskEntity
import kotlinx.android.synthetic.main.activity_new_task.*
import java.util.*

class NewTaskActivity : AppCompatActivity() {

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
                val toast = Toast.makeText(this, "Task Saved", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                val toast = Toast.makeText(this, "Please Fill the Data", Toast.LENGTH_SHORT).show()
            }
        }


        linearLayout_select_date.setOnClickListener {
            handleCalendarButton()
        }

        linearLayout_select_time.setOnClickListener {
            handleTimeButton()
        }
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

            val dataString = android.text.format.DateFormat.format("EEEE, dd MMM yyyy", calendar1)
            create_date_tv.text = dataString

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

            val dataString = android.text.format.DateFormat.format("HH:mm", calendar1)
            create_time_tv.text = dataString

        }, hour, minute, true)

        timePickerDialog.show()
    }

    private fun isDataFill(): Boolean {
        return !(create_date_tv.text == getString(R.string.select_the_date) || create_time_tv.text == getString(R.string.select_the_time) || task_title_et.text.toString() == "")
    }

    private fun getTaskEntity() : TaskEntity {
        return TaskEntity(null, task_title_et.text.toString(), create_date_tv.text.toString(), create_time_tv.text.toString(), create_note_et.text.toString(), isFinished = false)
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

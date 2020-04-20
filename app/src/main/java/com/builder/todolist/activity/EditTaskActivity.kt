package com.builder.todolist.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.room.Room
import com.builder.todolist.MainActivity
import com.builder.todolist.MainActivity.Companion.EXTRA_DATA
import com.builder.todolist.R
import com.builder.todolist.database.TaskDatabase
import com.builder.todolist.model.TaskEntity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
            finish()
        }

        btn_save_task.setOnClickListener {
            if (isDataFill()) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

                Thread(Runnable {
                    updateTask()
                }).start()
            } else {
                textFieldState()
            }
        }

        edit_img_delete.setOnClickListener {
            val alertDialog= MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.delete_all_task))
                .setMessage(getString(R.string.are_you_sure_to_delete_all_completed_task))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)

                    Thread(Runnable {
                        deleteTask()
                    }).start()

                    Toast.makeText(this, "Task Deleted", Toast.LENGTH_SHORT).show()
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

        edit_title_et.editText!!.setOnClickListener {
            edit_title_et.error = null
        }

        edit_date_picker_et.editText!!.setOnClickListener {
            handleCalendarButton()
            edit_date_picker_et.error = null
        }

        edit_time_picker_et.editText!!.setOnClickListener {
            handleTimeButton()
            edit_time_picker_et.error = null
        }
    }

    private fun isDataFill(): Boolean {
        return !(task_title_et.editText!!.text.isEmpty() || new_date_picker_et.editText!!.text.isEmpty() || new_time_picker_et.editText!!.text.isEmpty())
    }

    private fun textFieldState() {
        if (edit_title_et.editText!!.text.isEmpty()) {
            edit_title_et.error = getString(R.string.no_title_error_msg)
        } else {
            edit_title_et.error = null
        }

        if (edit_date_picker_et.editText!!.text.isEmpty()) {
            edit_date_picker_et.error = getString(R.string.no_date_error_msg)
        } else {
            edit_date_picker_et.error = null
        }

        if (edit_time_picker_et.editText!!.text.isEmpty()) {
            edit_time_picker_et.error = getString(R.string.no_time_error_msg)
        } else {
            edit_time_picker_et.error = null
        }
    }

    private fun taskPackage() {
        val taskData = intent.getParcelableExtra<TaskEntity>(EXTRA_DATA)!!

        val date = Calendar.getInstance()
        date.timeInMillis = taskData.date.toLong()

        val time = Calendar.getInstance()
        time.timeInMillis = taskData.time.toLong()

        dateSelected = date
        timeSelected = time

        val dateString = DateFormat.format("EEEE, dd MMM yyyy", date)
        val timeString = DateFormat.format("HH:mm", time)

        edit_title_et.editText!!.setText(taskData.title)
        edit_date_picker_et.editText!!.setText(dateString)
        edit_time_picker_et.editText!!.setText(timeString)
        edit_note_et.editText!!.setText(taskData.note)
        edit_checkbox.isChecked = taskData.isFinished

    }

    private fun updateTask() {
        val database = Room.databaseBuilder(
            applicationContext,
            TaskDatabase::class.java,
            "TaskDB"
        ).build()

        database.taskDao().updateTask(getTaskEntity())
    }

    private fun deleteTask() {
        val database = Room.databaseBuilder(
            applicationContext,
            TaskDatabase::class.java,
            "TaskDB"
        ).build()

        database.taskDao().deleteTask(getTaskEntity())
    }

    private fun getTaskEntity(): TaskEntity {
        val taskData = intent.getParcelableExtra<TaskEntity>(EXTRA_DATA)

        return TaskEntity(
            taskData?.id,
            edit_title_et.editText!!.text.toString(),
            dateSelected.timeInMillis.toString(),
            timeSelected.timeInMillis.toString(),
            edit_note_et.editText?.text.toString(),
            edit_checkbox.isChecked
        )
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

                val dataString = DateFormat.format("EEEE, dd MMM yyyy", calendar1)
                edit_date_picker_et.editText!!.setText(dataString)

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

                val dataString = DateFormat.format("HH:mm", calendar1)
                edit_time_picker_et.editText!!.setText(dataString)

                timeSelected = calendar1

            }, hour, minute, true)

        timePickerDialog.show()
    }
}

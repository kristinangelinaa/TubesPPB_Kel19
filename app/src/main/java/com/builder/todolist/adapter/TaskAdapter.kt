package com.builder.todolist.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.builder.todolist.MainActivity.Companion.EXTRA_DATA
import com.builder.todolist.R
import com.builder.todolist.activity.EditTaskActivity
import com.builder.todolist.model.TaskEntity
import java.util.*

class TaskAdapter(private val context: Context?, private val listTask: List<TaskEntity>) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    private lateinit var taskSelected: TaskEntity

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById(R.id.card_title_tv)
        var tvDate: TextView = itemView.findViewById(R.id.card_date_tv)
        var tvTime: TextView = itemView.findViewById(R.id.card_time_tv)
        var container: CardView = itemView.findViewById(R.id.card_container)
        var imgDone: ImageView = itemView.findViewById(R.id.card_img_done)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.task_card, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listTask.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = listTask[position]
        taskSelected = task

        val dateCalendar = Calendar.getInstance()
        dateCalendar.timeInMillis = task.date.toLong()

        val timeCalendar = Calendar.getInstance()
        timeCalendar.timeInMillis = task.time.toLong()

        val dateString = android.text.format.DateFormat.format("EEEE, dd MMM yyyy", dateCalendar)
        val timeString = android.text.format.DateFormat.format("HH:mm", timeCalendar)

        holder.tvTitle.text = task.title
        holder.tvDate.text = dateString
        holder.tvTime.text = timeString

        holder.container.setOnClickListener {
            val packages =
                TaskEntity(task.id, task.title, task.date, task.time, task.note, task.isFinished)

            val intent = Intent(holder.itemView.context, EditTaskActivity::class.java)
            intent.putExtra(EXTRA_DATA, packages)
            holder.itemView.context.startActivity(intent)
        }

        if (task.isFinished) {
            holder.imgDone.visibility = View.VISIBLE
        } else {
            holder.imgDone.visibility = View.GONE
        }

    }

}
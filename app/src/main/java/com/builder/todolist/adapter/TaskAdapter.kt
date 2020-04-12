package com.builder.todolist.adapter

import android.content.Context
import android.media.Image
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.builder.todolist.R
import com.builder.todolist.database.TaskEntity

class TaskAdapter(private val context: Context?, private val listTask: List<TaskEntity>) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById(R.id.card_title_tv)
        var tvDate: TextView = itemView.findViewById(R.id.card_date_tv)
        var tvTime: TextView = itemView.findViewById(R.id.card_time_tv)
        var btn_edit: ImageView = itemView.findViewById(R.id.card_btn_edit)
        var btn_done: ImageView = itemView.findViewById(R.id.card_btn_done)
        var container: CardView = itemView.findViewById(R.id.card_container)
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

        holder.tvTitle.text = task.title
        holder.tvDate.text = task.date
        holder.tvTime.text = task.time

        //Add click listener method here

    }

}
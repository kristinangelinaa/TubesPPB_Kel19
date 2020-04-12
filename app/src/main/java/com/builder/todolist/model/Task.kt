package com.builder.todolist.model

data class Task(
    var id: String = "",
    var title: String = "",
    var date: String = "",
    var time: String = "",
    var isFinished: Boolean = false
)
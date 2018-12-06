package com.example.todo

typealias TodoId = Long

data class Todo(
        val id: TodoId,
        val content: String,
        val done: Boolean
)
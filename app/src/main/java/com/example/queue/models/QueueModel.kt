package com.example.queue.models

data class QueueModel(
    val code: String,
    val id: Int,
    val creator: Int,
    val name: String,
    val description: String,
    val user_count: Int
)
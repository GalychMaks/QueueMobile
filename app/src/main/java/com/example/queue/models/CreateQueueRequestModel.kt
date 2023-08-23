package com.example.queue.models

data class CreateQueueRequestModel(
    val creator: Int,
    val name: String,
    val description: String
)

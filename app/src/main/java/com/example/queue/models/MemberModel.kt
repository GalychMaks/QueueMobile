package com.example.queue.models

data class MemberModel(
    val id: Int,
    val user: Int,
    val username: String,
    val queue: Int,
    val position: Int
)

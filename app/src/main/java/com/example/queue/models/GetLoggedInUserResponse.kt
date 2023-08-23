package com.example.queue.models

data class GetLoggedInUserResponse(
    val pk: Int,
    val username: String,
    val email: String,
    val first_name: String,
    val second_name: String
)

package com.example.queue.repository

import com.example.queue.api.RetrofitInstance
import com.example.queue.models.LoginRequest
import com.example.queue.models.RegistrationRequest

class Repository {
    suspend fun login(loginRequest: LoginRequest) = RetrofitInstance.api.login(loginRequest)
    suspend fun registration(registrationRequest: RegistrationRequest) = RetrofitInstance.api.registration(registrationRequest)
    suspend fun logout(authorization: String) = RetrofitInstance.api.logout(authorization)
    suspend fun getQueues() = RetrofitInstance.api.getQueues()

}
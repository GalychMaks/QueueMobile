package com.example.queue.api

import com.example.queue.models.Key
import com.example.queue.models.LoginRequest
import com.example.queue.models.QueueModel
import com.example.queue.models.RegistrationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface API {
    @POST("dj-rest-auth/login/")
    suspend fun login(@Body loginRequest: LoginRequest): Response<Key>

    @POST("dj-rest-auth/registration/")
    suspend fun registration(@Body registrationRequest: RegistrationRequest): Response<Void>

    @POST("dj-rest-auth/logout/")
    suspend fun logout(@Header("Authorization") authorization: String): Response<Void>

    @GET("queues/")
    suspend fun getQueues(): Response<List<QueueModel>>


}
package com.example.myqueue.api;

import com.example.myqueue.db.Queue;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {

    @POST("dj-rest-auth/registration/")
    Call<Void> signup(@Body SignUpRequestBody signUpRequestBody);

    @POST("dj-rest-auth/login/")
    Call<LoginResponseBodyModel> login(@Body LoginBodyModel loginBodyModel);

    @POST("dj-rest-auth/logout/")
    Call<String> logout();

    @GET("dj-rest-auth/user/")
    Call<UserModel> getLoggedInUser();

    @GET("queues/")
    Call<List<Queue>> getQueues();

    @GET("/users/{id}/queues/")
    Call<List<Queue>> getQueues(@Path("id") int userId);

    @GET("queues/{queue_id}/")
    Call<Queue> getQueue(@Path("queue_id") int queueId);


    @GET("queues/search/{code}/")
    Call<Queue> getQueue(@Path("code") String code);
    @GET("queues/{queue_id}/members/")
    Call<List<GetMembersResponseBodyModel>> getMembers(@Path("queue_id") int queueId);

    @POST("queues/")
    Call<Queue> createQueue(@Body CreateQueueModel createQueueModel);

    @POST("queues/{queue_id}/members/")
    Call<JoinQueueResponseBody> joinQueue(@Path("queue_id") int queueId, @Body JoinQueueRequestBody joinQueueRequestBody);

    @DELETE("queues/{queue_id}/members/{id}/")
    Call<Void> leaveQueue(@Path("queue_id") String queueId, @Path("id") int userId);

    @DELETE("queues/{id}/")
    Call<Void> deleteQueue(@Path("id") int queueId);
}

package com.example.myqueue.api;

import static com.example.myqueue.DrawerActivity.DrawerActivity.IS_LOGGED_IN;
import static com.example.myqueue.LoginActivity.LoginFragment.LOGGED_IN_USER;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myqueue.DrawerActivity.ProgressBarCallBack;
import com.example.myqueue.QueueListActivity.QueueListActivity;
import com.example.myqueue.Repository;
import com.example.myqueue.db.Queue;
import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebRepository implements Repository {
    private String URL;
    public static final int CONNECTION_TIMEOUT = 20; // seconds
    public static final String LOGGED_IN_KEY = "key";
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private MutableLiveData<List<Queue>> myQueues;
    private MutableLiveData<Queue> queue;
    private MutableLiveData<List<GetMembersResponseBodyModel>> members;

    public WebRepository(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        Retrofit retrofit;

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        URL = "http://" + sharedPreferences.getString("ip", "127.0.0.1") + ":8000/";

        if (sharedPreferences.getBoolean(IS_LOGGED_IN, false)) {
            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
            httpClientBuilder.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
            httpClientBuilder.addInterceptor(httpLoggingInterceptor);
            httpClientBuilder.addInterceptor(chain -> {
                Request originalRequest = chain.request();
                Request.Builder requestBuilder = originalRequest.newBuilder()
                        .header("Authorization", "Token " + sharedPreferences.getString(LOGGED_IN_KEY, null)); // Assuming your API expects the key in an "Authorization" header with a prefix like "Token"

                Request newRequest = requestBuilder.build();
                return chain.proceed(newRequest);
            });

            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClientBuilder.build())
                    .build();
        } else {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(httpLoggingInterceptor)
                    .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
    }

    public void signup(Context context, ProgressBarCallBack progressBarCallBack, SignUpRequestBody signUpRequestBody) {
        Call<Void> call = jsonPlaceHolderApi.signup(signUpRequestBody);
        progressBarCallBack.showProgressDialog();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressBarCallBack.hideProgressDialog();
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "Code " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(context, "Account created", Toast.LENGTH_SHORT).show();
                login(context, progressBarCallBack, signUpRequestBody.getUsername(), signUpRequestBody.getPassword1());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressBarCallBack.hideProgressDialog();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void login(Context context, ProgressBarCallBack progressBarCallBack, String userName, String password) {
        Call<LoginResponseBodyModel> call = jsonPlaceHolderApi.login(new LoginBodyModel(userName, password));

        progressBarCallBack.showProgressDialog();

        call.enqueue(new Callback<LoginResponseBodyModel>() {
            @Override
            public void onResponse(Call<LoginResponseBodyModel> call, Response<LoginResponseBodyModel> response) {
                progressBarCallBack.hideProgressDialog();
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                assert response.body() != null;

                editor.putString(LOGGED_IN_KEY, response.body().getKey());

                OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
                httpClientBuilder.addInterceptor(chain -> {
                    Request originalRequest = chain.request();
                    Request.Builder requestBuilder = originalRequest.newBuilder()
                            .header("Authorization", "Token " + sharedPreferences.getString(LOGGED_IN_KEY, null));
                    // Assuming your API expects the key in an "Authorization" header with a prefix like "Token"

                    Request newRequest = requestBuilder.build();
                    return chain.proceed(newRequest);
                });

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClientBuilder.build())
                        .build();

                jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

                getLoggedInUser(context);

                editor.putBoolean(IS_LOGGED_IN, true);
                editor.commit();

                Intent intent = new Intent(context, QueueListActivity.class);
                context.startActivity(intent);
            }

            @Override
            public void onFailure(Call<LoginResponseBodyModel> call, Throwable t) {
                progressBarCallBack.hideProgressDialog();
                Log.d("onFailure login", t.getMessage());
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void logout(Context context) {
        Call<String> call = jsonPlaceHolderApi.logout();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "code is " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(context, "successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getLoggedInUser(Context context) {
        Call<UserModel> call = jsonPlaceHolderApi.getLoggedInUser();

        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                }

                UserModel user = response.body();
                SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                editor.putString(LOGGED_IN_USER, gson.toJson(user));
                editor.apply();
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public LiveData<List<Queue>> getMyQueues() {
        if (myQueues == null) {
            myQueues = new MutableLiveData<>();
        }
        return myQueues;
    }

    public void updateMyQueues(Context context, ProgressBarCallBack progressBarCallBack, int userId) {
        Call<List<Queue>> call = jsonPlaceHolderApi.getQueues(userId); // change
        progressBarCallBack.showProgressDialog();
        call.enqueue(new Callback<List<Queue>>() {
            @Override
            public void onResponse(Call<List<Queue>> call, Response<List<Queue>> response) {
                progressBarCallBack.hideProgressDialog();
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "Code" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                myQueues.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Queue>> call, Throwable t) {
                progressBarCallBack.hideProgressDialog();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public LiveData<List<GetMembersResponseBodyModel>> getMembers() {
        if (members == null) {
            members = new MutableLiveData<>();
        }
        return members;
    }

    public void updateMembers(Context context, ProgressBarCallBack progressBarCallBack, int queueId) {
        Call<List<GetMembersResponseBodyModel>> call = jsonPlaceHolderApi.getMembers(queueId);
        progressBarCallBack.showProgressDialog();
        call.enqueue(new Callback<List<GetMembersResponseBodyModel>>() {
            @Override
            public void onResponse(Call<List<GetMembersResponseBodyModel>> call, Response<List<GetMembersResponseBodyModel>> response) {
                progressBarCallBack.hideProgressDialog();
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "Code " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                members.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<GetMembersResponseBodyModel>> call, Throwable t) {
                progressBarCallBack.hideProgressDialog();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public LiveData<Queue> getQueue(int queueId) {
        if (queue == null) {
            queue = new MutableLiveData<>();
        }

        Call<Queue> call = jsonPlaceHolderApi.getQueue(queueId);
        call.enqueue(new Callback<Queue>() {
            @Override
            public void onResponse(Call<Queue> call, Response<Queue> response) {
                if (!response.isSuccessful()) {
                    Log.d("on get queue response", "code is " + response.code());
                    return;
                }
                queue.postValue(response.body());
            }

            @Override
            public void onFailure(Call<Queue> call, Throwable t) {
                Log.d("on get queue Failure", t.getMessage());
            }
        });

        return queue;
    }

    public LiveData<Queue> getQueue(ProgressBarCallBack progressBarCallBack, String code) {
        if (queue == null) {
            queue = new MutableLiveData<>();
        }

        Call<Queue> call = jsonPlaceHolderApi.getQueue(code);
        progressBarCallBack.showProgressDialog();
        call.enqueue(new Callback<Queue>() {
            @Override
            public void onResponse(Call<Queue> call, Response<Queue> response) {
                progressBarCallBack.hideProgressDialog();
                if (!response.isSuccessful()) {
                    Log.d("on get queue response", "code is " + response.code());
                    return;
                }
                queue.postValue(response.body());
            }

            @Override
            public void onFailure(Call<Queue> call, Throwable t) {
                progressBarCallBack.hideProgressDialog();
                Log.d("on get queue Failure", t.getMessage());
            }
        });

        return queue;
    }

    public void createQueue(Context context, ProgressBarCallBack progressBarCallBack, CreateQueueModel createQueueModel) {
        Call<Queue> call = jsonPlaceHolderApi.createQueue(createQueueModel);
        progressBarCallBack.showProgressDialog();
        call.enqueue(new Callback<Queue>() {
            @Override
            public void onResponse(Call<Queue> call, Response<Queue> response) {
                progressBarCallBack.hideProgressDialog();
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "Error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(context, "Queue created", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Queue> call, Throwable t) {
                progressBarCallBack.hideProgressDialog();
                Log.d("on post queue Failure", t.getMessage());

            }
        });
    }

    public void joinQueue(Context context, ProgressBarCallBack progressBarCallBack, int queueId, JoinQueueRequestBody joinQueueRequestBody) {
        Call<JoinQueueResponseBody> call = jsonPlaceHolderApi.joinQueue(queueId, joinQueueRequestBody);
        progressBarCallBack.showProgressDialog();
        call.enqueue(new Callback<JoinQueueResponseBody>() {
            @Override
            public void onResponse(Call<JoinQueueResponseBody> call, Response<JoinQueueResponseBody> response) {
                progressBarCallBack.hideProgressDialog();
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "Error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                updateMembers(context, progressBarCallBack, queueId);
            }

            @Override
            public void onFailure(Call<JoinQueueResponseBody> call, Throwable t) {
                progressBarCallBack.hideProgressDialog();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void leaveQueue(Context context, ProgressBarCallBack progressBarCallBack, int queueId, int userId) {
        Call<Void> call = jsonPlaceHolderApi.leaveQueue(queueId + "", userId);
        progressBarCallBack.showProgressDialog();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressBarCallBack.hideProgressDialog();
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "Error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                updateMembers(context, progressBarCallBack, queueId);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressBarCallBack.hideProgressDialog();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void deleteQueue(Context context, ProgressBarCallBack progressBarCallBack, int queueId) {
        Call<Void> call = jsonPlaceHolderApi.deleteQueue(queueId);
        progressBarCallBack.showProgressDialog();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressBarCallBack.hideProgressDialog();
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "Error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressBarCallBack.hideProgressDialog();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

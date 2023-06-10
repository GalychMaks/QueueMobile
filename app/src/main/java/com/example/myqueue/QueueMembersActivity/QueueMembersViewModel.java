package com.example.myqueue.QueueMembersActivity;

import static com.example.myqueue.LoginActivity.LoginFragment.LOGGED_IN_USER;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myqueue.DrawerActivity.ProgressBarCallBack;
import com.example.myqueue.Repository;
import com.example.myqueue.api.GetMembersResponseBodyModel;
import com.example.myqueue.api.JoinQueueRequestBody;
import com.example.myqueue.api.UserModel;
import com.example.myqueue.db.Queue;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class QueueMembersViewModel extends AndroidViewModel {
    private Repository repository;

    @Inject
    public QueueMembersViewModel(@NonNull Application application, Repository repository) {
        super(application);
        this.repository = repository;
    }

    public LiveData<List<GetMembersResponseBodyModel>> getMembers() {
        return repository.getMembers();
    }

    public void updateMembers(Context context, ProgressBarCallBack progressBarCallBack, int queueId) {
        repository.updateMembers(context, progressBarCallBack, queueId);
    }

    public LiveData<Queue> getQueue(int queueId){
        return repository.getQueue(queueId);
    }

    public void joinQueue(Context context, ProgressBarCallBack progressBarCallBack, int queueId, JoinQueueRequestBody joinQueueRequestBody){
        repository.joinQueue(context, progressBarCallBack, queueId, joinQueueRequestBody);
    }

    public void leaveQueue(Context context, ProgressBarCallBack progressBarCallBack, int queueId, int userId){
        repository.leaveQueue(context, progressBarCallBack, queueId, userId);
    }

    public void deleteQueue(Context context, ProgressBarCallBack progressBarCallBack, int queueId){
        repository.deleteQueue(context, progressBarCallBack, queueId);
    }

    public UserModel getLoggedInUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        return gson.fromJson(sharedPreferences.getString(LOGGED_IN_USER, null), new TypeToken<UserModel>(){}.getType());
    }
}

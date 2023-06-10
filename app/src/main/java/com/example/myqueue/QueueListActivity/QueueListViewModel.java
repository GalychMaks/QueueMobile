package com.example.myqueue.QueueListActivity;

import static com.example.myqueue.LoginActivity.LoginFragment.LOGGED_IN_USER;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myqueue.DrawerActivity.ProgressBarCallBack;
import com.example.myqueue.Repository;
import com.example.myqueue.api.CreateQueueModel;
import com.example.myqueue.api.UserModel;
import com.example.myqueue.db.Queue;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class QueueListViewModel extends AndroidViewModel {
    private Repository repository;

    @Inject
    public QueueListViewModel(@NonNull Application application, Repository repository) {
        super(application);
        this.repository = repository;
    }

    public void refresh(Context context, ProgressBarCallBack pb, int userId){
        repository.updateMyQueues(context, pb, userId);
    }

    public LiveData<List<Queue>> getMyQueues(){
        return repository.getMyQueues();
    }

    public void createQueue(Context context, ProgressBarCallBack progressBarCallBack, CreateQueueModel createQueueModel){
        repository.createQueue(context, progressBarCallBack, createQueueModel);
    }

    public UserModel getLoggedInUser(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        return gson.fromJson(sharedPreferences.getString(LOGGED_IN_USER, null), new TypeToken<UserModel>(){}.getType());
    }
}

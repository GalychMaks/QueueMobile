package com.example.myqueue.DrawerActivity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myqueue.Repository;
import com.example.myqueue.db.Queue;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DrawerActivityViewModel extends AndroidViewModel {

    private Repository repository;

    @Inject
    public DrawerActivityViewModel(@NonNull Application application, Repository repository) {
        super(application);
        this.repository = repository;
    }

    public LiveData<Queue> getQueue(ProgressBarCallBack progressBarCallBack, String code) {
        return repository.getQueue(progressBarCallBack, code);
    }
}

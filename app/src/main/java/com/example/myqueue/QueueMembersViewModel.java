package com.example.myqueue;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class QueueMembersViewModel extends AndroidViewModel {
    private QueueRepository repository;
    private LiveData<List<User>> members;

    public QueueMembersViewModel(@NonNull Application application) {
        super(application);
        repository = new QueueRepository(application);
        members = repository.getAllUsers();
    }

    public void insert(User user) {
        repository.insert(user);
    }

    public void update(User user) {
        repository.update(user);
    }

    public void delete(User user) {
        repository.delete(user);
    }

    public LiveData<List<User>> getMembers(int queueId) {
        return members;
    }
}

package com.example.myqueue;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class QueueViewModel extends AndroidViewModel {

    private QueueRepository repository;

    LiveData<List<Queue>> allQueues;


    public QueueViewModel(@NonNull Application application) {
        super(application);
        repository = new QueueRepository(application);
        allQueues = repository.getAllQueues();
    }

    public void insert(Queue queue) {
        repository.insert(queue);
    }

    public void update(Queue queue) {
        repository.update(queue);
    }

    public void delete(Queue queue) {
        repository.delete(queue);
    }

    public LiveData<List<Queue>> getAllQueues() {
        return allQueues;
    }
}

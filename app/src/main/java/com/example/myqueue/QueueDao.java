package com.example.myqueue;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface QueueDao {
    @Insert
    void insert(Queue queue);

    @Update
    void update(Queue queue);

    @Delete
    void delete(Queue queue);

    @Query("SELECT * FROM queue_table")
    LiveData<List<Queue>> getAllQueues();
}

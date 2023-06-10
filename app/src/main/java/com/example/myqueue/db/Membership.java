package com.example.myqueue.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "membership_table")
public class Membership {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "queue_id")
    private int queueId;
    @ColumnInfo(name = "user_id")
    private int userId;

    public Membership(int queueId, int userId) {
        this.queueId = queueId;
        this.userId = userId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getQueueId() {
        return queueId;
    }

    public int getUserId() {
        return userId;
    }
}

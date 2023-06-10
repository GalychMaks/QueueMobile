package com.example.myqueue.db;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Queue {
    private String code;
    private int id;
    @SerializedName("creator")
    private int creatorId;
    private String name;
    private String description;
    @SerializedName("user_count")
    private int userCount;

    public Queue(int id, int creatorId, String name, String description, int userCount) {
        this.id = id;
        this.creatorId = creatorId;
        this.name = name;
        this.description = description;
        this.userCount = userCount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }
}

package com.example.myqueue.api;

import com.google.gson.annotations.SerializedName;

public class GetMembersResponseBodyModel {
    private int id;
    @SerializedName("user")
    private int userId;

    @SerializedName("username")
    private String userName;

    private int queue;

    private int position;

    public GetMembersResponseBodyModel(int id, int userId, String userName, int queue, int position) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.queue = queue;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public int getQueue() {
        return queue;
    }

    public int getPosition() {
        return position;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setQueue(int queue) {
        this.queue = queue;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

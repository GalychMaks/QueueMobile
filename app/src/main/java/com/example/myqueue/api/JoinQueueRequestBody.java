package com.example.myqueue.api;

public class JoinQueueRequestBody {
    private int user;
    private int queue;
    private int position;

    public JoinQueueRequestBody(int user, int queue, int position) {
        this.user = user;
        this.queue = queue;
        this.position = position;
    }

    public int getUser() {
        return user;
    }

    public int getQueue() {
        return queue;
    }

    public int getPosition() {
        return position;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public void setQueue(int queue) {
        this.queue = queue;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

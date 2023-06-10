package com.example.myqueue.api;

import com.google.gson.annotations.SerializedName;

public class CreateQueueModel {
    @SerializedName("creator")
    private int creatorId;

    @SerializedName("name")
    private String queueName;

    private String description;

    public CreateQueueModel(int creatorId, String queueName, String description) {
        this.creatorId = creatorId;
        this.queueName = queueName;
        this.description = description;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public String getQueueName() {
        return queueName;
    }

    public String getDescription() {
        return description;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

package com.example.myqueue;

import java.util.ArrayList;

public class Queue {
    private ArrayList<User> participants;
    private String name, description, code;
    private int id;
    private static int count = 0;

    public Queue(String name, String description, String code) {
        this.name = name;
        this.description = description;
        this.code = code;
        this.id = count;
        count++;
        participants = new ArrayList<>();
    }

    public int getId() {
        return this.id;
    }

    public boolean addParticipant(User user) {
        return participants.add(user);
    }

    public ArrayList<User> getParticipants() {
        return participants;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }
}

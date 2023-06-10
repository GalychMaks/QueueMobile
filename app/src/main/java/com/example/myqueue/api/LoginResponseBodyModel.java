package com.example.myqueue.api;

public class LoginResponseBodyModel {
    private String key;

    public LoginResponseBodyModel(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

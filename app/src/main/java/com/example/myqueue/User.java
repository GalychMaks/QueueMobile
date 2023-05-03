package com.example.myqueue;

public class User {

    private final String email, name, password;
    private final int id;
    private static int count;
    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.id = count;
        count++;
    }

    public int getId() {return id;}
    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}

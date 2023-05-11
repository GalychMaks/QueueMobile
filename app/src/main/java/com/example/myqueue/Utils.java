package com.example.myqueue;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Utils {
    public static final String QUEUE_ID_KEY = "queueId";
    public static final String ALL_USERS_KEY = "all_users";
    public static final String ALL_QUEUES_KEY = "all_queues";
    public static final String IS_LOGGED_IN = "is_logged_in";
    private static Utils instance;
    private SharedPreferences sharedPreferences;

    public static Utils getInstance(Context context) {
        if (null != instance) {
            return instance;
        }
        instance = new Utils(context);
        return instance;
    }

    private Utils(Context context) {

        sharedPreferences = context.getSharedPreferences("alternate_db", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_LOGGED_IN, false);
        editor.commit();

        if (null == getUserList()) {
            initUsers();
        }
        if (null == getQueueList()) {
            initQueues();
        }

    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_LOGGED_IN, isLoggedIn);
        editor.commit();
    }

    public boolean addUser(User1 user1) {
        ArrayList<User1> user1List = getUserList();
        if (null != user1List) {
            if (user1List.add(user1)) {
                Gson gson = new Gson();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(ALL_USERS_KEY);
                editor.putString(ALL_USERS_KEY, gson.toJson(user1List));
                editor.commit();
                return true;
            }
        }
        return false;
    }

    public boolean addQueue(Queue1 queue1) {
        ArrayList<Queue1> queue1List = getQueueList();
        if (null != queue1List) {
            if (queue1List.add(queue1)) {
                Gson gson = new Gson();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(ALL_QUEUES_KEY);
                editor.putString(ALL_QUEUES_KEY, gson.toJson(queue1List));
                editor.commit();
                return true;
            }
        }
        return false;
    }

    public void initUsers() {
        ArrayList<User1> user1List = new ArrayList<>();
        user1List.add(new User1("anton@gmail.com", "anton", "123456"));
        user1List.add(new User1("anatoliy@gmail.com", "anatoliy", "123456"));
        user1List.add(new User1("larysa@gmail.com", "larysa", "123456"));

        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString(ALL_USERS_KEY, gson.toJson(user1List));
        editor.commit();
    }

    public void initQueues() {
        ArrayList<Queue1> queue1List = new ArrayList<>();
        queue1List.add(new Queue1("Algorithms", "Lorem ipsum", "2"));
        Queue1 queue1 = new Queue1("OOP", "queue for OOP", "1");
        ArrayList<User1> user1List = getUserList();
        for (User1 user1 : user1List) {
            queue1.addParticipant(user1);
        }
        queue1List.add(queue1);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString(ALL_QUEUES_KEY, gson.toJson(queue1List));
        editor.commit();
    }

    public ArrayList<User1> getUserList() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<User1>>() {
        }.getType();
        ArrayList<User1> user1s = gson.fromJson(sharedPreferences.getString(ALL_USERS_KEY, null), type);
        return user1s;
    }

    public ArrayList<Queue1> getQueueList() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Queue1>>(){}.getType();
        ArrayList<Queue1> queue1s = gson.fromJson(sharedPreferences.getString(ALL_QUEUES_KEY, null), type);
        return queue1s;
    }

    public User1 findUserByEmail(String email) {
        ArrayList<User1> user1List = getUserList();
        if (null != user1List) {
            for (User1 user1 : user1List) {
                if (user1.getEmail().equals(email)) {
                    return user1;
                }
            }
        }
        return null;
    }

    public Queue1 getQueueByName(String name) {
        ArrayList<Queue1> queue1List = getQueueList();
        for (Queue1 q : queue1List) {
            if (q.getName().equals(name)) {
                return q;
            }
        }
        return null;
    }
}
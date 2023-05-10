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

    public boolean addUser(User user) {
        ArrayList<User> userList = getUserList();
        if (null != userList) {
            if (userList.add(user)) {
                Gson gson = new Gson();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(ALL_USERS_KEY);
                editor.putString(ALL_USERS_KEY, gson.toJson(userList));
                editor.commit();
                return true;
            }
        }
        return false;
    }

    public boolean addQueue(Queue queue) {
        ArrayList<Queue> queueList = getQueueList();
        if (null != queueList) {
            if (queueList.add(queue)) {
                Gson gson = new Gson();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(ALL_QUEUES_KEY);
                editor.putString(ALL_QUEUES_KEY, gson.toJson(queueList));
                editor.commit();
                return true;
            }
        }
        return false;
    }

    public void initUsers() {
        ArrayList<User> userList = new ArrayList<>();
        userList.add(new User("anton@gmail.com", "anton", "123456"));
        userList.add(new User("anatoliy@gmail.com", "anatoliy", "123456"));
        userList.add(new User("larysa@gmail.com", "larysa", "123456"));

        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString(ALL_USERS_KEY, gson.toJson(userList));
        editor.commit();
    }

    public void initQueues() {
        ArrayList<Queue> queueList = new ArrayList<>();
        queueList.add(new Queue("Algorithms", "Lorem ipsum", "2"));
        Queue queue = new Queue("OOP", "queue for OOP", "1");
        ArrayList<User> userList = getUserList();
        for (User user : userList) {
            queue.addParticipant(user);
        }
        queueList.add(queue);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString(ALL_QUEUES_KEY, gson.toJson(queueList));
        editor.commit();
    }

    public ArrayList<User> getUserList() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<User>>() {
        }.getType();
        ArrayList<User> users = gson.fromJson(sharedPreferences.getString(ALL_USERS_KEY, null), type);
        return users;
    }

    public ArrayList<Queue> getQueueList() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Queue>>(){}.getType();
        ArrayList<Queue> queues = gson.fromJson(sharedPreferences.getString(ALL_QUEUES_KEY, null), type);
        return queues;
    }

    public User findUserByEmail(String email) {
        ArrayList<User> userList = getUserList();
        if (null != userList) {
            for (User user : userList) {
                if (user.getEmail().equals(email)) {
                    return user;
                }
            }
        }
        return null;
    }

    public Queue getQueueByName(String name) {
        ArrayList<Queue> queueList = getQueueList();
        for (Queue q : queueList) {
            if (q.getName().equals(name)) {
                return q;
            }
        }
        return null;
    }
}
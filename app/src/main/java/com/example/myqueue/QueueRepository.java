package com.example.myqueue;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class QueueRepository {
    private QueueDao queueDao;
    private UserDao userDao;
    private LiveData<List<Queue>> allQueues;
    private LiveData<List<User>> allUsers;

    public QueueRepository(Application application) {
        QueueDatabase database = QueueDatabase.getInstance(application);
        queueDao = database.queueDao();
        allQueues = queueDao.getAllQueues();
        userDao = database.userDao();
        allUsers = userDao.getAllUsers();
    }

    public void insert(Queue queue) {
        new InsertQueueAsyncTask(queueDao).execute(queue);
    }

    public void update(Queue queue) {
        new UpdateQueueAsyncTask(queueDao).execute(queue);
    }

    public void delete(Queue queue) {
        new DeleteQueueAsyncTask(queueDao).execute(queue);
    }

    public LiveData<List<Queue>> getAllQueues() {
        return allQueues;
    }


    public void insert(User user) {
        new DeleteUserAsyncTask(userDao).execute(user);
    }

    public void update(User user) {
        new DeleteUserAsyncTask(userDao).execute(user);
    }

    public void delete(User user) {
        new DeleteUserAsyncTask(userDao).execute(user);
    }

    public LiveData<List<User>> getAllUsers(){return allUsers;}



    private static class InsertQueueAsyncTask extends AsyncTask<Queue, Void, Void> {
        private QueueDao queueDao;

        private InsertQueueAsyncTask(QueueDao queueDao) {
            this.queueDao = queueDao;
        }

        @Override
        protected Void doInBackground(Queue... queues) {
            queueDao.insert(queues[0]);
            return null;
        }
    }

    private static class UpdateQueueAsyncTask extends AsyncTask<Queue, Void, Void> {
        private QueueDao queueDao;

        private UpdateQueueAsyncTask(QueueDao queueDao) {
            this.queueDao = queueDao;
        }

        @Override
        protected Void doInBackground(Queue... queues) {
            queueDao.update(queues[0]);
            return null;
        }
    }

    private static class DeleteQueueAsyncTask extends AsyncTask<Queue, Void, Void> {
        private QueueDao queueDao;

        private DeleteQueueAsyncTask(QueueDao queueDao) {
            this.queueDao = queueDao;
        }

        @Override
        protected Void doInBackground(Queue... queues) {
            queueDao.delete(queues[0]);
            return null;
        }
    }

    private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        private InsertUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.insert(users[0]);
            return null;
        }
    }

    private static class UpdateUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        private UpdateUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.update(users[0]);
            return null;
        }
    }

    private static class DeleteUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        private DeleteUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.delete(users[0]);
            return null;
        }
    }
}

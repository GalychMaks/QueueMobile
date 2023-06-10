package com.example.myqueue.db;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class LocalRepository {

    private QueueDao queueDao;
    private UserDao userDao;
    private MembershipDao membershipDao;
    private LiveData<List<User>> allUsers;
    private LiveData<List<Queue>> allQueues;

    public LocalRepository(Application application) {
        Database database = Database.getInstance(application);

        userDao = database.userDao();
        allUsers = userDao.getAllUsers();

        queueDao = database.queueDao();
        allQueues = queueDao.getAllQueues();

        membershipDao = database.membershipDao();
    }

    public void insert(Queue queue) {
        new LocalRepository.InsertQueueAsyncTask(queueDao).execute(queue);
    }

    public void update(Queue queue) {
        new LocalRepository.UpdateQueueAsyncTask(queueDao).execute(queue);
    }

    public void delete(Queue queue) {
        new LocalRepository.DeleteQueueAsyncTask(queueDao).execute(queue);
    }

    public Queue getQueue(int queueId){return queueDao.getQueue(queueId);}

    public LiveData<List<Queue>> getAllQueues() {
        return allQueues;
    }

    public void insert(User user) {
        new InsertUserAsyncTask(userDao).execute(user);
    }

    public void update(User user) {
        new UpdateUserAsyncTask(userDao).execute(user);
    }

    public void delete(User user) {
        new DeleteUserAsyncTask(userDao).execute(user);
    }

    public void insert(Membership membership) {
        new InsertMembershipAsyncTask(membershipDao).execute(membership);
    }

    public void update(Membership membership) {
        new UpdateMembershipAsyncTask(membershipDao).execute(membership);
    }

    public void delete(Membership membership) {
        new DeleteMembershipAsyncTask(membershipDao).execute(membership);
    }

    public LiveData<List<User>> getMembers(int queueId) {
        return membershipDao.getMembers(queueId);
    }

    public Membership getMembership(int userId, int queueId){
        return membershipDao.getMembership(userId, queueId);
    }

    public LiveData<List<User>> getAllUsers() {
        return userDao.getAllUsers();
    }

    public User getUser(String email, String password) {
        return userDao.getUser(email, password);
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

    private static class InsertMembershipAsyncTask extends AsyncTask<Membership, Void, Void> {
        private MembershipDao membershipDao;

        private InsertMembershipAsyncTask(MembershipDao membershipDao) {
            this.membershipDao = membershipDao;
        }

        @Override
        protected Void doInBackground(Membership... memberships) {
            membershipDao.insert(memberships[0]);
            return null;
        }
    }

    private static class UpdateMembershipAsyncTask extends AsyncTask<Membership, Void, Void> {
        private MembershipDao membershipDao;

        private UpdateMembershipAsyncTask(MembershipDao membershipDao) {
            this.membershipDao = membershipDao;
        }

        @Override
        protected Void doInBackground(Membership... memberships) {
            membershipDao.update(memberships[0]);
            return null;
        }
    }

    private static class DeleteMembershipAsyncTask extends AsyncTask<Membership, Void, Void> {
        private MembershipDao membershipDao;

        private DeleteMembershipAsyncTask(MembershipDao membershipDao) {
            this.membershipDao = membershipDao;
        }

        @Override
        protected Void doInBackground(Membership... memberships) {
            membershipDao.delete(memberships[0]);
            return null;
        }
    }

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
}

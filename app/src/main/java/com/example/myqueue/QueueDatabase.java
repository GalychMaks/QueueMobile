package com.example.myqueue;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Queue.class, User.class}, version = 1)
public abstract class QueueDatabase extends RoomDatabase {
    private static QueueDatabase instance;

    public abstract QueueDao queueDao();

    public abstract UserDao userDao();

    public static synchronized QueueDatabase getInstance(Context context) {
        if (null == instance) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            QueueDatabase.class,
                            "queue_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private QueueDao queueDao;
        private UserDao userDao;

        private PopulateDbAsyncTask(QueueDatabase db) {
            queueDao = db.queueDao();
            userDao = db.userDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            queueDao.insert(new Queue("Name 1", "Description 1"));
            queueDao.insert(new Queue("Name 2", "Description 2"));
            queueDao.insert(new Queue("Name 3", "Description 3"));
            queueDao.insert(new Queue("Name 7", "Description 3"));
            userDao.insert(new User("Max", "max@gmail.com", "123456"));
            userDao.insert(new User("Joe", "joe@gmail.com", "123456"));
            return null;
        }
    }
}

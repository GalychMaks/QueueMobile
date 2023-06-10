package com.example.myqueue.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

//@androidx.room.Database(entities = {User.class, Membership.class}, version = 1)
public abstract class Database extends RoomDatabase {
    private static Database instance;
    public abstract QueueDao queueDao();
    public abstract UserDao userDao();
    public abstract MembershipDao membershipDao();

    public static synchronized Database getInstance(Context context) {
        if (null == instance) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            Database.class,
                            "queue_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
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
        private MembershipDao membershipDao;

        private PopulateDbAsyncTask(Database db) {
            queueDao = db.queueDao();
            userDao = db.userDao();
            membershipDao = db.membershipDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            User max = new User("Max", "max@gmail.com", "123456");
            userDao.insert(max);
            int maxId = max.getId();

//            queueDao.insert(new Queue(maxId, "All users(id=1)", "max creator"));
//            Queue queue = new Queue(maxId, "OOP", "Queue for OOP");
//            queueDao.insert(queue);

//            membershipDao.insert(new Membership(queue.getId(), maxId));

            return null;
        }
    }
}

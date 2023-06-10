package com.example.myqueue.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MembershipDao {

    @Insert
    void insert(Membership membership);

    @Update
    void update(Membership membershipDao);

    @Delete
    void delete(Membership membershipDao);
    @Query("SELECT user_table.* FROM user_table JOIN " +
            "membership_table ON user_table.id = membership_table.user_id WHERE membership_table.queue_id = :queueId")
    LiveData<List<User>> getMembers(int queueId);

    @Query("SELECT * FROM membership_table WHERE user_id= :userId AND queue_id= :queueId")
    Membership getMembership(int userId, int queueId);
}

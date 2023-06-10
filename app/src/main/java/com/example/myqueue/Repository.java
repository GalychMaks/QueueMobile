package com.example.myqueue;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.myqueue.DrawerActivity.ProgressBarCallBack;
import com.example.myqueue.api.CreateQueueModel;
import com.example.myqueue.api.GetMembersResponseBodyModel;
import com.example.myqueue.api.JoinQueueRequestBody;
import com.example.myqueue.api.SignUpRequestBody;
import com.example.myqueue.db.Queue;

import java.util.List;

public interface Repository {
    void signup(Context context, ProgressBarCallBack progressBarCallBack, SignUpRequestBody signUpRequestBody);

    void login(Context context, ProgressBarCallBack progressBarCallBack, String userName, String password);

    void logout(Context context);

    void getLoggedInUser(Context context);

    LiveData<List<Queue>> getMyQueues();

    void updateMyQueues(Context context, ProgressBarCallBack progressBarCallBack, int userId);

    LiveData<List<GetMembersResponseBodyModel>> getMembers();

    void updateMembers(Context context, ProgressBarCallBack progressBarCallBack, int queueId);

    LiveData<Queue> getQueue(int queueId);

    LiveData<Queue> getQueue(ProgressBarCallBack progressBarCallBack, String code);

    void createQueue(Context context, ProgressBarCallBack progressBarCallBack, CreateQueueModel createQueueModel);

    void joinQueue(Context context, ProgressBarCallBack progressBarCallBack, int queueId, JoinQueueRequestBody joinQueueRequestBody);

    void leaveQueue(Context context, ProgressBarCallBack progressBarCallBack, int queueId, int userId);

    void deleteQueue(Context context, ProgressBarCallBack progressBarCallBack, int queueId);
}

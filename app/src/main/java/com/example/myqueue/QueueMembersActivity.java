package com.example.myqueue;

import static com.example.myqueue.CreateQueueActivity.EXTRA_ID;

import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myqueue.databinding.ActivityQueueBinding;

public class QueueMembersActivity extends DrawerActivity {
    private RecyclerView recViewParticipantList;
    private ActivityQueueBinding activityQueueBinding;
    private QueueMembersViewModel queueMembersViewModel;
    private int queueId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityQueueBinding = ActivityQueueBinding.inflate(getLayoutInflater());
        setContentView(activityQueueBinding.getRoot());

        recViewParticipantList = activityQueueBinding.recViewParticipantList;
        recViewParticipantList.setLayoutManager(new LinearLayoutManager(this));

        QueueMembersRecViewAdapter adapter = new QueueMembersRecViewAdapter();
        recViewParticipantList.setAdapter(adapter);

        Intent intent = getIntent();
        if (null != intent) {
            queueId = intent.getIntExtra(EXTRA_ID, -1);
        }

        queueMembersViewModel = new ViewModelProvider(this).get(QueueMembersViewModel.class);
        queueMembersViewModel.getMembers(queueId);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, QueueListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
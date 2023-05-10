package com.example.myqueue;

import static com.example.myqueue.Utils.QUEUE_ID_KEY;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myqueue.databinding.ActivityQueueBinding;

public class QueueActivity extends DrawerActivity {
    private RecyclerView recViewParticipantList;
    private ActivityQueueBinding activityQueueBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityQueueBinding = ActivityQueueBinding.inflate(getLayoutInflater());
        setContentView(activityQueueBinding.getRoot());

        recViewParticipantList = findViewById(R.id.recViewParticipantList);
        QueueParticipantsRecViewAdapter adapter = new QueueParticipantsRecViewAdapter(this);

        Intent intent = getIntent();
        if (null != intent) {
            String name = intent.getStringExtra(QUEUE_ID_KEY);
            Queue incomingQueue = Utils.getInstance(this).getQueueByName(name);
            if (null != incomingQueue) {
                this.setTitle(incomingQueue.getName());
                adapter.setQueueParticipantList(incomingQueue.getParticipants());
            }
        }

        recViewParticipantList.setAdapter(adapter);
        recViewParticipantList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, QueueListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
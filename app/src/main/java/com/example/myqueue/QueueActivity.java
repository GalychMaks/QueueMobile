package com.example.myqueue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class QueueActivity extends AppCompatActivity {
    public static final String QUEUE_ID_KEY = "queueId";
    private RecyclerView recViewParticipantList;
    private TextView txtQueueName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

        recViewParticipantList = findViewById(R.id.recViewParticipantList);
        QueueParticipantsRecViewAdapter adapter = new QueueParticipantsRecViewAdapter(this);
        txtQueueName = findViewById(R.id.txtQueueName);

        Intent intent = getIntent();
        if(null != intent) {
            String name = intent.getStringExtra(QUEUE_ID_KEY);
                Queue incomingQueue = Utils.getInstance(this).getQueueByName(name);
                if(null != incomingQueue) {
                    txtQueueName.setText(incomingQueue.getName());
                    adapter.setQueueParticipantList(incomingQueue.getParticipants());
                }
        }

        recViewParticipantList.setAdapter(adapter);
        recViewParticipantList.setLayoutManager(new LinearLayoutManager(this));
    }
}
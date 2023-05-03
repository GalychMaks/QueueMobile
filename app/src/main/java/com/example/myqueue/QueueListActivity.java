package com.example.myqueue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class QueueListActivity extends AppCompatActivity {

    private RecyclerView recViewQueueList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue_list);

        recViewQueueList = findViewById(R.id.recViewQueueList);

        QueueRecViewAdapter adapter = new QueueRecViewAdapter(this);
        adapter.setQueueList(Utils.getInstance(this).getQueueList());

        recViewQueueList.setAdapter(adapter);
        recViewQueueList.setLayoutManager(new LinearLayoutManager(this));
    }
}
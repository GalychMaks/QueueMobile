package com.example.myqueue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.myqueue.databinding.ActivityQueueListBinding;

public class QueueListActivity extends DrawerActivity {

    private RecyclerView recViewQueueList;
    private ActivityQueueListBinding activityQueueListBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityQueueListBinding =ActivityQueueListBinding.inflate(getLayoutInflater());
        setContentView(activityQueueListBinding.getRoot());

        recViewQueueList = findViewById(R.id.recViewQueueList);

        QueueRecViewAdapter adapter = new QueueRecViewAdapter(this);
        adapter.setQueueList(Utils.getInstance(this).getQueueList());

        recViewQueueList.setAdapter(adapter);
        recViewQueueList.setLayoutManager(new LinearLayoutManager(this));
    }
}
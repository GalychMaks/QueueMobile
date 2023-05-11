package com.example.myqueue;

import static com.example.myqueue.CreateQueueActivity.EXTRA_ID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.myqueue.databinding.ActivityQueueListBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class QueueListActivity extends DrawerActivity {
    public static final int CREATE_NEW_QUEUE_REQUEST = 1;
    private ActivityQueueListBinding activityQueueListBinding;
    private RecyclerView recViewQueueList;
    private QueueViewModel queueViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityQueueListBinding = ActivityQueueListBinding.inflate(getLayoutInflater());
        setContentView(activityQueueListBinding.getRoot());

        FloatingActionButton btnCreateQueue = activityQueueListBinding.buttonCreateQueue;
        btnCreateQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QueueListActivity.this, CreateQueueActivity.class);
                startActivityForResult(intent, CREATE_NEW_QUEUE_REQUEST);
            }
        });

        recViewQueueList = activityQueueListBinding.recViewQueueList;
        recViewQueueList.setLayoutManager(new LinearLayoutManager(this));

        QueueRecViewAdapter adapter = new QueueRecViewAdapter();
        recViewQueueList.setAdapter(adapter);

        queueViewModel = new ViewModelProvider(this).get(QueueViewModel.class);
        queueViewModel.getAllQueues().observe(this, new Observer<List<Queue>>() {
            @Override
            public void onChanged(List<Queue> queues) {
                adapter.submitList(queues);
            }
        });

        adapter.setOnItemClickListener(new QueueRecViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Queue queue) {
                Intent intent = new Intent(QueueListActivity.this, QueueMembersActivity.class);
                intent.putExtra(EXTRA_ID, queue.getId());
                startActivity(intent);
                Toast.makeText(QueueListActivity.this, "onItemClick", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_NEW_QUEUE_REQUEST && resultCode == RESULT_OK) {
            String name = data.getStringExtra(CreateQueueActivity.EXTRA_NAME);
            String description = data.getStringExtra(CreateQueueActivity.EXTRA_DESCRIPTION);

            Queue queue = new Queue(name, description);
            queueViewModel.insert(queue);

            Toast.makeText(this, "Queue created", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Queue not created", Toast.LENGTH_SHORT).show();
        }
    }
}
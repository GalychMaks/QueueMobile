package com.example.myqueue.QueueListActivity;

import static com.example.myqueue.LoginActivity.LoginFragment.LOGGED_IN_USER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.myqueue.DrawerActivity.DrawerActivity;
import com.example.myqueue.QueueMembersActivity.QueueMembersActivity;
import com.example.myqueue.R;
import com.example.myqueue.api.CreateQueueModel;
import com.example.myqueue.api.UserModel;
import com.example.myqueue.databinding.ActivityQueueListBinding;
import com.example.myqueue.db.Queue;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class QueueListActivity extends DrawerActivity {
    public static final String EXTRA_ID = "com.example.myqueue.QueueListActivity.EXTRA_ID";
    public static final String EXTRA_QUEUE = "com.example.myqueue.QueueListActivity.EXTRA_QUEUE";
    private ActivityQueueListBinding activityQueueListBinding;
    private RecyclerView recViewQueueList;
    private QueueListViewModel queueListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // turn off the dark mode
        super.onCreate(savedInstanceState);
        activityQueueListBinding = ActivityQueueListBinding.inflate(getLayoutInflater());
        setContentView(activityQueueListBinding.getRoot());

        FloatingActionButton btnCreateQueue = activityQueueListBinding.buttonCreateQueue;
        btnCreateQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateQueueDialog createQueueDialog = new CreateQueueDialog(QueueListActivity.this, QueueListActivity.this, queueListViewModel);
                createQueueDialog.show();
            }
        });

        recViewQueueList = activityQueueListBinding.recViewQueueList;
        recViewQueueList.setLayoutManager(new LinearLayoutManager(this));

        QueueRecViewAdapter adapter = new QueueRecViewAdapter();
        recViewQueueList.setAdapter(adapter);

        queueListViewModel = new ViewModelProvider(this).get(QueueListViewModel.class);

        queueListViewModel.getMyQueues().observe(this, new Observer<List<Queue>>() {
            @Override
            public void onChanged(List<Queue> queues) {
                adapter.submitList(queues);
            }
        });
        refresh();

        adapter.setOnItemClickListener(new QueueRecViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Queue queue) {
                Gson gson = new Gson();
                Intent intent = new Intent(QueueListActivity.this, QueueMembersActivity.class);
                intent.putExtra(EXTRA_QUEUE, gson.toJson(queue));
                intent.putExtra(EXTRA_ID, queue.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.queue_list_option_menu, menu);
        return true;
    }

    public void refresh() {
        UserModel loggedInUser = queueListViewModel.getLoggedInUser(this);
        if (loggedInUser != null) {
            activityQueueListBinding.recViewQueueList.setVisibility(View.VISIBLE);
            activityQueueListBinding.txtErrorMassage.setVisibility(View.GONE);
            queueListViewModel.refresh(this, this, loggedInUser.getId());
        } else {
            activityQueueListBinding.recViewQueueList.setVisibility(View.GONE);
            activityQueueListBinding.txtErrorMassage.setVisibility(View.VISIBLE);
            activityQueueListBinding.txtErrorMassage.setText("Error: you not logged in");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnRefresh:
                refresh();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
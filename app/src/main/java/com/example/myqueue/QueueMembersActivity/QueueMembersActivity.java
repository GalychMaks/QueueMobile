package com.example.myqueue.QueueMembersActivity;

import static com.example.myqueue.LoginActivity.LoginFragment.LOGGED_IN_USER;
import static com.example.myqueue.QueueListActivity.QueueListActivity.EXTRA_ID;
import static com.example.myqueue.QueueListActivity.QueueListActivity.EXTRA_QUEUE;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myqueue.DrawerActivity.DrawerActivity;
import com.example.myqueue.QueueListActivity.QueueListActivity;
import com.example.myqueue.R;
import com.example.myqueue.api.GetMembersResponseBodyModel;
import com.example.myqueue.api.JoinQueueRequestBody;
import com.example.myqueue.api.UserModel;
import com.example.myqueue.databinding.ActivityQueueMembersBinding;
import com.example.myqueue.db.Queue;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class QueueMembersActivity extends DrawerActivity implements QueueMembersFragment.FragmentQueueMembersListener, DescriptionFragment.FragmentDescriptionListener {
    private ActivityQueueMembersBinding activityQueueMembersBinding;
    private QueueMembersViewModel queueMembersViewModel;
    private Queue currentQueue;
    private LiveData<List<GetMembersResponseBodyModel>> members;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityQueueMembersBinding = ActivityQueueMembersBinding.inflate(getLayoutInflater());
        setContentView(activityQueueMembersBinding.getRoot());

        queueMembersViewModel = new ViewModelProvider(this).get(QueueMembersViewModel.class);
        members = queueMembersViewModel.getMembers();

        Intent intent = getIntent();
        if (null != intent) {
            Gson gson = new Gson();
            currentQueue = gson.fromJson(intent.getStringExtra(EXTRA_QUEUE),
                    new TypeToken<Queue>() {
                    }.getType());
            if (currentQueue != null) {
                QueueMembersFragment fragment = new QueueMembersFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

                setTitle(currentQueue.getName());
                queueMembersViewModel.updateMembers(QueueMembersActivity.this, QueueMembersActivity.this, currentQueue.getId());
            }
        }
    }

    public void joinQueue() {
        UserModel loggedInUser = queueMembersViewModel.getLoggedInUser(this);
        if (loggedInUser == null) {
            Toast.makeText(this, "Error: you not logged in", Toast.LENGTH_SHORT).show();
            return;
        }
        if (currentQueue == null) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            return;
        }
        queueMembersViewModel.joinQueue(this,
                this,
                currentQueue.getId(),
                new JoinQueueRequestBody(loggedInUser.getId(),
                        currentQueue.getId(),
                        loggedInUser.getId()));
    }

    public void leaveQueue() {
        UserModel loggedInUser = queueMembersViewModel.getLoggedInUser(this);
        if (loggedInUser == null) {
            Toast.makeText(this, "Error: you not logged in", Toast.LENGTH_SHORT).show();
            return;
        }
        if (currentQueue == null) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            return;
        }
        queueMembersViewModel.leaveQueue(this, this, currentQueue.getId(), loggedInUser.getId());
    }

    public void deleteQueue() {
        UserModel loggedInUser = queueMembersViewModel.getLoggedInUser(this);
        if (loggedInUser == null) {
            Toast.makeText(this, "Error: you not logged in", Toast.LENGTH_SHORT).show();
            return;
        }
        if (currentQueue == null) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            return;
        }
        if (loggedInUser.getId() != currentQueue.getCreatorId()) {
            Toast.makeText(this, "Error: only creator can delete his queue", Toast.LENGTH_SHORT).show();
            return;
        }
        queueMembersViewModel.deleteQueue(this, this, currentQueue.getId());
        onBackPressed();
    }

    @Override
    public boolean isUserLoggedIn() {
        UserModel loggedInUser = queueMembersViewModel.getLoggedInUser(this);
        return (loggedInUser != null);
    }

    @Override
    public boolean isUserInQueue() {
        UserModel loggedInUser = queueMembersViewModel.getLoggedInUser(this);
        if (loggedInUser == null || currentQueue == null) {
            return false;
        }
        for (GetMembersResponseBodyModel member : members.getValue()) {
            if (member.getUserId() == loggedInUser.getId()) {
                return true;
            }
        }
        return false;
    }

    public void showDescription() {
        if (currentQueue == null) {
            Toast.makeText(this, "Error: try again", Toast.LENGTH_SHORT).show();
            return;
        }
        DescriptionDialog descriptionDialog = new DescriptionDialog(this, currentQueue);
        descriptionDialog.show();
    }

    @Override
    public LiveData<List<GetMembersResponseBodyModel>> getMembers() {
        return members;
    }

    @Override
    public Queue getCurrentQueue() {
        return currentQueue;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.queue_members_option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnRefresh:
                queueMembersViewModel.updateMembers(this, this, currentQueue.getId());
                break;
            case R.id.optionMenuDescription:
                showDescription();
                break;
            case R.id.optionMenuDeleteQueue:
                deleteQueue();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DescriptionFragment fragment = (DescriptionFragment) getSupportFragmentManager().findFragmentByTag("d");
        if (fragment != null && fragment.isVisible()) {
            QueueMembersFragment f = new QueueMembersFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, f).commit();
            return;
        }
        super.onBackPressed();
    }
}
package com.example.myqueue.QueueListActivity;

import static android.content.Context.MODE_PRIVATE;

import static com.example.myqueue.LoginActivity.LoginFragment.LOGGED_IN_USER;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myqueue.DrawerActivity.ProgressBarCallBack;
import com.example.myqueue.R;
import com.example.myqueue.Repository;
import com.example.myqueue.api.CreateQueueModel;
import com.example.myqueue.api.UserModel;
import com.example.myqueue.db.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CreateQueueDialog extends Dialog {

    private EditText editTextQueueName, editTextDescription;
    private final QueueListViewModel queueListViewModel;
    private final ProgressBarCallBack progressBarCallBack;
    public CreateQueueDialog(Context context, ProgressBarCallBack progressBarCallBack, QueueListViewModel queueListViewModel) {
        super(context);
        this.progressBarCallBack = progressBarCallBack;
        this.queueListViewModel = queueListViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_queue_dialog);

        editTextQueueName = findViewById(R.id.editTextQueueName);
        editTextDescription = findViewById(R.id.editTextDescription);

        Button btnCreateQueue = findViewById(R.id.btnCreateQueue);
        btnCreateQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserModel user = queueListViewModel.getLoggedInUser(getContext());
                if(user == null) {
                    Toast.makeText(getContext(), "Error: you not logged in", Toast.LENGTH_SHORT).show();
                    return;
                }
                String queueName = editTextQueueName.getText().toString();
                String description = editTextDescription.getText().toString();

                queueListViewModel.createQueue(getContext(), progressBarCallBack, new CreateQueueModel(user.getId(), queueName, description));
                dismiss();
            }
        });
    }
}

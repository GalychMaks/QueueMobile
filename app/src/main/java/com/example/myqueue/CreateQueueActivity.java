package com.example.myqueue;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myqueue.databinding.ActivityCreateQueueBinding;

public class CreateQueueActivity extends DrawerActivity {

    private ActivityCreateQueueBinding activityCreateQueueBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCreateQueueBinding = ActivityCreateQueueBinding.inflate(getLayoutInflater());
        setContentView(activityCreateQueueBinding.getRoot());

        Button btnSubmit = activityCreateQueueBinding.btnSubmit;
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CreateQueueActivity.this, "Create", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
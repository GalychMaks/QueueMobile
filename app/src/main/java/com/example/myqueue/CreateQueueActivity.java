package com.example.myqueue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myqueue.databinding.ActivityCreateQueueBinding;

public class CreateQueueActivity extends DrawerActivity {
    public static final String EXTRA_NAME =
            "com.example.myqueue.EXTRA_NAME";
    public static final String EXTRA_DESCRIPTION =
            "com.example.myqueue.EXTRA_DESCRIPTION";

    public static final String EXTRA_ID =
            "com.example.myqueue.EXTRA_ID";

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
                String queueName = activityCreateQueueBinding.editTextQueueName.getText().toString();
                String description = activityCreateQueueBinding.editTextDescription.getText().toString();

                if(queueName.trim().isEmpty() || description.trim().isEmpty()){
                    Toast.makeText(CreateQueueActivity.this, "Please insert a name and description", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent data = new Intent();
                data.putExtra(EXTRA_NAME, queueName);
                data.putExtra(EXTRA_DESCRIPTION, description);

                int id = getIntent().getIntExtra(EXTRA_ID, -1);
                if (id != -1) {
                    data.putExtra(EXTRA_ID, id);
                }

                setResult(RESULT_OK, data);
                finish();
            }
        });
    }
}
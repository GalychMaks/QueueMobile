package com.example.myqueue.DrawerActivity;

import static com.example.myqueue.QueueListActivity.QueueListActivity.EXTRA_QUEUE;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.example.myqueue.QueueMembersActivity.QueueMembersActivity;
import com.example.myqueue.R;
import com.example.myqueue.db.Queue;
import com.google.gson.Gson;


public class SearchQueueDialog extends Dialog {
    EditText editTextCode;
    ProgressBarCallBack progressBarCallBack;
    DrawerActivityViewModel viewModel;
    LifecycleOwner lifecycleOwner;

    public SearchQueueDialog(Context context, ProgressBarCallBack progressBarCallBack, DrawerActivityViewModel viewModel, LifecycleOwner lifecycleOwner) {
        super(context);
        this.progressBarCallBack = progressBarCallBack;
        this.viewModel = viewModel;
        this.lifecycleOwner = lifecycleOwner;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searsh_queue_dialog);

        editTextCode = findViewById(R.id.editTextCode);

        Button btnSearchQueue = findViewById(R.id.btnSearch);
        btnSearchQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = editTextCode.getText().toString();
                viewModel.getQueue(progressBarCallBack, code).observe(lifecycleOwner, new Observer<Queue>() {
                    @Override
                    public void onChanged(Queue queue) {
                        if (queue == null) {
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                            Log.d("tag", "Must be toast");
                            return;
                        }
                        Gson gson = new Gson();
                        Intent intent = new Intent(getContext(), QueueMembersActivity.class);
                        intent.putExtra(EXTRA_QUEUE, gson.toJson(queue));
                        getContext().startActivity(intent);
                    }
                });
                dismiss();
            }
        });
    }
}

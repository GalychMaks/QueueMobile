package com.example.myqueue.QueueMembersActivity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myqueue.R;
import com.example.myqueue.db.Queue;

public class DescriptionDialog extends Dialog {

    private final Queue queue;
    public DescriptionDialog(@NonNull Context context, Queue queue) {
        super(context);
        this.queue = queue;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.description_dialog);

        TextView textView = findViewById(R.id.txtDescriptionToPaste);
        textView.setText(queue.getDescription());

        Button btnCopyCode = findViewById(R.id.btnCopyCode);
        btnCopyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("label", queue.getCode());
                clipboardManager.setPrimaryClip(clipData);

                Toast.makeText(getContext(), "Copied", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnCopyLink = findViewById(R.id.btnCopyLink);
        btnCopyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Nothing yet", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

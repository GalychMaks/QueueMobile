package com.example.myqueue.QueueListActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.myqueue.R;

public class CreateQueueDialog extends AppCompatDialogFragment {

    CreateQueueDialogListener listener;
    EditText queueName, description;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.create_queue_dialog, null);
        builder.setView(view)
                .setTitle("Create Queue")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.createQueue(queueName.getText().toString(), description.getText().toString());
                    }
                });
        queueName = view.findViewById(R.id.editTextQueueName);
        description = view.findViewById(R.id.editTextDescription);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (CreateQueueDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement CreteQueueDialogListener");
        }
    }

    public interface CreateQueueDialogListener {
        void createQueue(String queueName, String description);
    }
}

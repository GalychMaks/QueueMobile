package com.example.myqueue.QueueMembersActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myqueue.R;
import com.example.myqueue.db.Queue;

public class DescriptionFragment extends Fragment {

    private FragmentDescriptionListener listener;

    public interface FragmentDescriptionListener {
        Queue getCurrentQueue();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.description_fragment, container, false);

        Queue currentQueue = listener.getCurrentQueue();
        TextView creator = v.findViewById(R.id.txtCreator);
        TextView code = v.findViewById(R.id.txtCode);
        TextView description = v.findViewById(R.id.txtDescription);

        creator.setText(currentQueue.getCreatorId()+"");
        code.setText(currentQueue.getCode());
        description.setText(currentQueue.getDescription());

        v.findViewById(R.id.btnCopy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("label", code.getText().toString());
                clipboardManager.setPrimaryClip(clipData);

                Toast.makeText(getContext(), "Copied", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentDescriptionListener) {
            listener = (FragmentDescriptionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FragmentDescriptionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}

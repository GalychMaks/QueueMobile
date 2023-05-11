package com.example.myqueue;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QueueMembersRecViewAdapter extends ListAdapter<User, QueueMembersRecViewAdapter.ViewHolder> {
    private QueueMembersRecViewAdapter.OnItemClickListener listener;

    protected QueueMembersRecViewAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<User> DIFF_CALLBACK = new DiffUtil.ItemCallback<User>() {
        @Override
        public boolean areItemsTheSame(User oldItem, User newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(User oldItem, User newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getEmail().equals(newItem.getEmail());
        }
    };

    @NonNull
    @Override
    public QueueMembersRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.queue_participant_item, parent, false);
        return new QueueMembersRecViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QueueMembersRecViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        User currentUser = getItem(position);
        holder.txtParticipantName.setText(currentUser.getName());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtParticipantName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtParticipantName = itemView.findViewById(R.id.txtParticipantName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(User user);
    }

    public void setOnItemClickListener(QueueMembersRecViewAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}

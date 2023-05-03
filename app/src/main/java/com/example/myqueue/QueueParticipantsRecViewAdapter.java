package com.example.myqueue;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QueueParticipantsRecViewAdapter extends RecyclerView.Adapter<QueueParticipantsRecViewAdapter.ViewHolder> {
    private ArrayList<User> queueParticipantList = new ArrayList<>();
    private Context mContext;

    public QueueParticipantsRecViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public QueueParticipantsRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.queue_participant_item, parent, false);
        return new QueueParticipantsRecViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QueueParticipantsRecViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.txtParticipantName.setText(queueParticipantList.get(position).getName());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, queueParticipantList.get(position).getName() + " Selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return queueParticipantList.size();
    }

    public void setQueueParticipantList(ArrayList<User> queueParticipantList) {
        this.queueParticipantList = queueParticipantList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtParticipantName;
        private CardView parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtParticipantName = itemView.findViewById(R.id.txtParticipantName);
            parent = itemView.findViewById(R.id.parent);
        }
    }
}

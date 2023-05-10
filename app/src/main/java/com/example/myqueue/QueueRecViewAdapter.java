package com.example.myqueue;


import static com.example.myqueue.Utils.QUEUE_ID_KEY;

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

public class QueueRecViewAdapter extends RecyclerView.Adapter<QueueRecViewAdapter.ViewHolder>{

    private ArrayList<Queue> queueList = new ArrayList<>();
    private Context mContext;

    public QueueRecViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.queue_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.txtQueueName.setText(queueList.get(position).getName());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, QueueActivity.class);
                intent.putExtra(QUEUE_ID_KEY, queueList.get(position).getName());
                mContext.startActivity(intent);
                //Toast.makeText(mContext, queueList.get(position).getName() + " Selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return queueList.size();
    }

    public void setQueueList(ArrayList<Queue> queueList) {
        this.queueList = queueList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtQueueName;
        private CardView parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtQueueName = itemView.findViewById(R.id.txtQueueName);
            parent = itemView.findViewById(R.id.parent);
        }
    }
}

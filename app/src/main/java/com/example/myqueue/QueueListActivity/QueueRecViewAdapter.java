package com.example.myqueue.QueueListActivity;



import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myqueue.R;
import com.example.myqueue.db.Queue;

public class QueueRecViewAdapter extends ListAdapter<Queue, QueueRecViewAdapter.ViewHolder> {
    private OnItemClickListener listener;

    protected QueueRecViewAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Queue> DIFF_CALLBACK = new DiffUtil.ItemCallback<Queue>() {
        @Override
        public boolean areItemsTheSame(Queue oldItem, Queue newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(Queue oldItem, Queue newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getDescription().equals(newItem.getDescription());
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.queue_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Queue currentQueue = getItem(position);
        holder.queueName.setText(currentQueue.getName());
        holder.shortDescription.setText(currentQueue.getDescription());
        holder.membersCount.setText(currentQueue.getUserCount()+"");
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView queueName;
        private TextView shortDescription;
        private TextView membersCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            queueName = itemView.findViewById(R.id.queueName);
            shortDescription = itemView.findViewById(R.id.shortDescription);
            membersCount = itemView.findViewById(R.id.membersCount);

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
        void onItemClick(Queue queue);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}

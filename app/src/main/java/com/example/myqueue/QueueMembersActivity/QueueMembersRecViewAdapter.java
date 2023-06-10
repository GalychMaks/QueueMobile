package com.example.myqueue.QueueMembersActivity;

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
import com.example.myqueue.api.GetMembersResponseBodyModel;

public class QueueMembersRecViewAdapter extends ListAdapter<GetMembersResponseBodyModel, QueueMembersRecViewAdapter.ViewHolder> {
    private QueueMembersRecViewAdapter.OnItemClickListener listener;

    protected QueueMembersRecViewAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<GetMembersResponseBodyModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<GetMembersResponseBodyModel>() {
        @Override
        public boolean areItemsTheSame(GetMembersResponseBodyModel oldItem, GetMembersResponseBodyModel newItem) {
            return oldItem.getUserId() == newItem.getUserId();
        }

        @Override
        public boolean areContentsTheSame(GetMembersResponseBodyModel oldItem, GetMembersResponseBodyModel newItem) {
            return oldItem.getUserName().equals(newItem.getUserName()) &&
                    oldItem.getPosition()== newItem.getPosition();
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
        GetMembersResponseBodyModel currentGetMembersBodyModel = getItem(position);
        holder.txtParticipantName.setText(currentGetMembersBodyModel.getUserName());
        holder.txtPosition.setText(currentGetMembersBodyModel.getPosition()+"");
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtParticipantName;
        private TextView txtPosition;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtParticipantName = itemView.findViewById(R.id.txtParticipantName);
            txtPosition = itemView.findViewById(R.id.txtPosition);

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
        void onItemClick(GetMembersResponseBodyModel getMembersResponseBodyModel);
    }

    public void setOnItemClickListener(QueueMembersRecViewAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}

package com.example.myqueue.QueueMembersActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myqueue.R;
import com.example.myqueue.api.GetMembersResponseBodyModel;

import java.util.List;

public class QueueMembersFragment extends Fragment {

    RecyclerView recViewParticipantList;
    FragmentQueueMembersListener listener;

    interface FragmentQueueMembersListener {
        LiveData<List<GetMembersResponseBodyModel>> getMembers();
        void joinQueue();
        void leaveQueue();
        boolean isUserLoggedIn();
        boolean isUserInQueue();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.queue_members_fragment, container, false);

        recViewParticipantList = v.findViewById(R.id.recViewParticipantList);
        recViewParticipantList.setLayoutManager(new LinearLayoutManager(getContext()));

        QueueMembersRecViewAdapter adapter = new QueueMembersRecViewAdapter();
        recViewParticipantList.setAdapter(adapter);


        Button btnJoin = v.findViewById(R.id.btnJoin);
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.joinQueue();
            }
        });

        Button btnLeave = v.findViewById(R.id.btnLeave);
        btnLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.leaveQueue();
            }
        });

        listener.getMembers().observe(getViewLifecycleOwner(), new Observer<List<GetMembersResponseBodyModel>>() {
            @Override
            public void onChanged(List<GetMembersResponseBodyModel> members) {
                adapter.submitList(members);
                if (members.isEmpty()) {
                    v.findViewById(R.id.txtThisQueueIsEmpty).setVisibility(View.VISIBLE);
                } else {
                    v.findViewById(R.id.txtThisQueueIsEmpty).setVisibility(View.GONE);
                }
                if(!listener.isUserLoggedIn()) {
                    btnLeave.setVisibility(View.GONE);
                    btnJoin.setVisibility(View.GONE);
                } else {
                    if(listener.isUserInQueue()) {
                        btnLeave.setVisibility(View.VISIBLE);
                        btnJoin.setVisibility(View.GONE);
                    } else {
                        btnLeave.setVisibility(View.GONE);
                        btnJoin.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentQueueMembersListener) {
            listener = (FragmentQueueMembersListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FragmentQueueMembersListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}

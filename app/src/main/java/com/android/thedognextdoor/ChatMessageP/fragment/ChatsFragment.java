package com.android.thedognextdoor.ChatMessageP.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.thedognextdoor.ChatMessageP.Adapters.UsersAdapter;
import com.android.thedognextdoor.ChatMessageP.ChatList;
import com.android.thedognextdoor.ChatMessageP.ChatMessage;
import com.android.thedognextdoor.ProfileP.MyProfileDog;
import com.android.thedognextdoor.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {
    private RecyclerView recyclerView;
    private UsersAdapter usersAdapter;
    private List<MyProfileDog> mUsers;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    private List<ChatList> usersList;

    public ChatsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        recyclerView = view.findViewById(R.id.rv_ChatsFragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        usersList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance()
                .getReference("ChatList").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for (DataSnapshot snapshotC : snapshot.getChildren()) {
                    ChatList chatList = snapshotC.getValue(ChatList.class);
                    usersList.add(chatList);

                }
                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    private void chatList() {
        mUsers = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("MyUsers");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();
                for (DataSnapshot snapshotB : snapshot.getChildren()) {
                    MyProfileDog myProfileDog = snapshotB.getValue(MyProfileDog.class);
                    for (ChatList chatList : usersList) {
                        if (myProfileDog.getId() != null) {
                            if (myProfileDog.getId().equals(chatList.getId())) {
                                mUsers.add(myProfileDog);
                            }
                        }
                    }
                }
                usersAdapter = new UsersAdapter(getContext(), mUsers, true);
                recyclerView.setAdapter(usersAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}
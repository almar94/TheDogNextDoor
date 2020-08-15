package com.android.thedognextdoor.ChatMessageP.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.thedognextdoor.ChatMessageP.Adapters.UsersAdapter;
import com.android.thedognextdoor.MatchesP.MatchesActivity;
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

public class UsersFragment extends Fragment {

    private RecyclerView rv;
    private UsersAdapter usersAdapter;
    private List<MyProfileDog> mUsers;
    MyProfileDog myProfileDog;
    Button btn;

    public UsersFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        rv = view.findViewById(R.id.recyclerViewUsers);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers = new ArrayList<>();
        ReadUsers();
        return view;

    }

private void ReadUsers(){
        final FirebaseUser firebaseUser = FirebaseAuth
                .getInstance().getCurrentUser();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("MyUsers");
    reference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            mUsers.clear();
            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                myProfileDog = postSnapshot.getValue(MyProfileDog.class);
                assert myProfileDog != null;
                assert firebaseUser != null;
                if (myProfileDog.getId() != null) {
                    if (!myProfileDog.getId().equals(firebaseUser.getUid())) {
                        mUsers.add(myProfileDog);
                    }
                    usersAdapter = new UsersAdapter(getContext(), mUsers, true);
                    rv.setAdapter(usersAdapter);
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
}




}
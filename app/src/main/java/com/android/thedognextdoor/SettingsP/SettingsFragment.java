package com.android.thedognextdoor.SettingsP;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.thedognextdoor.MainActivity;
import com.android.thedognextdoor.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {

    public CardView match_me, age, distance, location, edit,exit;
    public Switch show_me;
    Boolean switchState = true;

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        show_me = view.findViewById(R.id.Switch_Show_me);
        match_me = view.findViewById(R.id.CardView_Match_me);
        age = view.findViewById(R.id.CardView_age);
        distance = view.findViewById(R.id.CardView_distance);
        location = view.findViewById(R.id.CardView_Location);
        edit = view.findViewById(R.id.CardView_edit);
        exit = view.findViewById(R.id.CardView_exit);

        show_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchState) {
                    // Show me in the search is enabled
                } else {
                    //  Show me in the search is disabled
                }
            }
        });

        match_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MatchMeActivity.class);
                startActivity(intent);
            }
        });

        age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        distance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
            }
        });

    }
}

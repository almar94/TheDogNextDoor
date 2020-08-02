package com.android.thedognextdoor.SettingsP;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Switch;

import com.android.thedognextdoor.R;

public class MatchMeActivity extends AppCompatActivity {

    public Switch men, women, both;
    Boolean switchState = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_me);
    }
}
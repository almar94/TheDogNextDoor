package com.android.thedognextdoor.FriendsP;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.thedognextdoor.PhotoAlbumP.ShowPhotoAlbum;
import com.android.thedognextdoor.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsProfile extends AppCompatActivity {

    Button photoAlbum;
    public TextView FriendName, FriendAge, FriendGender, FriendDescription, Friend_dogName, Friend_dogGender, Friend_dogDescription;
    private CircleImageView FriendImageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_profile);

        FriendName = findViewById(R.id.FriendName);
        FriendAge = findViewById(R.id.FriendAge);
        FriendGender = findViewById(R.id.FriendGender);
        FriendDescription = findViewById(R.id.FriendDescription);
        Friend_dogName = findViewById(R.id.FrienddogName);
        Friend_dogGender = findViewById(R.id.FrienddogGender);
        Friend_dogDescription = findViewById(R.id.FrienddogDescription);

        photoAlbum = findViewById(R.id.Friend_photoAlbum);
        photoAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendsProfile.this, FriendsPhotoAlbum.class);
                startActivity(intent);
            }
        });
    }
}
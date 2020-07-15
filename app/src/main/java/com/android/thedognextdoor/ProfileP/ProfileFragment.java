package com.android.thedognextdoor.ProfileP;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.android.thedognextdoor.PhotoAlbumP.MyImage;
import com.android.thedognextdoor.PhotoAlbumP.PhotoAlbum;
import com.android.thedognextdoor.PhotoAlbumP.ShowPhotoAlbum;
import com.android.thedognextdoor.R;
import com.android.thedognextdoor.RegistrationP.SignIn;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileFragment extends Fragment {

    public DatabaseReference databaseReference;
    public FloatingActionButton edit;
    Button addPhoto, photoAlbum;
    private TextView myName, myAge, myGender, myDescription, dogName, dogGender, dogDescription;
    String myName2, myAge2, myGender2, myDescription2, dogName2, dogDescription2, dogGender2;

    public FirebaseAuth firebaseAuth;
    public FirebaseDatabase firebaseDatabase;

    public FirebaseStorage firebaseStorage;
    public StorageReference storageReference;

    static MyProfileDog myProfileDog;
    private ImageView myImageView;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseReference = FirebaseDatabase.getInstance().getReference("images/");

        myName = view.findViewById(R.id.myName);
        myAge = view.findViewById(R.id.myAge);
        myGender = view.findViewById(R.id.myGender);
        myDescription = view.findViewById(R.id.myDescription);
        dogName = view.findViewById(R.id.dogName);
        dogGender = view.findViewById(R.id.dogGender);
        dogDescription = view.findViewById(R.id.dogDescription);
        myImageView = view.findViewById(R.id.imageView1);
        addPhoto = view.findViewById(R.id.addPhoto);
        photoAlbum = view.findViewById(R.id.photoAlbum);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("images/");

        //set the profile image
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.getPhotoUrl() != null) {
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .into(myImageView);
        }

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PhotoAlbum.class);
                startActivity(intent);
            }
        });

        photoAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ShowPhotoAlbum.class);
                startActivity(intent);
            }
        });


        DatabaseReference databaseReference = firebaseDatabase.getReference((firebaseAuth.getUid()));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myProfileDog = dataSnapshot.getValue(MyProfileDog.class);
                if (myProfileDog.getMyName() != null) {
                    myName.setText(myProfileDog.getMyName());
                    myAge.setText(myProfileDog.getMyAge());
                    myGender.setText(myProfileDog.getMyGender());
                    myDescription.setText(myProfileDog.getMyDescription());
                    dogName.setText(myProfileDog.getDogName());
                    dogGender.setText(myProfileDog.getDogGender());
                    dogDescription.setText(myProfileDog.getDogDescription());
                } else {
                    Intent intent = new Intent(getContext(), SignIn.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });


        edit = view.findViewById(R.id.floatingActionButton);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        myImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyImage.class);
                startActivity(intent);
            }
        });
    }
}
package com.android.thedognextdoor.PhotoAlbumP;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.thedognextdoor.MainPage;
import com.android.thedognextdoor.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ShowPhotoAlbum extends AppCompatActivity implements PhotoAdapter.onItemClickListener {


    Button  addPhoto, back_to_profile;
    private RecyclerView recyclerView;
    private PhotoAdapter adapter;

    public FirebaseStorage firebaseStorage;
    public DatabaseReference databaseReference;
    public StorageReference storageReference;
    public ValueEventListener mDBListener;
    public FirebaseAuth firebaseAuth;

    private List<Upload> mUpload;
    public StorageReference imageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo_album);

        firebaseAuth = FirebaseAuth.getInstance();
        addPhoto = findViewById(R.id.addPhoto);
        back_to_profile = findViewById(R.id.Back_to_profile);

        recyclerView = findViewById(R.id.rvPhotoAlbum);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUpload = new ArrayList<>();

        adapter = new PhotoAdapter(ShowPhotoAlbum.this, mUpload);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(ShowPhotoAlbum.this);

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowPhotoAlbum.this, AddToPhotoAlbum.class);
                startActivity(intent);
            }
        });

        back_to_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowPhotoAlbum.this, MainPage.class);
                startActivity(intent);
            }
        });



        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference(firebaseAuth.getCurrentUser().getUid()).child("UploadsToStorage");
        databaseReference = FirebaseDatabase.getInstance().getReference(firebaseAuth.getCurrentUser().getUid()).child("Uploads");
        mDBListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUpload.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setKey(postSnapshot.getKey());
                    mUpload.add(upload);
                }
                adapter.notifyDataSetChanged();
                adapter.setData(mUpload);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShowPhotoAlbum.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Long press to delete", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(int position) {
        Upload selectItem = mUpload.get(position);
        final String selectKay = selectItem.getKey();
        imageRef = firebaseStorage.getReferenceFromUrl(selectItem.getPhotoUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReference.child(selectKay).removeValue();
                Toast.makeText(ShowPhotoAlbum.this, "Photo deleted", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ShowPhotoAlbum.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(mDBListener);
    }


}
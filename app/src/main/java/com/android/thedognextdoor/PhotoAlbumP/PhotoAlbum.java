package com.android.thedognextdoor.PhotoAlbumP;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.thedognextdoor.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class PhotoAlbum extends AppCompatActivity {

    ImageView photoAlbum;
    Button buttonUpload, buttonPhotoAlbum, chooseButton;


    private Uri filePath;
    int TAKE_IMAGE_CODE = 200;

    public StorageReference storageReference;
    public DatabaseReference databaseReference;
    public FirebaseAuth firebaseAuth;


    public StorageTask storageTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_album);


        storageReference = FirebaseStorage.getInstance().getReference("Uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("Uploads");

        photoAlbum = findViewById(R.id.photo_album);
        buttonUpload = findViewById(R.id.buttonUpload);
        buttonPhotoAlbum = findViewById(R.id.PhotoAlbum);
        chooseButton = findViewById(R.id.chooseButton);

        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhotoAlbum();
            }
        });
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (storageTask != null && storageTask.isInProgress()) {
                    Toast.makeText(PhotoAlbum.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadPhoto();
                }
            }
        });
        buttonPhotoAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoAlbumClass();
            }
        });
    }

    private void openPhotoAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), TAKE_IMAGE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_IMAGE_CODE && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            Glide.with(this)
                    .load(filePath)
                    .into(photoAlbum);
        }
    }


    private String getExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadPhoto() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        if (filePath != null) {
            StorageReference photoReference = storageReference.child(System.currentTimeMillis()
                    + "." + getExtension(filePath));
            storageTask = photoReference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.setMessage("Uploaded....");
                    Toast.makeText(PhotoAlbum.this, "Upload succesfully", Toast.LENGTH_SHORT).show();
                    Upload upload = new Upload("Photo", taskSnapshot.getUploadSessionUri().toString());
                    String uploadId = databaseReference.push().getKey();
                    databaseReference.child(uploadId).setValue(upload);
                    progressDialog.cancel();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PhotoAlbum.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(PhotoAlbum.this, "no photo selected", Toast.LENGTH_SHORT).show();
        }
    }
    private void photoAlbumClass() {
        Intent intent = new Intent(PhotoAlbum.this, ShowPhotoAlbum.class);
        startActivity(intent);
    }
}
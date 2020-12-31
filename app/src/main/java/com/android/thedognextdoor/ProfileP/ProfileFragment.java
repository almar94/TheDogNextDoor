package com.android.thedognextdoor.ProfileP;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.thedognextdoor.PhotoAlbumP.ShowPhotoAlbum;
import com.android.thedognextdoor.R;
import com.android.thedognextdoor.RegistrationP.SignIn;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    Button photoAlbum;
    private TextView myName, myAge, myGender, myDescription, dogName, dogGender, dogDescription;

    public FirebaseAuth firebaseAuth;
    public FirebaseDatabase firebaseDatabase;
    public StorageReference storageReference;
    public FirebaseUser f_user;
    public DatabaseReference reference;

    private Uri imageUri;
    String mUri;
    private static final int TAKE_IMAGE_CODE = 10021;
    private StorageTask<UploadTask.TaskSnapshot> uploadTask;


    static MyProfileDog myProfileDog;
    private CircleImageView myImageView;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        myName = view.findViewById(R.id.myName);
        myAge = view.findViewById(R.id.myAge);
        myGender = view.findViewById(R.id.myGender);
        myDescription = view.findViewById(R.id.myDescription);
        dogName = view.findViewById(R.id.dogName);
        dogGender = view.findViewById(R.id.dogGender);
        dogDescription = view.findViewById(R.id.dogDescription);
        myImageView = view.findViewById(R.id.imageView1);

        photoAlbum = view.findViewById(R.id.photoAlbum);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //Profile img reference
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("users/"+firebaseAuth.getCurrentUser().getUid()+"/MyProfileImage.jpg");
        //set the profile image
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(myImageView);
            }
        });


        f_user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("MyUsers").child(f_user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myProfileDog = snapshot.getValue(MyProfileDog.class);
                if (myProfileDog.getMyName() != null) {
                    myName.setText(myProfileDog.getMyName());
                    myAge.setText(myProfileDog.getMyAge());
                    myGender.setText(myProfileDog.getMyGender());
                    myDescription.setText(myProfileDog.getMyDescription());
                    dogName.setText(myProfileDog.getDogName());
                    dogGender.setText(myProfileDog.getDogGender());
                    dogDescription.setText(myProfileDog.getDogDescription());
//                    if (myProfileDog.getImageURL() != null) {
//                        Glide.with(getContext())
//                                .load(myProfileDog.getImageURL())
//                                .into(myImageView);
//                    }
                } else {
//                    Intent intent = new Intent(getContext(), SignIn.class);
//                    startActivity(intent);
//                    getActivity().finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        photoAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ShowPhotoAlbum.class);
                startActivity(intent);
            }
        });
        myImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });
        return view;
    }

    private void SelectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), TAKE_IMAGE_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_IMAGE_CODE && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();

            UploadImgToFireBase(imageUri);

        }
    }

    private void UploadImgToFireBase(final Uri uri) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        final StorageReference fileRef = storageReference.child("users/"+firebaseAuth.getCurrentUser().getUid()+"/MyProfileImage.jpg");
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(myImageView);
                        progressDialog.dismiss();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }
}






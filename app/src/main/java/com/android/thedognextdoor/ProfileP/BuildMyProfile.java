package com.android.thedognextdoor.ProfileP;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.thedognextdoor.MainPage;
import com.android.thedognextdoor.R;
import com.android.thedognextdoor.RegistrationP.SignIn;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class BuildMyProfile extends AppCompatActivity {

    Button newSingIn;
    EditText myName, myDescription, dogName, dogDescription;
    String myName2, myAge2, myGender2, myDescription2, dogName2, dogDescription2, dogGender2;
    public RadioGroup statusGroup1, statusGroup2;
    TextView myAge;
    private ProgressBar spinner;

    private static final String TAG = "SearchActivity";
    private static final int REQUEST_CODE = 1;

    private FirebaseAuth firebaseAuth;


    private DatabaseReference databaseReference;

    DatePickerDialog.OnDateSetListener dateSetListener;
    Calendar calendar = Calendar.getInstance();
    Calendar today = Calendar.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_my_profile);

        verifyPermissions();

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(getApplicationContext(), SignIn.class));
        }
        databaseReference = FirebaseDatabase.getInstance().getReference();

        myName = findViewById(R.id.myNameText);
        myAge = findViewById(R.id.myAgeText);
        myDescription = findViewById(R.id.myDescriptionText);
        dogName = findViewById(R.id.dogNameText);
        dogDescription = findViewById(R.id.dogDescriptionText);
        spinner = findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);

        statusGroup1 = findViewById(R.id.radioGroup);
        statusGroup2 = findViewById(R.id.radioGroupDog);

//        myImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent profileIntent = new Intent();
//                profileIntent.setType("image/*");
//                profileIntent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(profileIntent, "Select Image."), PICK_IMAGE);
//            }
//        });

        myAge.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dob = new DatePickerDialog(BuildMyProfile.this, dateSetListener, year, month, day);
                dob.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dob.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year - 1, month + 1, dayOfMonth);
                int age = today.get(Calendar.YEAR) - calendar.get(Calendar.YEAR);
                if (today.get(Calendar.DAY_OF_YEAR) < calendar.get(Calendar.DAY_OF_YEAR)) {
                    age--;
                }
                Integer ageInt = new Integer(age);
                String birthday = ageInt.toString();
                myAge.setText(birthday);
            }
        };

        statusGroup1.check(R.id.radioButtonMale);
        statusGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                myGender2 = "Male";
                switch (checkedId) {
                    case R.id.radioButtonMale:
                        myGender2 = "Male";
                        break;
                    case R.id.radioButtonFemale:
                        myGender2 = "Female";
                        break;
                    case R.id.radioButtonOther:
                        myGender2 = "Other";
                        break;
                }

            }
        });
        statusGroup1.check(R.id.radioButtonMaleDog);
        statusGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                dogGender2 = "Male";
                switch (checkedId) {
                    case R.id.radioButtonMaleDog:
                        dogGender2 = "Male";
                        break;
                    case R.id.radioButtonFemaleDog:
                        dogGender2 = "Female";
                        break;
                }
            }
        });
        newSingIn = findViewById(R.id.newSingIn);

        newSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myName2 = myName.getText().toString().trim();
                myAge2 = myAge.getText().toString().trim();
                myDescription2 = myDescription.getText().toString().trim();
                dogName2 = dogName.getText().toString().trim();
                dogDescription2 = dogDescription.getText().toString().trim();
                if (myName2.isEmpty() && myAge2.isEmpty() && myDescription2.isEmpty() &&
                        dogName2.isEmpty() && dogDescription2.isEmpty()) {
                    String msg = "Please enter information in all fields";
                    Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
                } else {
                    if (!myName2.isEmpty() && !myAge2.isEmpty() && !myDescription2.isEmpty() &&
                            !dogName2.isEmpty() && !dogDescription2.isEmpty()) {
                        MyProfileDog myProfileDog = new MyProfileDog(myName2, myAge2, myGender2, myDescription2,
                                dogName2, dogGender2, dogDescription2);
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        databaseReference.child(user.getUid()).setValue(myProfileDog);
                        Toast.makeText(getApplicationContext(), "User information updated", Toast.LENGTH_LONG).show();
   //                     sendUserData();
                        spinner.setVisibility(View.VISIBLE);
                        Intent intent = new Intent(BuildMyProfile.this, MainPage.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }


//    private void sendUserData() {
//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        // Get "User UID" from Firebase > Authentification > Users.
//        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
//        StorageReference imageReference = storageReference.child(firebaseAuth.getUid()).child("Images").child("Profile Pic"); //User id/Images/Profile Pic.jpg
//        UploadTask uploadTask = imageReference.putFile(imagePath);
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(BuildMyProfile.this, "Error: Uploading profile picture", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(BuildMyProfile.this, "Profile picture uploaded", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void verifyPermissions() {
        Log.d(TAG, "verifyPermissions: asking user for permissions");
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[1]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[2]) == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(BuildMyProfile.this,
                    permissions,
                    REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        verifyPermissions();
    }


}

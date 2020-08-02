package com.android.thedognextdoor.SettingsP;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.thedognextdoor.MainActivity;
import com.android.thedognextdoor.MainPage;
import com.android.thedognextdoor.ProfileP.BuildMyProfile;
import com.android.thedognextdoor.R;
import com.bumptech.glide.Glide;
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
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditActivity extends AppCompatActivity {

    EditText myName, myDescription, dogName, dogDescription;
    String myName1, myDescription1, dogName1, dogDescription1, myAge2, myGender2, dogGender2;
    private Button mBack, mConfirm;
    TextView myAge;
    public RadioGroup statusGroup1, statusGroup2;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    private String userId;

    DatePickerDialog.OnDateSetListener dateSetListener;
    Calendar calendar = Calendar.getInstance();
    Calendar today = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        myName = findViewById(R.id.myName);
        myDescription = findViewById(R.id.myDescription);
        dogName = findViewById(R.id.dogName);
        dogDescription = findViewById(R.id.dogDescription);
        myAge = findViewById(R.id.myAgeText);
        statusGroup1 = findViewById(R.id.radioGroup);
        statusGroup2 = findViewById(R.id.radioGroupDog);


        mBack =  findViewById(R.id.back);
        mConfirm = findViewById(R.id.confirm);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("MyUsers").child(userId);

        getUserInfo();

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                return;
            }
        });

        myAge.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dob = new DatePickerDialog(EditActivity.this, dateSetListener, year, month, day);
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
    }


    private void getUserInfo() {
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("myName")!=null){
                        myName1 = map.get("myName").toString();
                        myName.setText(myName1);
                    }
                    if(map.get("myDescription")!=null){
                        myDescription1 = map.get("myDescription").toString();
                        myDescription.setText(myDescription1);
                    }
                    if(map.get("dogName")!=null){
                        dogName1 = map.get("dogName").toString();
                        dogName.setText(dogName1);
                    }
                    if(map.get("myAge")!=null){
                        myAge2 = map.get("myAge").toString();
                        myAge.setText(myAge2);
                    }
                    if(map.get("dogDescription")!=null){
                        dogDescription1 = map.get("dogDescription").toString();
                        dogDescription.setText(dogDescription1);
                    }
                    if(map.get("myGender")!=null){
                        myGender2 = map.get("myGender").toString();
                    }
                    if(map.get("dogGender")!=null){
                        dogGender2 = map.get("myDescription").toString();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void saveUserInformation() {
        myName1 = myName.getText().toString();
        myDescription1 = myDescription.getText().toString();
        dogName1 = dogName.getText().toString();
        dogDescription1 = dogDescription.getText().toString();

        Map userInfo = new HashMap();
        userInfo.put("myName", myName1);
        userInfo.put("myDescription", myDescription1);
        userInfo.put("dogName", dogName1);
        userInfo.put("dogDescription1", dogDescription1);
        userInfo.put("myAge", myAge2);
        userInfo.put("myGender", myGender2);
        userInfo.put("dogGender", dogGender2);
        mUserDatabase.updateChildren(userInfo);
        Intent intent = new Intent(EditActivity.this, MainPage.class);
        startActivity(intent);
       finish();
    }
}
package com.android.thedognextdoor.RegistrationP;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.thedognextdoor.ProfileP.BuildMyProfile;
import com.android.thedognextdoor.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {

    Button sing;
    final int CODE = 594;
    private TextView Email, Password;
    private String EmailSTR, PasswordSTR;

    private ProgressBar spinner;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        Email = findViewById(R.id.singEmail);
        Password = findViewById(R.id.singPassword);
        spinner = findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);

        sing = findViewById(R.id.buttonSingUp);
        sing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmailSTR = Email.getText().toString();
                PasswordSTR = Password.getText().toString();

                if (TextUtils.isEmpty(EmailSTR)) {
                    Toast.makeText(getApplicationContext(), "Please enter your E-mail address", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(PasswordSTR)) {
                    Toast.makeText(getApplicationContext(), "Please enter your Password", Toast.LENGTH_LONG).show();
                }
                if (PasswordSTR.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter your Password", Toast.LENGTH_LONG).show();
                }
                if (PasswordSTR.length() < 8) {
                    Toast.makeText(getApplicationContext(), "Password must be more than 8 digit", Toast.LENGTH_LONG).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(EmailSTR, PasswordSTR)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(SignUp.this, BuildMyProfile.class);
                                        startActivityForResult(intent, CODE);
                                        finish();
                                    } else {
                                        String msg = task.getException().getMessage();
                                        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });
                }

            }
        });
    }




    @Override
    public void onStart () {
        super.onStart();
        //    Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }


}
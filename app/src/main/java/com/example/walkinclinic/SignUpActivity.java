package com.example.walkinclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.walkinclinic.account.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class SignUpActivity extends AppCompatActivity {
    private EditText fieldFirstName, fieldLastName, fieldEmail, fieldPwd;
    private RadioGroup fieldUserTypeSelection;
    private Button btnSignUp;
    private TextView signIn;
    private char userType;
    private ProgressBar loading;

    // Firebase stuff
    private DatabaseReference ref;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Get input fields
        fieldFirstName = findViewById(R.id.nameFirst);
        fieldLastName = findViewById(R.id.nameLast);
        fieldEmail = findViewById(R.id.email);
        fieldPwd = findViewById(R.id.password);
        fieldUserTypeSelection = findViewById(R.id.userType);
        signIn= findViewById(R.id.alreadyLog);
        btnSignUp = findViewById(R.id.buttonSignUp);
        loading = findViewById(R.id.signUpProgressBar);
        loading.setVisibility(View.GONE);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSignUpBtn();

            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(I);
                finish();
            }
        });

        // Setup Firebase
        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    protected  void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            // handle user already logged in
        }
    }


    public void onClickSignUpBtn () {

        final String firstName = fieldFirstName.getText().toString().trim();
        final String lastName = fieldLastName.getText().toString().trim();
        final String email = fieldEmail.getText().toString().trim();
        final String pwd = fieldPwd.getText().toString().trim();
        final String hashedPwd = getHashedPassword(pwd);

        // Loading wheel
        loading.setVisibility(View.VISIBLE);

        if (fieldUserTypeSelection.getCheckedRadioButtonId() == -1) {
            Toast.makeText(SignUpActivity.this, "Please select your status", Toast.LENGTH_LONG).show();
        }
        if (FieldUtil.fieldsAreValid(firstName, lastName, email, pwd,SignUpActivity.this) && !(fieldUserTypeSelection.getCheckedRadioButtonId() == -1)) {
            // Create user w/ Firebase
            mAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Store in database

                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        ref = FirebaseDatabase.getInstance().getReference();
                        if (userType == 'P') {
                            Patient newUser = new Patient(email, hashedPwd, firstName, lastName,uid);
                            ref.child("patients").child(uid).setValue(newUser);

                            Intent intent = new Intent(getApplicationContext(), PatientSearch.class);   //Application Context and Activity
                            intent.putExtra("USER_FIRSTNAME", firstName);
                            startActivity(intent);//, ProfileActivity.REQUEST_NEW_TEAM);
                            finish();
                        }
                        // userType == 'E'
                        else {
                            Employee newUser = new Employee(email, hashedPwd, firstName, lastName,uid);
                            newUser.setPaymentTypes("000");
                            newUser.setInsuranceTypes("000");
                            newUser.setTitle(" ");
                            newUser.setPhoneNumber(" ");
                            newUser.setAddress(" ");
                            ref.child("employees").child(uid).setValue(newUser);


                            Intent intent = new Intent(getApplicationContext(), EmployeeSetupActivity.class);   //Application Context and Activity
                            intent.putExtra("USER_DATA", newUser);
                            startActivity(intent);//, ProfileActivity.REQUEST_NEW_TEAM);
                            finish();
                        }

                    }
                    else {
                        // Print out error message
                        loading.setVisibility(View.GONE);
                        Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    }
                });
        }
        else {
            loading.setVisibility(View.GONE);
        }
    }


    public void setUserType(View v) {
        int selectedID = fieldUserTypeSelection.getCheckedRadioButtonId();

        if (selectedID == R.id.isPatient) {
            userType = 'P';
        }
        else if (selectedID == R.id.isClinicEmployee) {
            userType = 'E';
        }
    }

    public static String getHashedPassword(String pwd) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(pwd.getBytes());

            byte[] digestedBytes = md.digest();

            StringBuilder hexDigest = new StringBuilder();
            for (byte digestedByte : digestedBytes) {
                hexDigest.append(Integer.toString((digestedByte & 0xff) + 0x100, 16).substring(1));
            }
            return hexDigest.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
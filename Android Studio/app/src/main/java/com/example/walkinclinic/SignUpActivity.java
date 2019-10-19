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
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

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

        loading.setVisibility(View.VISIBLE);
        if (fieldUserTypeSelection.getCheckedRadioButtonId() == -1) {
            Toast.makeText(SignUpActivity.this, "Select your status", Toast.LENGTH_LONG).show();
        }
        if (fieldsAreValid(firstName, lastName, email, pwd,SignUpActivity.this) && !(fieldUserTypeSelection.getCheckedRadioButtonId() == -1)) {
            // Create user w/ Firebase
            mAuth.createUserWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Store in database
                                UserAccount newUser;
                                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                ref = FirebaseDatabase.getInstance().getReference();
                                if (userType == 'P') {
                                    newUser = new Patient(email, hashedPwd, firstName, lastName);
                                }
                                // userType == 'E'
                                else {
                                    newUser = new Employee(email, hashedPwd, firstName, lastName);
                                }
                                ref.child("users").child(uid).setValue(newUser);
                                // Go to Welcome Screen
                                if(userType == 'P'){
                                    Intent intent = new Intent(getApplicationContext(), PatientUI.class);   //Application Context and Activity
                                    intent.putExtra("USER_FIRSTNAME", firstName);
                                    startActivity(intent);//, ProfileActivity.REQUEST_NEW_TEAM);
                                } else {
                                    Intent intent = new Intent(getApplicationContext(), EmployeeUI.class);   //Application Context and Activity
                                    intent.putExtra("USER_FIRSTNAME", firstName);
                                    startActivity(intent);//, ProfileActivity.REQUEST_NEW_TEAM);
                                }
                            }
                            else {
                                // Print out error message
                                loading.setVisibility(View.GONE);
                                Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } else {
            loading.setVisibility(View.GONE);
        }
    }

    public static boolean fieldsAreValid(String firstName, String lastName, String email, String pwd, Context c) {
        // check name length
        if (firstName.length() < 3) {
            Toast.makeText(c, "First name should be at least 3 characters", Toast.LENGTH_LONG).show();
            return false;
        }
        if (lastName.length() < 2) {
            Toast.makeText(c, "Last name should be at least 2 characters", Toast.LENGTH_LONG).show();
            return false;
        }
        // check email pattern
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        if (! matcher.find()) {
            Toast.makeText(c, "Enter a valid email", Toast.LENGTH_LONG).show();
            return false;
        }
        // check password length and match
        if (pwd.length() < 6 ) {
            Toast.makeText(c, "Password must be at least 6 characters", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
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

    private static String getHashedPassword(String pwd) {
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
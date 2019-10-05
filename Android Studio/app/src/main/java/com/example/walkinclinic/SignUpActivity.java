package com.example.walkinclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.walkinclinic.account.*;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private EditText fieldFirstName, fieldLastName, fieldEmail, fieldPwd, fieldPwdConfirm;
    private RadioGroup fieldUserTypeSelection;
    private char userType;
    private Button buttonSignUp;
    private DatabaseReference ref;
    private FirebaseAuth mAuth;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Get input fields
        mAuth = FirebaseAuth.getInstance();
        fieldFirstName = findViewById(R.id.nameFirst);
        fieldLastName = findViewById(R.id.nameLast);
        fieldEmail = findViewById(R.id.email);
        fieldPwd = findViewById(R.id.password);
        fieldPwdConfirm = findViewById(R.id.passwordConfirm);
        fieldUserTypeSelection = findViewById(R.id.userType);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               final String firstName = fieldFirstName.getText().toString().trim();
                final String lastName = fieldLastName.getText().toString().trim();
                final String email = fieldEmail.getText().toString().trim();
                final String pwd = fieldPwd.getText().toString().trim();
                 String pwdConfirm = fieldPwdConfirm.getText().toString().trim();

                mAuth.createUserWithEmailAndPassword(email,pwd)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Patient patient = new Patient(
                                            email,
                                            pwd,
                                            firstName,
                                            lastName

                                    );
                                    FirebaseDatabase.getInstance().getReference("Patients")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(patient).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(SignUpActivity.this, getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                                            } else {
                                                //display a failure message
                                            }
                                        }
                                    });
                                }else {
                                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                if (fieldsAreValid(firstName, lastName, email, pwd, pwdConfirm)) {
                    if (userType == 'P') {
                        mAuth.createUserWithEmailAndPassword(email,pwd)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            Patient patient = new Patient(
                                                    email,
                                                    pwd,
                                                    firstName,
                                                    lastName

                                            );
                                            FirebaseDatabase.getInstance().getReference("Patients")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .setValue(patient).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(SignUpActivity.this, getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                                                    } else {
                                                        //display a failure message
                                                    }
                                                }
                                            });
                                        }else {
                                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }); }
                    // userType == 'E'
                    else {
                        mAuth.createUserWithEmailAndPassword(email,pwd)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            Employee employee = new Employee(
                                                    email,
                                                    pwd,
                                                    firstName,
                                                    lastName

                                            );
                                            FirebaseDatabase.getInstance().getReference("Employee")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .setValue(employee).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(SignUpActivity.this, getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                                                        startActivity(new Intent(SignUpActivity.this, WelcomeActivity.class));
                                                    } else {
                                                        Toast.makeText(SignUpActivity.this.getApplicationContext(),
                                                                "SignUp unsuccessful: " + task.getException().getMessage(),
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }else {
                                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }

                }
            }
        });
    }

    public boolean fieldsAreValid(String firstName, String lastName, String email, String pwd, String pwdConfirm) {
        // check name length
        if (firstName.length() < 3) {
            Toast.makeText(SignUpActivity.this, "First name should be at least 3 characters", Toast.LENGTH_LONG).show();
            return false;
        }
        if (lastName.length() < 2) {
            Toast.makeText(SignUpActivity.this, "Last name should be at least 2 characters", Toast.LENGTH_LONG).show();
            return false;
        }
        // check email pattern
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        if (! matcher.find()) {
            Toast.makeText(SignUpActivity.this, "Enter a valid email", Toast.LENGTH_LONG).show();
            return false;
        }
        // check password length and match
        if (pwd.length() < 6 || pwdConfirm.length() < 6) {
            Toast.makeText(SignUpActivity.this, "Password must be at least 6 characters", Toast.LENGTH_LONG).show();
            return false;
        }
        if (! pwd.equals(pwdConfirm)) {
            Toast.makeText(SignUpActivity.this, "Passwords must match", Toast.LENGTH_LONG).show();
            return false;
        }
        // check if user type is selected
        if (fieldUserTypeSelection.getCheckedRadioButtonId() == -1) {
            Toast.makeText(SignUpActivity.this, "Select your status", Toast.LENGTH_LONG).show();
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
}

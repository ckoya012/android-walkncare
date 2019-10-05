package com.example.walkinclinic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.walkinclinic.account.*;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignUpActivity extends AppCompatActivity {
    private EditText fieldFirstName, fieldLastName, fieldEmail, fieldPwd, fieldPwdConfirm;
    private RadioGroup fieldUserTypeSelection;
    private ProgressBar progressBar;
    private char userType;
    FirebaseAuth mFirebaseAuth;
    private Button buttonSignUp;
    private DatabaseReference ref;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Get input fields
        fieldFirstName = findViewById(R.id.nameFirst);
        fieldLastName = findViewById(R.id.nameLast);
        fieldEmail = findViewById(R.id.email);
        fieldPwd = findViewById(R.id.password);
        fieldPwdConfirm = findViewById(R.id.passwordConfirm);
        fieldUserTypeSelection = findViewById(R.id.userType);
        progressBar.setVisibility(View.GONE);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        mFirebaseAuth = FirebaseAuth.getInstance();
        findViewById(R.id.buttonSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userType == 'P') {
                    registerPatient();
                    //bring to home page

                } else {
                    registerEmployee();
                    //bring to home page
                }
            }
        });

    }

    private void registerPatient() {
        final String fname = fieldFirstName.getText().toString().trim();
        final String lname = fieldLastName.getText().toString().trim();
        final String email = fieldEmail.getText().toString().trim();
        final String password = fieldPwd.getText().toString().trim();
        String cpassword = fieldPwdConfirm.getText().toString().trim();

        if (fname.isEmpty()) {
            fieldFirstName.setError("Please enter first name");
            fieldFirstName.requestFocus();
            return;
        }
        if (lname.isEmpty()) {
            fieldLastName.setError("Please enter last name");
            fieldLastName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            fieldEmail.setError("Please enter email address");
            fieldEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            fieldPwd.setError("Please enter a password");
            fieldPwd.requestFocus();
            return;
        }

        if (!password.equals(cpassword)) {
            Toast.makeText(SignUpActivity.this, "Passwords must match", Toast.LENGTH_LONG).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Patient patient = new Patient(email, password, fname, lname);

                            FirebaseDatabase.getInstance().getReference("Patient")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(patient).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, "User Registered !", Toast.LENGTH_SHORT).show();
                                    } else {
                                        //display a failure message
                                    }
                                }

                            });
                        } else {
                            //display a failure message
                        }
                    }
                });
    }

    private void registerEmployee() {
        final String fname = fieldFirstName.getText().toString().trim();
        final String lname = fieldLastName.getText().toString().trim();
        final String email = fieldEmail.getText().toString().trim();
        final String password = fieldPwd.getText().toString().trim();
        String cpassword = fieldPwdConfirm.getText().toString().trim();

        if (fname.isEmpty()) {
            fieldFirstName.setError("Please enter first name");
            fieldFirstName.requestFocus();
            return;
        }
        if (lname.isEmpty()) {
            fieldLastName.setError("Please enter last name");
            fieldLastName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            fieldEmail.setError("Please enter email address");
            fieldEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            fieldPwd.setError("Please enter a password");
            fieldPwd.requestFocus();
            return;
        }

        if (!password.equals(cpassword)) {
            Toast.makeText(SignUpActivity.this, "Passwords must match", Toast.LENGTH_LONG).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Employee employee = new Employee(email, password, fname, lname);

                            FirebaseDatabase.getInstance().getReference("Patient")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(employee).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, "Walk in Clinic Registered !", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(SignUpActivity.this, "Walk in Clinic Not Registered !", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });
                        } else {
                            Toast.makeText(SignUpActivity.this, "Walk in Clinic Not Registered !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public void setUserType(View v) {
        int selectedID = fieldUserTypeSelection.getCheckedRadioButtonId();

        if (selectedID == R.id.isPatient) {
            userType = 'P';
        } else if (selectedID == R.id.isClinicEmployee) {
            userType = 'E';
        }
    }


}

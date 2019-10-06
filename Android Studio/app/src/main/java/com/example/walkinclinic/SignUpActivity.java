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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private EditText fieldFirstName, fieldLastName, fieldEmail, fieldPwd, fieldPwdConfirm;
    private RadioGroup fieldUserTypeSelection;
    private char userType;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    // Firebase stuff
    private DatabaseReference ref;
    private FirebaseAuth mAuth;

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

    public void onClickSignUpBtn (View v) {
        final String firstName = fieldFirstName.getText().toString().trim();
        final String lastName = fieldLastName.getText().toString().trim();
        final String email = fieldEmail.getText().toString().trim();
        final String pwd = fieldPwd.getText().toString().trim();
        String pwdConfirm = fieldPwdConfirm.getText().toString().trim();

        if (fieldsAreValid(firstName, lastName, email, pwd, pwdConfirm)) {
            // Create user w/ Firebase
            mAuth.createUserWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Store in database
                                UserAccount newUser;
                                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                if (userType == 'P') {
                                    newUser = new Patient(email, pwd, firstName, lastName);
                                    ref = FirebaseDatabase.getInstance().getReference().child("patients");
                                }
                                // userType == 'E'
                                else {
                                    newUser = new Employee(email, pwd, firstName, lastName);
                                    ref = FirebaseDatabase.getInstance().getReference().child("employees");
                                }
                                ref.child(uid).setValue(newUser);
                                Toast.makeText(SignUpActivity.this, "Sign up successful!", Toast.LENGTH_LONG).show();

                                // Go to Welcome Screen
                                startActivity(new Intent(SignUpActivity.this, WelcomeActivity.class));
                            }
                            else {
                                // Print out error message
                                Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
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

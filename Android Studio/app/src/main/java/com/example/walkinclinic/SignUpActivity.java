package com.example.walkinclinic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private EditText fieldFirstName, fieldLastName, fieldEmail, fieldPwd, fieldPwdConfirm;
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
        buttonSignUp = findViewById(R.id.buttonSignUp);
        // ref = FirebaseDatabase.getInstance().getReference().child("users");

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = fieldFirstName.getText().toString().trim();
                String lastName = fieldLastName.getText().toString().trim();
                String email = fieldEmail.getText().toString().trim();
                String pwd = fieldPwd.getText().toString().trim();
                String pwdConfirm = fieldPwdConfirm.getText().toString().trim();

                if (fieldsAreValid(firstName, lastName, email, pwd, pwdConfirm)) {
                    Toast.makeText(SignUpActivity.this, "Registration successful!", Toast.LENGTH_LONG).show();
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
        return true;
    }
}

package com.example.walkinclinic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.content.pm.ActivityInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth; //used to authenticate user
    private boolean isPatient; //keeps track whether user is patient or not
    private TextView signUp;    //clickable text for going to sign up screen
    private ProgressBar loading;    //loading view for when you need to load

    // Valid email pattern
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // prevents changing orientation
        signUp= findViewById(R.id.redirectSignup);
        mAuth = FirebaseAuth.getInstance();
        loading = findViewById(R.id.progressBar);
        loading.setVisibility(View.GONE);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true); //caches login data for faster login in the future
    }

    public void onLoginClicked(View view) {
        loading.setVisibility(View.VISIBLE);
        EditText email = (EditText) findViewById(R.id.loginEmail);
        EditText password = (EditText) findViewById(R.id.loginPwd);
        String sEmail = email.getText().toString().trim();
        String sPwd = password.getText().toString().trim();

        if (sEmail.equals("admin") && sPwd.equals("5T5ptQ")) { // hard code for admin login
            Intent intent = new Intent(getApplicationContext(), AdminMainActivity.class);   //Application Context and Activity
            startActivity(intent);//, ProfileActivity.REQUEST_NEW_TEAM);
            finish();
        }
        else {
            if (emailIsValid(sEmail)) { // checks if login inputs are valid
                //creates listener used to authenticate login
                mAuth.signInWithEmailAndPassword(sEmail, sPwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        getData(); // if valid login, get user data
                    } else {
                        Toast.makeText(LoginActivity.this, "Login has failed!", Toast.LENGTH_LONG).show();
                        loading.setVisibility(View.GONE);
                    }
                    }
                });
            }
            else {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_LONG).show();
                loading.setVisibility(View.GONE);
            }
        }
    }

    public static boolean emailIsValid(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();
    }

    public void onSignUp(View view){
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);   //Application Context and Activity
        startActivity(intent);//, ProfileActivity.REQUEST_NEW_TEAM);
        finish();
    }

    private void getData(){
        final String userID = mAuth.getCurrentUser().getUid();

        // Determine if the user is a patient
        DatabaseReference patientsRef = FirebaseDatabase.getInstance().getReference().child("patients");
        patientsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userID)) {
                    String firstNameData = dataSnapshot.child(userID).child("nameFirst").getValue(String.class);
                    Intent intent = new Intent(getApplicationContext(), PatientMainActivity.class);
                    intent.putExtra("USER_FIRSTNAME", firstNameData);
                    startActivity(intent);
                    finish();
                } else {
                    return;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        // Determine if the user is an employee
        DatabaseReference employeesRef = FirebaseDatabase.getInstance().getReference().child("employees");
        employeesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userID)) {
                    String firstNameData = dataSnapshot.child(userID).child("nameFirst").getValue(String.class);
                    Intent intent = new Intent(getApplicationContext(), EmployeeMainActivity.class);
                    intent.putExtra("USER_FIRSTNAME", firstNameData);
                    startActivity(intent);
                    finish();
                } else {
                    return;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}

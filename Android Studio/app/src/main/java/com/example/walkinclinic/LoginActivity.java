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



public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth; //used to authenticate user
    private boolean isPatient; //keeps track whether user is patient or not
    private TextView signUp;    //clickable text for going to sign up screen
    private ProgressBar loading;    //loading view for when you need to load
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

        if(SignUpActivity.fieldsAreValid("bob","bob",sEmail,sPwd,LoginActivity.this)){ // checks if login inputs are valid
            if (sEmail.equals("admin") && sPwd.equals("5T5ptQ")) { // hard code for admin login
                Intent intent = new Intent(getApplicationContext(), AdminUI.class);   //Application Context and Activity
                startActivity(intent);//, ProfileActivity.REQUEST_NEW_TEAM);
            } else {
                //creates listener used to authenticate login
                mAuth.signInWithEmailAndPassword(sEmail, sPwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            getData(); // if valid login, get user data
                        } else {
                            Toast.makeText(LoginActivity.this, "Login failed!", Toast.LENGTH_LONG).show();
                            loading.setVisibility(View.GONE);
                        }
                    }
                });

            }
        }else{
            loading.setVisibility(View.GONE);
        }
    }

    public void onSignUp(View view){
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);   //Application Context and Activity
        startActivity(intent);//, ProfileActivity.REQUEST_NEW_TEAM);
    }

    private void getData(){
        String userID = mAuth.getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(userID);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) { // creates listener for datachanges, gets data using data snapshot
                boolean typeData = dataSnapshot.child("isEmployee").getValue(Boolean.class); // gets user type
                String firstNameData = dataSnapshot.child("nameFirst").getValue(String.class); // gets user first name
                if(typeData){
                    Intent intent = new Intent(getApplicationContext(), EmployeeUI.class);   //starts activity
                    intent.putExtra("USER_FIRSTNAME", firstNameData);
                    startActivity(intent);//, ProfileActivity.REQUEST_NEW_TEAM);
                } else {
                    Intent intent = new Intent(getApplicationContext(), PatientUI.class);   //starts activity
                    intent.putExtra("USER_FIRSTNAME", firstNameData);
                    startActivity(intent);//, ProfileActivity.REQUEST_NEW_TEAM);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Login failed! DATABASE ERROR: "+databaseError, Toast.LENGTH_LONG).show();
            }
        };
        ref.addValueEventListener(postListener);
    }
}

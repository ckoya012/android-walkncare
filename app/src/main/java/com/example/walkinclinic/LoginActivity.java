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

import com.example.walkinclinic.account.Employee;
import com.example.walkinclinic.account.Patient;
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


    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    // Valid email pattern

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // prevents changing orientation
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true); //caches login data for faster login in the future
        signUp= findViewById(R.id.redirectSignup);
        mAuth = FirebaseAuth.getInstance();
        loading = findViewById(R.id.progressBar);
        loading.setVisibility(View.GONE);


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
                    String email = dataSnapshot.child(userID).child("email").getValue(String.class);
                    String pwd = dataSnapshot.child(userID).child("password").getValue(String.class);
                    String firstName = dataSnapshot.child(userID).child("nameFirst").getValue(String.class);
                    String lastName = dataSnapshot.child(userID).child("nameLast").getValue(String.class);
                    String id = dataSnapshot.child(userID).child("id").getValue(String.class);
//                    String currentAppointment = dataSnapshot.child(userID).child("appointment").getValue(String.class);

                    Intent intent = new Intent(getApplicationContext(), PatientSearchActivity.class);
                    Patient user = new Patient(email, pwd, firstName, lastName, id);
                    intent.putExtra("USER_DATA", user);
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
                    String lastNameData = dataSnapshot.child(userID).child("nameLast").getValue(String.class);;
                    String address = dataSnapshot.child(userID).child("address").getValue(String.class);;
                    String email = dataSnapshot.child(userID).child("email").getValue(String.class);;
                    String id = dataSnapshot.child(userID).child("id").getValue(String.class);;
                    String insuranceTypes = dataSnapshot.child(userID).child("insuranceTypes").getValue(String.class);;
                    String paymentTypes = dataSnapshot.child(userID).child("paymentTypes").getValue(String.class);;
                    String phone = dataSnapshot.child(userID).child("phone").getValue(String.class);;
                    String title = dataSnapshot.child(userID).child("title").getValue(String.class);;
                    String firstNameData = dataSnapshot.child(userID).child("nameFirst").getValue(String.class);
                    String password = dataSnapshot.child(userID).child("password").getValue(String.class);
                    Intent intent = new Intent(getApplicationContext(), EmployeeMainActivity.class);

                    Employee user = new Employee(email,password,firstNameData,lastNameData,id);
                    user.setTitle(title);
                    user.setInsuranceTypes(insuranceTypes);
                    user.setPaymentTypes(paymentTypes);
                    user.setPhoneNumber(phone);
                    user.setAddress(address);
                    intent.putExtra("USER_DATA", user);
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

    public static boolean emailIsValid(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();
    }

}

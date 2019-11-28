package com.example.walkinclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.walkinclinic.account.Employee;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmployeeMainActivity extends AppCompatActivity {

    private Employee user;
    private TextView textNumServices;
    private DatabaseReference refClinicServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_main);

        // set variables
        user = (Employee) getIntent().getSerializableExtra("USER_DATA");
        textNumServices = findViewById(R.id.textNumServicesOffered);
        refClinicServices = FirebaseDatabase.getInstance().getReference("employees").child(user.getID()).child("services");

        // set welcome message text
        TextView message = findViewById(R.id.welcomeMsgEmp);
        message.setText("Welcome " + user.getNameFirst() + "! Your clinic has...");
    }

    // onStart will be used to update the textViews according to the database in real time
    @Override
    protected void onStart() {
        super.onStart();

        // Update the number of services in real time
        refClinicServices.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long numServices = 0;

                // iterate through the types of services
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    numServices += snapshot.getChildrenCount();
                }

                textNumServices.setText(numServices + " Services offered");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public void setInfoClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), EmployeeSetupActivity.class);
        intent.putExtra("USER_DATA", user);
        startActivity(intent);
    }

    public void manageServicesClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), EmployeeServiceManagementActivity.class);
        intent.putExtra("USER_DATA", user);
        startActivity(intent);
    }

    public void signOutClicked (View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

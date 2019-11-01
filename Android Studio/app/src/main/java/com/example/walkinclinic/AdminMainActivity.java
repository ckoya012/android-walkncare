package com.example.walkinclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminMainActivity extends AppCompatActivity {
    // Label to display number of users and services in the database
    TextView numEmployeeLabel, numPatientsLabel, numServicesLabel;
    DatabaseReference refEmployees, refPatients, refServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        numEmployeeLabel = findViewById(R.id.numEmployeesLabel);
        numPatientsLabel= findViewById(R.id.numPatientsLabel);
        numServicesLabel = findViewById(R.id.numServicesLabel);

        refEmployees = FirebaseDatabase.getInstance().getReference("employees");
        refPatients = FirebaseDatabase.getInstance().getReference("patients");
        refServices = FirebaseDatabase.getInstance().getReference("services");
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Update the number of employees in real time
        refEmployees.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long numEmployees = dataSnapshot.getChildrenCount();
                numEmployeeLabel.setText(numEmployees + " Employees registered");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        // Update the number of patients in real time
        refPatients.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long numPatients = dataSnapshot.getChildrenCount();
                numPatientsLabel.setText(numPatients + " Patients registered");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        // Update the number of services in real time
        refServices.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long numServices = dataSnapshot.getChildrenCount();
                numServicesLabel.setText(numServices + " Services available");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public void userManagementClicked(View view){
        Intent intent = new Intent(getApplicationContext(), AdminUserManagement.class);
        startActivity(intent);
    }

    public void serviceManagementClicked(View view){
        Intent intent = new Intent(getApplicationContext(), AdminServiceManagement.class);
        startActivity(intent);
    }
}

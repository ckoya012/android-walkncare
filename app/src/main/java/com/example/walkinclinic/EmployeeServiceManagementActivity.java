package com.example.walkinclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.walkinclinic.account.Employee;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmployeeServiceManagementActivity extends AppCompatActivity {

    private Employee user;
    TextView numGPLabel, numInjectionLabel, numTestLabel, numAdminLabel;
    DatabaseReference refGPServices, refInjectionServices, refTestServices, refAdminServices;
    private CardView manageGP, manageInjection, manageTest, manageAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_service_management);

        user = (Employee) getIntent().getSerializableExtra("USER_DATA");

        // labels to display number of services
        numGPLabel = findViewById(R.id.numGPServicesLabel);
        numInjectionLabel = findViewById(R.id.numInjectionServicesLabel);
        numTestLabel = findViewById(R.id.numTestServicesLabel);
        numAdminLabel = findViewById(R.id.numAdminServicesLabel);

        // DB references
        refGPServices = FirebaseDatabase.getInstance().getReference("employees").child(user.getID()).child("services").child("gp");
        refInjectionServices = FirebaseDatabase.getInstance().getReference("employees").child(user.getID()).child("services").child("injection");
        refTestServices = FirebaseDatabase.getInstance().getReference("employees").child(user.getID()).child("services").child("test");
        refAdminServices = FirebaseDatabase.getInstance().getReference("employees").child(user.getID()).child("services").child("administrative");

        // cards for clicking
        manageGP = findViewById(R.id.cardManageGPServices);
        manageInjection = findViewById(R.id.cardManageInjectionServices);
        manageTest = findViewById(R.id.cardManageTestServices);
        manageAdmin = findViewById(R.id.cardManageAdminServices);


        // set onClick listeners for each card, to determine the corresponding String to send to the next activity
        manageGP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manageServicesClicked(view, "GP");
            }
        });

        manageInjection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manageServicesClicked(view, "Injection");
            }
        });

        manageTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manageServicesClicked(view, "Test");
            }
        });

        manageAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manageServicesClicked(view, "Administrative");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Update the number of GP services in real time
        refGPServices.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long numGPServices = dataSnapshot.getChildrenCount();
                String pluralServices = numGPServices == 1 ? "service" : "services";
                numGPLabel.setText(numGPServices + " General Practice " + pluralServices);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        // Update the number of injection services in real time
        refInjectionServices.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long numInjectionServices = dataSnapshot.getChildrenCount();
                String pluralServices = numInjectionServices == 1 ? "service" : "services";
                numInjectionLabel.setText(numInjectionServices + " Injection " + pluralServices);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        // Update the number of test services in real time
        refTestServices.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long numTestServices = dataSnapshot.getChildrenCount();
                String pluralServices = numTestServices == 1 ? "service" : "services";
                numTestLabel.setText(numTestServices + " Test " + pluralServices);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        // Update the number of admin services in real time
        refAdminServices.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long numAdminServices = dataSnapshot.getChildrenCount();
                String pluralServices = numAdminServices == 1 ? "service" : "services";
                numAdminLabel.setText(numAdminServices + " Administrative " + pluralServices);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public void manageServicesClicked(View view, String typeOfService) {
        Intent intent = new Intent(getApplicationContext(), EmployeeServiceListViewerActivity.class);   //Application Context and Activity
        intent.putExtra("TYPE_OF_SERVICE", typeOfService);
        intent.putExtra("USER_DATA", user);
        startActivity(intent);
    }
}

package com.example.walkinclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminServiceManagement extends AppCompatActivity {

    private CardView GP, injection, test, admin;
    TextView numGPLabel, numInjectionLabel, numTestLabel, numAdminLabel;
    DatabaseReference refGPServices, refInjectionServices, refTestServices, refAdminServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_service_management);

        // Cards for clicking
        GP = findViewById(R.id.GPServicesCard);
        injection= findViewById(R.id.injectionServicesCard);
        test = findViewById(R.id.testServicesCard);
        admin= findViewById(R.id.adminServicesCard);

        // Labels to display number of services
        numGPLabel = findViewById(R.id.numGPServicesLabel);
        numInjectionLabel = findViewById(R.id.numInjectionServicesLabel);
        numTestLabel = findViewById(R.id.numTestServicesLabel);
        numAdminLabel = findViewById(R.id.numAdminServicesLabel);

        // DB references
        refGPServices = FirebaseDatabase.getInstance().getReference("services").child("gp");
        refInjectionServices = FirebaseDatabase.getInstance().getReference("services").child("injection");
        refTestServices = FirebaseDatabase.getInstance().getReference("services").child("test");
        refAdminServices = FirebaseDatabase.getInstance().getReference("services").child("administrative");

        // set onClick listeners for each card, to determine the corresponding String to send to the next activity
        GP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), AdminServiceListViewerActivity.class);
                intent.putExtra("TYPE_OF_SERVICE", "GP"); // needed for Firebase
                startActivity(intent);
            }
        });

        injection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), AdminServiceListViewerActivity.class);
                intent.putExtra("TYPE_OF_SERVICE", "Injection"); // needed for Firebase
                startActivity(intent);
            }
        });

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), AdminServiceListViewerActivity.class);
                intent.putExtra("TYPE_OF_SERVICE", "Test"); // needed for Firebase
                startActivity(intent);
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), AdminServiceListViewerActivity.class);
                intent.putExtra("TYPE_OF_SERVICE", "Administrative"); // needed for Firebase
                startActivity(intent);
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
                numGPLabel.setText(numGPServices + " General Practice services");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        // Update the number of injection services in real time
        refInjectionServices.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long numInjectionServices = dataSnapshot.getChildrenCount();
                numInjectionLabel.setText(numInjectionServices + " Injection services");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        // Update the number of test services in real time
        refTestServices.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long numTestServices = dataSnapshot.getChildrenCount();
                numTestLabel.setText(numTestServices + " Test services");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        // Update the number of admin services in real time
        refAdminServices.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long numAdminServices = dataSnapshot.getChildrenCount();
                numAdminLabel.setText(numAdminServices + " Administrative services");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
}

package com.example.walkinclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.walkinclinic.account.Employee;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EmployeeMainActivity extends AppCompatActivity {
    private Employee user;
    private CardView manageGP, manageInjection, manageTest, manageAdmin;

    ListView scheduleList;
    List<DailySchedule> scheduleLists;

    DatabaseReference scheduleRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_main);
        TextView message = (TextView) findViewById(R.id.welcomeMsgEmp);
        user = (Employee) getIntent().getSerializableExtra("USER_DATA");

        scheduleList = (ListView) findViewById(R.id.scheduleList);
        scheduleLists = new ArrayList<>();


        scheduleRef = FirebaseDatabase.getInstance().getReference("employees").child(user.getID()).child("schedule");


        message.setText("Welcome " + user.getNameFirst() + "! This is your clinic.");

        // set click listeners for the service-management buttons
        manageGP = findViewById(R.id.cardManageGPServices);
        manageInjection = findViewById(R.id.cardManageInjectionServices);
        manageTest = findViewById(R.id.cardManageTestServices);
        manageAdmin = findViewById(R.id.cardManageAdminServices);


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
        // DB references for schedule

    }

    public void setInfoClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), EmployeeSetupActivity.class);   //Application Context and Activity
        intent.putExtra("USER_DATA", user);
        startActivity(intent);
    }

    public void manageServicesClicked(View view, String typeOfService) {
        Intent intent = new Intent(getApplicationContext(), EmployeeServiceManagementActivity.class);   //Application Context and Activity
        intent.putExtra("TYPE_OF_SERVICE", typeOfService);
        intent.putExtra("USER_DATA", user);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        scheduleRef.addListenerForSingleValueEvent(new ValueEventListener() {
            DatabaseReference mondayRef = scheduleRef.child("1_monday");
            DatabaseReference tuesdayRef = scheduleRef.child("2_tuesday");
            DatabaseReference wednesdayRef = scheduleRef.child("3_wednesday");
            DatabaseReference thursdayRef = scheduleRef.child("4_thursday");
            DatabaseReference fridayRef = scheduleRef.child("5_friday");
            DatabaseReference saturdayRef = scheduleRef.child("6_saturday");
            DatabaseReference sundayRef = scheduleRef.child("7_sunday");

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                scheduleLists.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    String day = postSnapshot.getKey();
                    String start = postSnapshot.getValue().toString();
                    String end = postSnapshot.child(day).child("time2").getValue(String.class);


                    DailySchedule dailySchedule = new DailySchedule(start, end);
                    dailySchedule.setDay(day);
                    scheduleLists.add(dailySchedule);
                }

                ScheduleList scheduleAdapter = new ScheduleList(EmployeeMainActivity.this, scheduleLists);
                scheduleList.setAdapter(scheduleAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

package com.example.walkinclinic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.walkinclinic.account.Employee;
import com.google.firebase.database.DatabaseReference;

public class EmployeeMainActivity extends AppCompatActivity {
    private Employee user;
    private Button manageGP;
    private Button manageInjection;
    private Button manageTest;
    private Button manageAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_main);
        TextView message = (TextView) findViewById(R.id.welcomeMsgEmp);
        user = (Employee) getIntent().getSerializableExtra("USER_DATA");

        message.setText("Welcome " + user.getNameFirst() + "! You are logged in as an employee.");

        // set click listeners for the service-management buttons
        manageGP = findViewById(R.id.btnManageGPServices);
        manageInjection = findViewById(R.id.btnManageInjectionServices);
        manageTest = findViewById(R.id.btnManageTestServices);
        manageAdmin = findViewById(R.id.btnManageAdminServices);


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


}

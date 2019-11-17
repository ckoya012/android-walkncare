package com.example.walkinclinic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class EmployeeServiceManagementActivity extends AppCompatActivity {

    private String typeOfService;
    private TextView typeOfServiceLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_service_management);

        typeOfService = (String) getIntent().getExtras().get("TYPE_OF_SERVICE");
        typeOfService = typeOfService.toLowerCase();
        typeOfServiceLabel = findViewById(R.id.serviceTypeLabelEmp);
        typeOfServiceLabel.setText("Add/remove " + typeOfService + " services offered by your clinic");
    }
}

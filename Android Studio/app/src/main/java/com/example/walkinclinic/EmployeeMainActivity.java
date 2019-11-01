package com.example.walkinclinic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class EmployeeMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_main);
        TextView message = (TextView)findViewById(R.id.welcomeMsgEmp);
        message.setText("Welcome "+(getIntent().getStringExtra("USER_FIRSTNAME"))+"! You are logged in as an employee.");
    }
}

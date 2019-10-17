package com.example.walkinclinic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class EmployeeUI extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_ui);
        TextView message = (TextView)findViewById(R.id.welcomeMsgEmp);
        message.setText("Welcome "+(getIntent().getStringExtra("USER_FIRSTNAME"))+"! You are logged in as a employee.");
    }
}

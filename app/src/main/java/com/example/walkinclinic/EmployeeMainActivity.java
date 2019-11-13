package com.example.walkinclinic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.walkinclinic.account.Employee;

public class EmployeeMainActivity extends AppCompatActivity {
    private Employee user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_main);
        TextView message = (TextView)findViewById(R.id.welcomeMsgEmp);
        user = (Employee) getIntent().getSerializableExtra("USER_DATA");
        message.setText("Welcome "+user.getNameFirst()+"! You are logged in as an employee.");
    }

    public void setInfoClicked(View view){
        Intent intent = new Intent(getApplicationContext(), EmployeeSetupActivity.class);   //Application Context and Activity
        intent.putExtra("USER_DATA", user);
        startActivity(intent);//, ProfileActivity.REQUEST_NEW_TEAM);
    }
}

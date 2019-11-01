package com.example.walkinclinic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class PatientMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);
        TextView message = (TextView)findViewById(R.id.welcomeMsgPat);
        message.setText("Welcome "+(getIntent().getStringExtra("USER_FIRSTNAME"))+"! You are logged in as a patient.");
    }
}

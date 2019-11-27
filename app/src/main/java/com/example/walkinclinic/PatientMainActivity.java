package com.example.walkinclinic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class PatientMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);
        TextView message = (TextView)findViewById(R.id.welcomeMsgPat);
        message.setText("Welcome "+(getIntent().getStringExtra("USER_FIRSTNAME"))+"! You are logged in as a patient.");
    }

    public void goToBookAppointmentActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), PatientBookAppointmentActivity.class);
        startActivity(intent);
    }

    public void onClickSignOut (View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}

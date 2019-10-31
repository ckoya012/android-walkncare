package com.example.walkinclinic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminUI extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_ui);
    }

    public void userManagementClicked(View view){
        Intent intent = new Intent(getApplicationContext(), UserManagement.class);
        startActivity(intent);
    }
}

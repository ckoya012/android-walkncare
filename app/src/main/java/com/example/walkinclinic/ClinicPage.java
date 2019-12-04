package com.example.walkinclinic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.walkinclinic.PatientSearchActivity;
import com.example.walkinclinic.account.Patient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ClinicPage extends AppCompatActivity {
    String clinicName;
    String clinincId;
    DatabaseReference ref;
    DatabaseReference dbRate;
    DatabaseReference dbNumUsers;
    DatabaseReference refTitle;
    DatabaseReference commentRef;
    DatabaseReference firstComment;


    TextView cName;

    RatingBar rateBar;
    int numUsers;
    float averageR;
    Button submitBtn;
    EditText userComment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_page);
        submitBtn = findViewById(R.id.submitBtn);
        rateBar = findViewById(R.id.userRating);
        userComment = (EditText) findViewById(R.id.userComments);
        clinincId = (String) getIntent().getExtras().get("CLINIC_ID");
        ref = FirebaseDatabase.getInstance().getReference("employees").child(clinincId);
        refTitle = ref.child("title");


        dbRate = ref.child("rating");
        dbNumUsers = ref.child("num_users");
        //commentRef = ref.child("comments");


    }


    @Override
    protected void onStart() {
        super.onStart();
        if (dbRate == null) {
            dbRate.setValue("0.0");
        }
        dbRate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Object averageF = dataSnapshot.getValue(Object.class);
                averageR = Float.valueOf(averageF.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if (dbNumUsers == null) {
            dbNumUsers.setValue("0");
        }

        dbNumUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Object.class) == null) {
                    DatabaseReference tmpRef = dataSnapshot.getRef();
                    tmpRef.setValue(0);
                }
                Object nUF = dataSnapshot.getValue(Object.class);
                numUsers = Integer.valueOf(nUF.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        refTitle.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = dataSnapshot.getValue(String.class);
                cName = findViewById(R.id.clinicName);
                cName.setText(title);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /*commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //String commentID= commentRef.getKey();
                if(dataSnapshot==null) {
                    firstComment = ref.push();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


    }

    void computeAverage(float rateF, int numUser) {
        ;

        float prevIn = averageR * (numUser - 1);
        float newRateAverage = (rateF + prevIn) / numUser;
        dbRate.setValue(String.valueOf(newRateAverage));
        dbNumUsers.setValue(String.valueOf(numUser));

    }


    public void submitRate(View view) {

        DatabaseReference commentRef = ref.child("comments");
        String userC = userComment.getText().toString().trim();
        float userRate = rateBar.getRating();
        if(validComment(userC) == true && validRate(userRate) == true)
        {
            commentRef.push().setValue(userC);
        }
        else {
            Toast.makeText(getApplicationContext(), "Please add a valid rate and comment", Toast.LENGTH_LONG).show();
        }
        numUsers++;
        computeAverage(userRate, numUsers);
        //Intent intent = new Intent(getApplicationContext(), PatientSearchActivity.class);
        //startActivity(intent);
        finish();


    }

    public boolean validRate(Float s){
       boolean result = true;
       float threshold = 1;
       if(s<=threshold){
           result = false;
       }

       return result;
    }

    public boolean validComment(String c){
        boolean result = true;
        String threshold = null;
        if(c.equals(threshold)){
            result = false;
        }
        return result;
    }

}



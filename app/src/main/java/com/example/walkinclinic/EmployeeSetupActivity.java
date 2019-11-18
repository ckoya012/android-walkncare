package com.example.walkinclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.walkinclinic.account.Employee;
import com.example.walkinclinic.account.Patient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmployeeSetupActivity extends AppCompatActivity {
    private DatabaseReference ref;
    private String uid,sInsurance,sPayment;
    private FirebaseAuth mAuth;
    private EditText addressView,phoneView,titleView;
    private char[] insurance, payment;
    private CheckBox in1,in2,in3,p1,p2,p3;
    private Employee user;
    private boolean valid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_setup);

        addressView = findViewById(R.id.addressView);
        phoneView = findViewById(R.id.numberView);
        titleView = findViewById(R.id.clinicView);
        in1 = findViewById(R.id.diBox);
        in2 = findViewById(R.id.ppBox);
        in3 = findViewById(R.id.wcBox);
        p1 = findViewById(R.id.ccBox);
        p2 = findViewById(R.id.dcBox);
        p3 = findViewById(R.id.cBox);
        user = (Employee) getIntent().getSerializableExtra("USER_DATA");
        uid = user.getID();
        ref = FirebaseDatabase.getInstance().getReference().child("employees").child(uid);

    }

    @Override
    protected void onStart(){
        super.onStart();
        insurance = new char[3];
        payment = new char[3];
        valid = false;
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                addressView.setText(dataSnapshot.child("address").getValue(String.class));
                phoneView.setText(dataSnapshot.child("phoneNumber").getValue(String.class));
                titleView.setText(dataSnapshot.child("title").getValue(String.class));
                sInsurance = dataSnapshot.child("insuranceTypes").getValue(String.class);
                sPayment = dataSnapshot.child("paymentTypes").getValue(String.class);
                insurance[0] = sInsurance.charAt(0);
                insurance[1] = sInsurance.charAt(1);
                insurance[2] = sInsurance.charAt(2);
                payment[0] = sPayment.charAt(0);
                payment[1] = sPayment.charAt(1);
                payment[2] = sPayment.charAt(2);
                if(insurance[0] == '1'){
                    in1.setChecked(true);
                }
                if(insurance[1] == '1'){
                    in2.setChecked(true);
                }
                if(insurance[2] == '1'){
                    in3.setChecked(true);
                }
                if(payment[0] == '1'){
                    p1.setChecked(true);
                }
                if(payment[1] == '1'){
                    p2.setChecked(true);
                }
                if(payment[2] == '1'){
                    p3.setChecked(true);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

    }

    public void setButtonClicked(View view){



        if(in1.isChecked()){
            insurance[0] = '1';
        } else {
            insurance[0] = '0';
        }
        if(in2.isChecked()){
            insurance[1] = '1';
        } else {
            insurance[1] = '0';
        }
        if(in3.isChecked()){
            insurance[2] = '1';
        } else {
            insurance[2] = '0';
        }

        if(p1.isChecked()){
            payment[0] = '1';
        } else {
            payment[0] = '0';
        }
        if(p2.isChecked()){
            payment[1] = '1';
        } else {
            payment[1] = '0';
        }
        if(p3.isChecked()){
            payment[2] = '1';
        } else {
            payment[2] = '0';
        }
        boolean valid = true;
        if(addressView.getText().toString().trim().length() <= 3){
            valid = false;
            Toast.makeText(EmployeeSetupActivity.this, "Invalid Address, Try Again", Toast.LENGTH_LONG).show();
        } else {
            user.setAddress(addressView.getText().toString().trim());
        }

        if(phoneView.getText().toString().trim().length() != 10){
            valid = false;
            Toast.makeText(EmployeeSetupActivity.this, "Invalid Phone Number, Try Again", Toast.LENGTH_LONG).show();
        } else {
            user.setPhoneNumber(phoneView.getText().toString().trim());
        }

        if(titleView.getText().toString().trim().length() <= 3){
            valid = false;
            Toast.makeText(EmployeeSetupActivity.this, "Invalid Clinic Name, Try Again", Toast.LENGTH_LONG).show();
        } else {
            user.setTitle((titleView.getText().toString().trim()));
        }

        if(new String(payment).equals("000")){
            valid = false;
            Toast.makeText(EmployeeSetupActivity.this, "Select At Least 1 Payment Method", Toast.LENGTH_LONG).show();
        } else {
            user.setPaymentTypes(new String(payment));
        }

        if(new String(insurance).equals("000")){
            valid = false;
            Toast.makeText(EmployeeSetupActivity.this, "Select At Least 1 Insurance Type", Toast.LENGTH_LONG).show();
        } else {
            user.setInsuranceTypes(new String(insurance));
        }

        if(valid == true) {
            ref.setValue(user);
            Toast.makeText(EmployeeSetupActivity.this, "Profile Information Set", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), EmployeeMainActivity.class);   //Application Context and Activity
            intent.putExtra("USER_DATA", user);
            startActivity(intent);//, ProfileActivity.REQUEST_NEW_TEAM);
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        Button btn = (Button)findViewById(R.id.setButton);
        btn.performClick();
    }

}

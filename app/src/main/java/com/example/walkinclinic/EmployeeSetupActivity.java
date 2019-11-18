package com.example.walkinclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.walkinclinic.account.Employee;
import com.example.walkinclinic.account.Patient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmployeeSetupActivity extends AppCompatActivity  {
    private DatabaseReference ref;
    private String uid,sInsurance,sPayment;
    private FirebaseAuth mAuth;
    private EditText addressView,phoneView,titleView;
    private char[] insurance, payment;
    private CheckBox in1,in2,in3,p1,p2,p3;
    private CheckBox c1, c2, c3, c4, c5, c6, c7;
    private Employee user;
    private boolean valid;
    private Spinner spinner1,spinner2,spinner3,spinner4,
            spinner5,spinner6,spinner7,spinner8,spinner9,
            spinner10,spinner11,spinner12,spinner13,spinner14;
    private String[] timeTable = new String[24];

    public String timeSlot(int position){
        int hour=12;
        int minute= 00;
        timeTable[0]=hour+":"+minute;
        for(int i=1;i<24;i++){
            if(i%2==0){
                hour= (hour+1)%12;
                minute= 00;
            }else{
                minute=30;
            }
            timeTable[i]=hour+":"+minute;
        }
        return timeTable[position];
    }
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

        c1 = findViewById(R.id.closedBox1);// closedBox for Monday
        c2 = findViewById(R.id.closedBox2); // closedBox for Tuesday
        c3 = findViewById(R.id.closedBox3);// closedBox for Wednesday
        c4 = findViewById(R.id.closedBox4);// closedBox for Thursday
        c5 = findViewById(R.id.closedBox5);// closedBox for Friday
        c6 = findViewById(R.id.closedBox6);//  closedBox for Saturday
        c7 = findViewById(R.id.closedBox7); // closedBox for Sunday

        spinner1= findViewById(R.id.spinnerAmMonday);
        spinner2= findViewById(R.id.spinnerPmMonday);
        spinner3= findViewById(R.id.spinnerAmTuesday);
        spinner4= findViewById(R.id.spinnerPmTuesday);
        spinner5= findViewById(R.id.spinnerAmWednesday);
        spinner6= findViewById(R.id.spinnerPmWednesday);
        spinner7= findViewById(R.id.spinnerAmThrusday);
        spinner8= findViewById(R.id.spinnerPmThrusday);
        spinner9= findViewById(R.id.spinnerAmFriday);
        spinner10= findViewById(R.id.spinnerPmFriday);
        spinner11= findViewById(R.id.spinnerAmSaturday);
        spinner12= findViewById(R.id.spinnerPmSaturday);
        spinner13= findViewById(R.id.spinnerAmSunday);
        spinner14= findViewById(R.id.spinnerPmSunday);

        // Spinner click listener



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

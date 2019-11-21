package com.example.walkinclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;


import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.walkinclinic.account.Employee;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EmployeeSetupActivity extends AppCompatActivity  {
    private DatabaseReference ref;
    private DatabaseReference scheduleRef;
    private String uid, sInsurance, sPayment;
    private FirebaseAuth mAuth;
    private EditText addressView, phoneView, titleView;
    private char[] insurance, payment;
    private CheckBox in1, in2, in3, p1, p2, p3;
    private Employee user;
    private DailySchedule dayHours;

    private boolean valid;
    private Spinner spinner1, spinner2, spinner3, spinner4,
            spinner5, spinner6, spinner7, spinner8, spinner9,
            spinner10, spinner11, spinner12, spinner13, spinner14;

    private String[] timeTableAm = new String[25];
    private String[] timeTablePm = new String[25];
    String[] dayOfWeek = {"1_monday", "2_tuesday", "3_wednesday", "4_thursday", "5_friday", "6_saturday", "7_sunday"};

    public String[] timeOptionAm() {
        timeTableAm[0] = "  ";
        int hour = 12;
        for (int i = 0; i < 24; i++) {
            if (i == 0) {
                timeTableAm[1] = "12:00 am";
            } else {
                if (i % 2 == 0) {
                    hour = (hour + 1) % 12;
                    timeTableAm[i + 1] = hour + ":00 am";
                } else {
                    timeTableAm[i + 1] = hour + ":30 am";
                }
            }
        }
        return timeTableAm;
    }

    public String[] timeOptionPm() {
        timeTablePm[0] = "  ";
        int hour = 12;
        for (int i = 0; i < 24; i++) {
            if (i == 0) {
                timeTablePm[1] = "12:00 pm";
            } else {
                if (i % 2 == 0) {
                    hour = (hour + 1) % 12;
                    timeTablePm[i + 1] = hour + ":00 pm";
                } else {
                    timeTablePm[i + 1] = hour + ":30 pm";
                }
            }
        }
        return timeTablePm;
    }

    public String[] startTime;
    public String[] endTime ;


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
        scheduleRef = FirebaseDatabase.getInstance().getReference().child("employees").child(uid).child("schedule");

        spinner1 = findViewById(R.id.spinnerAmMonday);
        spinner2 = findViewById(R.id.spinnerPmMonday);
        spinner3 = findViewById(R.id.spinnerAmTuesday);
        spinner4 = findViewById(R.id.spinnerPmTuesday);
        spinner5 = findViewById(R.id.spinnerAmWednesday);
        spinner6 = findViewById(R.id.spinnerPmWednesday);
        spinner7 = findViewById(R.id.spinnerAmThrusday);
        spinner8 = findViewById(R.id.spinnerPmThrusday);
        spinner9 = findViewById(R.id.spinnerAmFriday);
        spinner10 = findViewById(R.id.spinnerPmFriday);
        spinner11 = findViewById(R.id.spinnerAmSaturday);
        spinner12 = findViewById(R.id.spinnerPmSaturday);
        spinner13 = findViewById(R.id.spinnerAmSunday);
        spinner14 = findViewById(R.id.spinnerPmSunday);


        ArrayAdapter<String> adapterAM = new ArrayAdapter<String>(EmployeeSetupActivity.this, android.R.layout.simple_list_item_1, timeOptionAm());
        adapterAM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapterPM = new ArrayAdapter<String>(EmployeeSetupActivity.this, android.R.layout.simple_list_item_1, timeOptionPm());
        adapterPM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        String s= scheduleRef.child("1_monday").child("time1").toString();

        spinner1.setAdapter(adapterAM);

        spinner2.setAdapter(adapterPM);
        spinner3.setAdapter(adapterAM);
        spinner4.setAdapter(adapterPM);
        spinner5.setAdapter(adapterAM);
        spinner6.setAdapter(adapterPM);
        spinner7.setAdapter(adapterAM);
        spinner8.setAdapter(adapterPM);
        spinner9.setAdapter(adapterAM);
        spinner10.setAdapter(adapterPM);
        spinner11.setAdapter(adapterAM);
        spinner12.setAdapter(adapterPM);
        spinner13.setAdapter(adapterAM);
        spinner14.setAdapter(adapterPM);

    }


    @Override
    protected void onStart() {
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
                if (insurance[0] == '1') {
                    in1.setChecked(true);
                }
                if (insurance[1] == '1') {
                    in2.setChecked(true);
                }
                if (insurance[2] == '1') {
                    in3.setChecked(true);
                }
                if (payment[0] == '1') {
                    p1.setChecked(true);
                }
                if (payment[1] == '1') {
                    p2.setChecked(true);
                }
                if (payment[2] == '1') {
                    p3.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        DatabaseReference mondayRef = scheduleRef.child("1_monday");

        mondayRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String s1= dataSnapshot.child("time1").getValue(String.class);
                String s2= dataSnapshot.child("time2").getValue(String.class);
                int p1= getSpinnerIndex(s1,spinner1);
                int p2=getSpinnerIndex(s2,spinner2);
                spinner1.setSelection(p1);
                spinner2.setSelection(p2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference tuesdayRef = scheduleRef.child("2_tuesday");
        tuesdayRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String s3= dataSnapshot.child("time1").getValue(String.class);
                String s4= dataSnapshot.child("time2").getValue(String.class);
                int p3= getSpinnerIndex(s3,spinner3);
                int p4=getSpinnerIndex(s4,spinner4);
                spinner3.setSelection(p3);
                spinner4.setSelection(p4);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference wednesdayRef = scheduleRef.child("3_wednesday");
        wednesdayRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String s5= dataSnapshot.child("time1").getValue(String.class);
                String s6= dataSnapshot.child("time2").getValue(String.class);
                int p5= getSpinnerIndex(s5,spinner5);
                int p6=getSpinnerIndex(s6,spinner6);
                spinner5.setSelection(p5);
                spinner6.setSelection(p6);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference thursdayRef = scheduleRef.child("4_thursday");
        thursdayRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String s7= dataSnapshot.child("time1").getValue(String.class);
                String s8= dataSnapshot.child("time2").getValue(String.class);
                int p7= getSpinnerIndex(s7,spinner7);
                int p8=getSpinnerIndex(s8,spinner8);
                spinner7.setSelection(p7);
                spinner8.setSelection(p8);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference fridayRef = scheduleRef.child("5_friday");
        fridayRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String s9= dataSnapshot.child("time1").getValue(String.class);
                String s10= dataSnapshot.child("time2").getValue(String.class);
                int p9= getSpinnerIndex(s9,spinner9);
                int p10=getSpinnerIndex(s10,spinner10);
                spinner9.setSelection(p9);
                spinner10.setSelection(p10);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference saturdayRef = scheduleRef.child("6_saturday");
        saturdayRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String s11= dataSnapshot.child("time1").getValue(String.class);
                String s12= dataSnapshot.child("time2").getValue(String.class);
                int p11= getSpinnerIndex(s11,spinner11);
                int p12=getSpinnerIndex(s12,spinner12);
                spinner11.setSelection(p11);
                spinner12.setSelection(p12);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference sundayRef = scheduleRef.child("7_sunday");
        sundayRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String s13= dataSnapshot.child("time1").getValue(String.class);
                String s14= dataSnapshot.child("time2").getValue(String.class);
                int p13= getSpinnerIndex(s13,spinner13);
                int p14=getSpinnerIndex(s14,spinner14);
                spinner13.setSelection(p13);
                spinner14.setSelection(p14);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*scheduleRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


   */ }
    public int getSpinnerIndex(String s, Spinner spinner){
        int pos=0;
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(s)){
                pos = i;
            }
        }
        return pos;
    }

    //checks to seee all spinners are selected
    //TODO:public boolean isValidSchedule(){}

    public void setHoursAm() {
        startTime = new String[7];

        startTime[0] = spinner1.getSelectedItem().toString();

        startTime[1] = spinner3.getSelectedItem().toString();
        startTime[2] = spinner5.getSelectedItem().toString();
        startTime[3] = spinner7.getSelectedItem().toString();
        startTime[4] = spinner9.getSelectedItem().toString();
        startTime[5] = spinner11.getSelectedItem().toString();
        startTime[6] = spinner13.getSelectedItem().toString();

    }

    public void setHoursPm() {
        endTime = new String[7];

        endTime[0] = spinner2.getSelectedItem().toString();
        endTime[1] = spinner4.getSelectedItem().toString();
        endTime[2] = spinner6.getSelectedItem().toString();
        endTime[3] = spinner8.getSelectedItem().toString();
        endTime[4] = spinner10.getSelectedItem().toString();
        endTime[5] = spinner12.getSelectedItem().toString();
        endTime[6] = spinner14.getSelectedItem().toString();

    }


    void setHours(String daysClosed) {
        for(int i=0;i<7;i++){
           //if both empty toss closed into database
            //TODO: set to close if both blank
            setHoursAm();
            setHoursPm();
            dayHours = new DailySchedule(startTime[i], endTime[i]);
            scheduleRef.child(dayOfWeek[i]).setValue(dayHours);
        }
    }


    public void setButtonClicked(View view) {
        //if hours validate the set hours
        setHours(" ");
        if (in1.isChecked()) {
            insurance[0] = '1';
        } else {
            insurance[0] = '0';
        }
        if (in2.isChecked()) {
            insurance[1] = '1';
        } else {
            insurance[1] = '0';
        }
        if (in3.isChecked()) {
            insurance[2] = '1';
        } else {
            insurance[2] = '0';
        }

        if (p1.isChecked()) {
            payment[0] = '1';
        } else {
            payment[0] = '0';
        }
        if (p2.isChecked()) {
            payment[1] = '1';
        } else {
            payment[1] = '0';
        }
        if (p3.isChecked()) {
            payment[2] = '1';
        } else {
            payment[2] = '0';
        }
        boolean valid = true;
        if (addressView.getText().toString().trim().length() <= 3) {
            valid = false;
            Toast.makeText(EmployeeSetupActivity.this, "Invalid Address, Try Again", Toast.LENGTH_LONG).show();
        } else {
            user.setAddress(addressView.getText().toString().trim());
        }

        if (phoneView.getText().toString().trim().length() != 10) {
            valid = false;
            Toast.makeText(EmployeeSetupActivity.this, "Invalid Phone Number, Try Again", Toast.LENGTH_LONG).show();
        } else {
            user.setPhoneNumber(phoneView.getText().toString().trim());
        }

        if (titleView.getText().toString().trim().length() <= 3) {
            valid = false;
            Toast.makeText(EmployeeSetupActivity.this, "Invalid Clinic Name, Try Again", Toast.LENGTH_LONG).show();
        } else {
            user.setTitle((titleView.getText().toString().trim()));
        }

        if (new String(payment).equals("000")) {
            valid = false;
            Toast.makeText(EmployeeSetupActivity.this, "Select At Least 1 Payment Method", Toast.LENGTH_LONG).show();
        } else {
            user.setPaymentTypes(new String(payment));
        }

        if (new String(insurance).equals("000")) {
            valid = false;
            Toast.makeText(EmployeeSetupActivity.this, "Select At Least 1 Insurance Type", Toast.LENGTH_LONG).show();
        } else {
            user.setInsuranceTypes(new String(insurance));
        }

        // validate the 7 pairs of spinners
        if (! validSpinnerPair(spinner1, spinner2)) {
            valid = false;
            Toast.makeText(EmployeeSetupActivity.this, "Please enter a valid schedule for Monday", Toast.LENGTH_LONG).show();
        }
        else if (! validSpinnerPair(spinner3, spinner4)) {
            valid = false;
            Toast.makeText(EmployeeSetupActivity.this, "Please enter a valid schedule for Tuesday", Toast.LENGTH_LONG).show();
        }
        else if (! validSpinnerPair(spinner5, spinner6)) {
            valid = false;
            Toast.makeText(EmployeeSetupActivity.this, "Please enter a valid schedule for Wednesday", Toast.LENGTH_LONG).show();
        }
        else if (! validSpinnerPair(spinner7, spinner8)) {
            valid = false;
            Toast.makeText(EmployeeSetupActivity.this, "Please enter a valid schedule for Thursday", Toast.LENGTH_LONG).show();
        }
        else if (! validSpinnerPair(spinner9, spinner10)) {
            valid = false;
            Toast.makeText(EmployeeSetupActivity.this, "Please enter a valid schedule for Friday", Toast.LENGTH_LONG).show();
        }
        else if (! validSpinnerPair(spinner11, spinner12)) {
            valid = false;
            Toast.makeText(EmployeeSetupActivity.this, "Please enter a valid schedule for Saturday", Toast.LENGTH_LONG).show();
        }
        else if (! validSpinnerPair(spinner13, spinner14)) {
            valid = false;
            Toast.makeText(EmployeeSetupActivity.this, "Please enter a valid schedule for Sunday", Toast.LENGTH_LONG).show();
        }


        if (valid == true) {
            ref.child("address").setValue(user.getAddress());
            ref.child("phoneNumber").setValue(user.getPhoneNumber());
            ref.child("title").setValue(user.getTitle());
            ref.child("paymentTypes").setValue(user.getPaymentTypes());
            ref.child("insuranceTypes").setValue(user.getInsuranceTypes());
            Toast.makeText(EmployeeSetupActivity.this, "Profile Information Set", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), EmployeeMainActivity.class);   //Application Context and Activity
            intent.putExtra("USER_DATA", user);
            startActivity(intent);//, ProfileActivity.REQUEST_NEW_TEAM);
            finish();
        }
    }

    // returns true if one spinner is empty (index = 0), but not both (xor)
    private boolean validSpinnerPair(Spinner s1, Spinner s2) {
        if (s1.getSelectedItemPosition() == 0 && s2.getSelectedItemPosition() != 0)
            return false;
        if (s2.getSelectedItemPosition() == 0 && s1.getSelectedItemPosition() != 0)
            return false;
        return true;
    }

    @Override
    public void onBackPressed() {
        Button btn = (Button) findViewById(R.id.setButton);
        btn.performClick();
    }


}

package com.example.walkinclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.walkinclinic.account.Employee;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SearchWalkin extends AppCompatActivity {

    ListView clinicListV;
    DatabaseReference clinicRef;
    int section;
    String filter;
    List<Employee> employees;
    DatabaseReference rateRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_walkin);

        Intent intent = getIntent();

        filter = intent.getStringExtra("FILTER");
        section = intent.getIntExtra("SECTION", 0);

        clinicListV = findViewById(R.id.clinicList);
        clinicRef = FirebaseDatabase.getInstance().getReference("employees");
        employees = new ArrayList<>();

        clinicListV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Employee employee = employees.get(i);
                //TODO:rating pop up method
                rateClinic(employee.getTitle(), employee.getID());
                // DatabaseReference rateRef= FirebaseDatabase.getInstance().getReference("employees").child(employee.getID()).child("rating");
                //employee.setRating(rateC);
                //rateRef.setValue(employee.getRating());
                return true;
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        clinicRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                employees.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    String email = postSnapshot.child("email").getValue(String.class);
                    String password = postSnapshot.child("password").getValue(String.class);
                    String firstName = postSnapshot.child("nameFirst").getValue(String.class);
                    String lastName = postSnapshot.child("nameLast").getValue(String.class);
                    String id = postSnapshot.getKey();

                    String title = postSnapshot.child("title").getValue(String.class);
                    String address = postSnapshot.child("address").getValue(String.class);
                    String phoneNum = postSnapshot.child("phoneNumber").getValue(String.class);
                    DatabaseReference services = postSnapshot.child("services").getRef();


                    Date now = new Date();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(now);
                    int day = (calendar.get(Calendar.DAY_OF_WEEK));
                    String time1 = postSnapshot.child("schedule").child(refDay(day)).child("time1").getValue(String.class);
                    String time2 = postSnapshot.child("schedule").child(refDay(day)).child("time2").getValue(String.class);


                    int i = 1;

                    switch (i) {
                        case 1:
                            if (address != null) {
                                if (address.compareTo(filter) == 0) {
                                    Employee employee = new Employee(email, password, firstName, lastName, id);
                                    employee.setTitle(title);
                                    employee.setAddress(address);
                                    employee.setSchedule(time1, time2);
                                    employee.setPhoneNumber(phoneNum);
                                    employees.add(employee);
                                }
                            } else {
                                break;
                            }
                        case 2:
                            if (title != null) {
                                if (title.compareTo(filter) == 0) {
                                    Employee employee = new Employee(email, password, firstName, lastName, id);
                                    employee.setTitle(title);
                                    employee.setAddress(address);
                                    employee.setSchedule(time1, time2);
                                    employee.setPhoneNumber(phoneNum);
                                    employees.add(employee);
                                }
                            } else {
                                break;
                            }
                        case 3:

                    }
                }
                ClinicList employeesAdapter = new ClinicList(SearchWalkin.this, employees);
                clinicListV.setAdapter(employeesAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public String refDay(int dayOfweek) {
        String[] ref = {"7_sunday", "1_monday", "2_tuesday", "3_wednesday", "4_thursday", "5_friday", "6_staurday"};
        return ref[dayOfweek - 1];
    }

    private void rateClinic(String clinicName, final String id) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_rate_clinic, null);
        dialogBuilder.setView(dialogView);

        final Button submitBtn = (Button) dialogView.findViewById(R.id.submitBtn);
        final EditText commentSection = (EditText) dialogView.findViewById(R.id.commentSectionTV);
        final RatingBar rateBar = (RatingBar) dialogView.findViewById(R.id.userRating);
        //float addRate= Float.valueOf(rateBar.getNumStars());

        dialogBuilder.setTitle(clinicName);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SearchWalkin.this, "Thank you for your input", Toast.LENGTH_LONG).show();
                b.dismiss();
            }
        });
    }

    public void clinicVClick(View view){
        Intent intent = new Intent(getApplicationContext(), PatientMainActivity.class);
        startActivity(intent);
    }
    //TODO: search by service and hours
    //TODO: rate method
}

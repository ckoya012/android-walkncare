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
import com.example.walkinclinic.account.Patient;
import com.example.walkinclinic.account.Service;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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

    private ListView clinicListV;
    private DatabaseReference clinicRef;
    private int section;
    private String filter;
    private List<Employee> employees;
    private DatabaseReference rateRef;
    private DatabaseReference refComment;
    private final String CLOSED = "  ";


    private Patient user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_walkin);


        Intent intent = getIntent();

        filter = intent.getStringExtra("FILTER");
        section = intent.getIntExtra("SECTION", 0);
        user = (Patient) getIntent().getSerializableExtra("USER_DATA");

        clinicListV = findViewById(R.id.clinicList);
        clinicRef = FirebaseDatabase.getInstance().getReference("employees");
        refComment = FirebaseDatabase.getInstance().getReference("employees");
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

        clinicListV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // get the clinic that was clicked
                Employee clinic = employees.get(i);
                Intent intent = new Intent(getApplicationContext(), PatientBookAppointmentActivity.class);
                intent.putExtra("USER_DATA", user);
                intent.putExtra("CLINIC_DATA", clinic);
                startActivity(intent);
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


                    Date now = new Date();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(now);
                    int day = (calendar.get(Calendar.DAY_OF_WEEK));
                    String time1 = postSnapshot.child("schedule").child(refDay(day)).child("time1").getValue(String.class);
                    String time2 = postSnapshot.child("schedule").child(refDay(day)).child("time2").getValue(String.class);


                    DataSnapshot services = postSnapshot.child("services");
                    for (DataSnapshot serviceSnapshot : services.getChildren()) {
                        for (DataSnapshot specific : serviceSnapshot.getChildren()) {
                            String service = specific.child("service").getValue(String.class);
                            if (service.compareTo(filter) == 0) {
                                Employee employee = new Employee(email, password, firstName, lastName, id);
                                employee.setTitle(title);
                                employee.setAddress(address);
                                employee.setSchedule(time1, time2);
                                employee.setPhoneNumber(phoneNum);
                                employees.add(employee);
                                break;
                            }

                        }

                    }

                    if (address != null) {
                        if (address.compareTo(filter) == 0) {
                            Employee employee = new Employee(email, password, firstName, lastName, id);
                            employee.setTitle(title);
                            employee.setAddress(address);
                            employee.setSchedule(time1, time2);
                            employee.setPhoneNumber(phoneNum);
                            employees.add(employee);
                        }
                    }
                    if (title != null) {
                        if (title.compareTo(filter) == 0) {
                            Employee employee = new Employee(email, password, firstName, lastName, id);
                            employee.setTitle(title);
                            employee.setAddress(address);
                            employee.setSchedule(time1, time2);
                            employee.setPhoneNumber(phoneNum);
                            employees.add(employee);
                        }
                    }
 /*                   if (filter.length() > 2) {
                        if (filter.length() == 8) {

                            String numRange = filter.substring(0, 2);
                            String amOrPM = filter.substring(filter.length() - 2);

                            if (time1.length() == 8) {
                                String time1S = time1.substring(0, 2);
                                String time1E = time1.substring(filter.length() - 2);
                                if (time2.length() == 8) {
                                    String time2S = time2.substring(0, 2);
                                    String time2E = time2.substring(time2.length() - 2);

                                    int compare = Integer.parseInt(numRange);
                                    if (time1.compareTo(CLOSED) != 0) {
                                        int mustStartBefore = Integer.parseInt(time1S);
                                        int mustEndAfter = Integer.parseInt(time2S);


                                        if (amOrPM.equals("am")) {
                                            if (mustStartBefore <= compare) {
                                                Employee employee = new Employee(email, password, firstName, lastName, id);
                                                employee.setTitle(title);
                                                employee.setAddress(address);
                                                employee.setSchedule(time1, time2);
                                                employee.setPhoneNumber(phoneNum);
                                                employees.add(employee);
                                            }
                                        } else {
                                            if (compare <= mustEndAfter) {
                                                Employee employee = new Employee(email, password, firstName, lastName, id);
                                                employee.setTitle(title);
                                                employee.setAddress(address);
                                                employee.setSchedule(time1, time2);
                                                employee.setPhoneNumber(phoneNum);
                                                employees.add(employee);
                                            }
                                        }

                                    }


                                } else {
                                    String time2S = time2.substring(0, 1);
                                    String time2E = time2.substring(time2.length() - 1);
                                    int compare = Integer.parseInt(numRange);
                                    if (time1.compareTo(CLOSED) != 0) {
                                        int mustStartBefore = Integer.parseInt(time1S);
                                        int mustEndAfter = Integer.parseInt(time2S);


                                        if (amOrPM.equals("am")) {
                                            if (mustStartBefore <= compare) {
                                                Employee employee = new Employee(email, password, firstName, lastName, id);
                                                employee.setTitle(title);
                                                employee.setAddress(address);
                                                employee.setSchedule(time1, time2);
                                                employee.setPhoneNumber(phoneNum);
                                                employees.add(employee);
                                            }
                                        } else {
                                            if (compare <= mustEndAfter) {
                                                Employee employee = new Employee(email, password, firstName, lastName, id);
                                                employee.setTitle(title);
                                                employee.setAddress(address);
                                                employee.setSchedule(time1, time2);
                                                employee.setPhoneNumber(phoneNum);
                                                employees.add(employee);
                                            }
                                        }

                                    }

                                }


                            } else {
                                String time1S = time1.substring(0, 1);
                                String time1E = time1.substring(filter.length() - 2);
                                if (time2.length() == 8) {
                                    String time2S = time2.substring(0, 2);


                                } else {
                                    String time2S = time2.substring(0, 2);
                                    String time2E = time2.substring(time2.length() - 2);

                                }
                            }




                        } else {
                            String numRange = filter.substring(0, 1);
                            String amOrPM = filter.substring(filter.length() - 2);
                            String time1S = time1.substring(0, 1);
                            String time2E = time2.substring(time2.length() - 2);
                            int compare = Integer.parseInt(numRange);

                            if (time1.compareTo(CLOSED) != 0) {
                                if (time1S.substring(1).compareTo(":") != 0) {
                                    int mustStartBefore = Integer.parseInt(time1S);
                                    int mustEndAfter = Integer.parseInt(time2E);


                                    if (amOrPM.equals("am")) {
                                        if (mustStartBefore <= compare) {
                                            Employee employee = new Employee(email, password, firstName, lastName, id);
                                            employee.setTitle(title);
                                            employee.setAddress(address);
                                            employee.setSchedule(time1, time2);
                                            employee.setPhoneNumber(phoneNum);
                                            employees.add(employee);
                                        }
                                    } else {
                                        if (compare <= mustEndAfter) {
                                            Employee employee = new Employee(email, password, firstName, lastName, id);
                                            employee.setTitle(title);
                                            employee.setAddress(address);
                                            employee.setSchedule(time1, time2);
                                            employee.setPhoneNumber(phoneNum);
                                            employees.add(employee);
                                        }
                                    }
                                }

                            }
                        }
                    }
/*
                    DatabaseReference sAdministrative = services.child("administrative");
                    for(String admin: sAdministrative.get)
                    DatabaseReference sGp = services.child("gp");
                    sGp.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Service service = postSnapshot.getValue(Service.class);
                                // add service to list
                                if (service.getService().equals(filter)) {
                                    Employee employee = new Employee(email, password, firstName, lastName, id);
                                    employee.setTitle(title);
                                    employee.setAddress(address);
                                    employee.setSchedule(time1, time2);
                                    employee.setPhoneNumber(phoneNum);
                                    employees.add(employee);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    DatabaseReference sInjection = services.child("injection");
                    sInjection.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Service service = postSnapshot.getValue(Service.class);
                                if (service.getService().equals(filter)) {
                                    Employee employee = new Employee(email, password, firstName, lastName, id);
                                    employee.setTitle(title);
                                    employee.setAddress(address);
                                    employee.setSchedule(time1, time2);
                                    employee.setPhoneNumber(phoneNum);
                                    employees.add(employee);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    DatabaseReference sTest = services.child("test");
                    sTest.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Service service = postSnapshot.getValue(Service.class);
                                if (service.getService().equals(filter)) {
                                    Employee employee = new Employee(email, password, firstName, lastName, id);
                                    employee.setTitle(title);
                                    employee.setAddress(address);
                                    employee.setSchedule(time1, time2);
                                    employee.setPhoneNumber(phoneNum);
                                    employees.add(employee);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    */


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
        String[] ref = {"7_sunday", "1_monday", "2_tuesday", "3_wednesday", "4_thursday", "5_friday", "6_saturday"};
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

        final DatabaseReference commentR = refComment.child(id).child("comments");
        String input = commentSection.getText().toString().trim();
        commentR.setValue(input);

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

    //TODO: search by service and hours
    //TODO: rate method
}

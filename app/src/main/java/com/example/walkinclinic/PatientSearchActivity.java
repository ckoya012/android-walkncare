package com.example.walkinclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.walkinclinic.account.Employee;
import com.example.walkinclinic.account.Patient;
import com.example.walkinclinic.account.Service;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PatientSearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner searchSpinner;
    String[] searchTypes = {"Address", "Services", "Name", "Time"};
    //String[] hoursList = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    ImageView btnSearch;
    String validMatch;
    int searchSection;


    TextView searchByTV;
    AutoCompleteTextView searchBar;
    DatabaseReference ref;
    DatabaseReference services;

    List<String> adresses;
    List<String> clinicNames;
    List<Service> allServices;

    private Patient user;

    List<Employee> employeeList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = (Patient) getIntent().getSerializableExtra("USER_DATA");
        String userFirstName = user.getNameFirst();

        setContentView(R.layout.activity_patient_search);
        TextView message = (TextView) findViewById(R.id.welcomeMsgPat);
        message.setText("Welcome " + userFirstName + "! You are logged in as a patient.");

        searchByTV = findViewById(R.id.searchByTV);
        searchSpinner = findViewById(R.id.searchType);
        btnSearch = findViewById(R.id.searchIV);
        searchBar = findViewById(R.id.searchAutoComView);

        searchSpinner.setOnItemSelectedListener(this);


        ref = FirebaseDatabase.getInstance().getReference().child("employees");
        services = FirebaseDatabase.getInstance().getReference("services");
        employeeList = new ArrayList<>();
        adresses = new ArrayList<>();
        clinicNames = new ArrayList<>();
        allServices = new ArrayList<>();


        ArrayAdapter<String> searchFilter = new ArrayAdapter<String>(PatientSearchActivity.this, android.R.layout.simple_list_item_1, searchTypes);
        searchFilter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchSpinner.setAdapter(searchFilter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        ref.addValueEventListener((new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adresses.clear();
                clinicNames.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String address = (String) postSnapshot.child("address").getValue(String.class);
                    String name = (String) postSnapshot.child("title").getValue(String.class);
                    adresses.add(address);
                    clinicNames.add(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }));

        DatabaseReference sAdministrative = services.child("administrative");
        sAdministrative.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Service service = postSnapshot.getValue(Service.class);
                    // add service to list
                    allServices.add(service);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference sGp = services.child("gp");
        sGp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Service service = postSnapshot.getValue(Service.class);
                    // add service to list
                    allServices.add(service);
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
                    // add service to list
                    allServices.add(service);
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
                    // add service to list
                    allServices.add(service);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBar.showDropDown();
            }
        });

    }

    public void onSearchClicked(View view) {
        // String searchItem = searchBar.getText().toString().trim();
        if (validMatch != null) {
            Intent intent = new Intent(this, SearchWalkin.class);
            intent.putExtra("FILTER", validMatch);
            intent.putExtra("SECTION", searchSection);
            intent.putExtra("USER_DATA", user);
            startActivity(intent);
            finish();

        } else {
            Toast.makeText(this, "Sorry we were unable to find a clinic that mathed description", Toast.LENGTH_LONG).show();
        }
    }

    public List<String> serviceName(List<Service> services) {
        List<String> sNames = new ArrayList<>();
        for (Service service : services) {
            String sName = service.getService();
            sNames.add(sName);
        }
        return sNames;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String sp1 = String.valueOf(searchSpinner.getSelectedItem());
        Toast.makeText(this, sp1, Toast.LENGTH_SHORT).show();
        if (sp1.contentEquals(searchTypes[0])) {
            searchSection = 1;
            final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, adresses);
            searchBar.setAdapter(adapter1);
            searchBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    validMatch = adapter1.getItem(position).toString(); //INDEXOUTOFBOUNDSEXCEPTION
                }
            });
            searchBar.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String cSearchM = searchBar.getText().toString();
                    for (int i = 0; i < adapter1.getCount(); i++) {
                        String tmp = adapter1.getItem(i).toString();
                        if (cSearchM.compareTo(tmp) == 0) {
                            validMatch = cSearchM;
                            return;
                        } else {
                            validMatch = null;
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        } else if (sp1.contentEquals(searchTypes[1])) {//TODO: force selection for all list
            searchSection = 2;
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, serviceName(allServices));//add array
            searchBar.setAdapter(adapter2);
        } else if (sp1.contentEquals(searchTypes[2])) {
            searchSection = 3;
            ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, clinicNames);//add array
            searchBar.setAdapter(adapter3);
        } else {
            searchSection = 4;
            ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, allHours());//add array
            searchBar.setAdapter(adapter4);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public List<String> allHours() {
        List<String> hours = new ArrayList<>();
        int hour = 12;
        for (int i = 0; i < 24; i++) {
            if (i == 0) {
                hours.add("12:00 am");
            } else {
                if (i % 2 == 0) {
                    hour = (hour + 1) % 12;
                    hours.add(hour + ":00 am");
                } else {
                    hours.add(hour + ":30 am");
                }
            }
        }
        return hours;

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void onClickSignOut(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

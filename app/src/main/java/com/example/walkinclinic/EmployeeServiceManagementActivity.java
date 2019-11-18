package com.example.walkinclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.walkinclinic.account.Service;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EmployeeServiceManagementActivity extends AppCompatActivity {

    private String typeOfService;
    private TextView typeOfServiceLabel;
    private ListView listViewServices;

    private DatabaseReference db;
    List<Service> availableServices; // services made available by the admin


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_service_management);

        // get service type
        typeOfService = (String) getIntent().getExtras().get("TYPE_OF_SERVICE");
        typeOfService = typeOfService.toLowerCase();

        // get + set elements
        typeOfServiceLabel = findViewById(R.id.serviceTypeLabel);
        listViewServices = findViewById(R.id.listViewServices);
        listViewServices.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        typeOfServiceLabel.setText("Add/remove " + typeOfService + " services offered by your clinic");

        // set the DB ref and create array to store services fetched from db
        db = FirebaseDatabase.getInstance().getReference("services").child(typeOfService);
        availableServices = new ArrayList<>();

        // event listener for checkable list items
        // TODO:
        listViewServices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Service service = availableServices.get(i);
                Toast.makeText(getApplicationContext(), "Service: " + service.getService(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // clear previous services
                availableServices.clear();

                // iterate through all nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // get service
                    Service service = postSnapshot.getValue(Service.class);
                    // add service to list
                    availableServices.add(service);
                }
                // create adapter
                ServiceListCheckable servicesAdapter = new ServiceListCheckable(EmployeeServiceManagementActivity.this, availableServices);
                // attach adapter to ListView
                listViewServices.setAdapter(servicesAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

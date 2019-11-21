package com.example.walkinclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.walkinclinic.account.Employee;
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

    private Employee user;
    private String uid;

    private DatabaseReference refAvailableServices;
    private DatabaseReference refAssociatedServices;
    List<Service> availableServices; // services that were made available by the admin
    List<String> associatedServiceIDs; // services that are associated with the employee / clinic


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

        // get user data
        user = (Employee) getIntent().getSerializableExtra("USER_DATA");
        uid = user.getID();

        // set the database refs
        refAvailableServices = FirebaseDatabase.getInstance().getReference("services").child(typeOfService);
        refAssociatedServices = FirebaseDatabase.getInstance().getReference().child("employees").child(uid).child("services").child(typeOfService);

        // create the lists to store services offerred by the admin & services that the employee/clinic are currently associated to
        availableServices = new ArrayList<>();
        associatedServiceIDs = new ArrayList<>();

        // event listener for checkable list items
        // TODO:
        listViewServices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Service service = availableServices.get(i);
                String serviceID = service.getId();
                String serviceName = service.getService();

                View listViewItem = getViewByPosition(i, listViewServices);
                final CheckedTextView checkBox = listViewItem.findViewById(R.id.checkBox);
                final boolean isChecked = checkBox.isChecked();

                // whether or not the service is associated can be determined if the checkbox was checked
                if (isChecked) {
                    // remove the serviceID
                    checkBox.setChecked(false);
                    associatedServiceIDs.remove(serviceID);
                    refAssociatedServices.child(serviceID).removeValue();
                    Toast.makeText(getApplicationContext(), "Service removed: " + serviceName, Toast.LENGTH_SHORT).show();
                }
                else {
                    // add the serviceID
                    checkBox.setChecked(true);
                    // remove this if statement once setChecked works inside of onStart
                    if (associatedServiceIDs.contains(serviceID)) {
                        Toast.makeText(getApplicationContext(), "You already offer this service.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        associatedServiceIDs.add(serviceID);
                        refAssociatedServices.child(serviceID).setValue(serviceID);
                        Toast.makeText(getApplicationContext(), "Service added: " + serviceName, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // get list of service IDs offered by clinic
        refAssociatedServices.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // clear previous services
                associatedServiceIDs.clear();

                // iterate through all nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // get service
                    String serviceID = postSnapshot.getValue(String.class);
                    // add service to list
                    associatedServiceIDs.add(serviceID);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        // get list of services offered and display them in a list view
        refAvailableServices.addValueEventListener(new ValueEventListener() {
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

                for (int i = 0; i < availableServices.size(); i ++) {
                    Service service = availableServices.get(i);
                    String serviceID = service.getId();

                    if (associatedServiceIDs.contains(serviceID)) {
                        // check the checkbox
                        View listViewItem = getViewByPosition(i, listViewServices);
                        CheckedTextView checkBox = listViewItem.findViewById(R.id.checkBox);
                        // TODO: find out why the following line doesnt work
                        checkBox.setChecked(true);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        }
        else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}

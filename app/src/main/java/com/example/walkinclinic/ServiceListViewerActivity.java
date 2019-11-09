package com.example.walkinclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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

public class ServiceListViewerActivity extends AppCompatActivity {

    TextView serviceTypeLabel;
    EditText editTextName;
    //EditText editTextPrice;
    EditText editTextRole;
    Button buttonAddService;
    ListView listViewServices;

    List<Service> services;

    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list_viewer);

        editTextName = findViewById(R.id.editTextName);
        //editTextPrice = findViewById(R.id.editTextPrice);
        editTextRole = findViewById(R.id.editTextRole);
        listViewServices = findViewById(R.id.listViewServices);
        buttonAddService = findViewById(R.id.addServiceButton);

        services = new ArrayList<>();

        final String TYPE_OF_SERVICE = (String) getIntent().getExtras().get("TYPE_OF_SERVICE");
        final String DB_REF_CHILD = TYPE_OF_SERVICE.toLowerCase();

        db = FirebaseDatabase.getInstance().getReference("services").child(DB_REF_CHILD);



        // Set the title of the service
        serviceTypeLabel = findViewById(R.id.serviceTypeLabel);
        serviceTypeLabel.setText(TYPE_OF_SERVICE + " Services");

        buttonAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addService();
            }
        });

        // TODO: setOnItemLongClickListener to update and delete service
        listViewServices.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Service service = services.get(i);
                showUpdateDeleteDialog(service.getId(),service.getService(), service.getRole());//
                return true;
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
                services.clear();

                // iterate through all nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // get service
                    Service service = postSnapshot.getValue(Service.class);
                    // add service to list
                    services.add(service);
                }
                // create adapter
                ServiceList servicesAdapter = new ServiceList(ServiceListViewerActivity.this, services);
                // attach adapter to ListView
                listViewServices.setAdapter(servicesAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addService() {
        // get values to save
        String name = editTextName.getText().toString().trim();
        /*double price;
        try {
            price = Double.parseDouble(editTextPrice.getText().toString().trim());
        }
        catch (NumberFormatException e) {
            price = -1;
        }*/
        String role = editTextRole.getText().toString().trim();

        if (fieldsAreValid(name/*, price*/, role, ServiceListViewerActivity.this)) {
            // get UID for the service to be added
            String id = db.push().getKey();
            Service service = new Service(name/*, price*/, role, id);
            db.child(id).setValue(service);

            // clear all fields
            editTextName.setText("");
            //editTextPrice.setText("");
            editTextRole.setText("");

            Toast.makeText(getApplicationContext(), "Service added.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean fieldsAreValid(String name/*, double price*/, String role, Context c) {

        // check service name
        if (name.length() < 1) {
            Toast.makeText(c, "Please enter a service", Toast.LENGTH_LONG).show();
            return false;
        }
        /* check negative price
        if (price < 0) {
            Toast.makeText(c, "Please enter a valid price", Toast.LENGTH_LONG).show();
            return false;
        }*/
        // check role
        if (role.length() < 1) {
            Toast.makeText(c, "Please enter a role", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    // TODO: showUpdateDeleteDialog() method
    private void showUpdateDeleteDialog(final String productId, String productName, String serviceRole) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_delete_service, null);
        dialogBuilder.setView(dialogView);

        final EditText newServiceName= (EditText) dialogView.findViewById(R.id.editTextName2);
        newServiceName.setText(productName);
        final EditText newRoleName= (EditText) dialogView.findViewById(R.id.editTextRole2);
        newRoleName.setText(serviceRole);

        final Button buttonDelete = (Button) dialogView.findViewById(R.id.deleteServiceBtn);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.updateBtn);


        dialogBuilder.setTitle(productName);
        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteService(productId);
                b.dismiss();
            }
        });
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name= newServiceName.getText().toString().trim();
                String role= newRoleName.getText().toString().trim();
                if (!TextUtils.isEmpty(name)&& !TextUtils.isEmpty(role)) {
                    updateService(productId, name, role );
                    b.dismiss();
                }

            }
        });
    }

    // TODO: deleteService() method
    private void deleteService(String sId){
       db.child(sId).removeValue();
        Toast.makeText(this, "Service Deleted", Toast.LENGTH_LONG).show();
    }

    // TODO: updateService() method
    private void updateService(String sId, String name, String role){
        DatabaseReference dR = db.child(sId);
        //updating product
        Service service = new Service(name,/*,0.0*/role,sId);
        dR.setValue(service);
        Toast.makeText(this, "Service Updated", Toast.LENGTH_LONG).show();
    }


}

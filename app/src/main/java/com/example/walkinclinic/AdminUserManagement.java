package com.example.walkinclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.walkinclinic.account.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminUserManagement extends AppCompatActivity {

    ListView employeeList,patientList;

    List<Employee> employees;
    List<Patient> patients;
    DatabaseReference databaseEmployee,databasePatients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_management);

        employeeList = (ListView) findViewById(R.id.employeeList);
        databaseEmployee = FirebaseDatabase.getInstance().getReference("employees");
        employees = new ArrayList<>();

        patientList = (ListView) findViewById(R.id.patientList);
        databasePatients = FirebaseDatabase.getInstance().getReference("patients");
        patients = new ArrayList<>();

        patientList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Patient patient = patients.get(i);
                showUpdateDeleteDialog(patient.getID(), patient.getEmail(),false);
                return true;
            }
        });

        employeeList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Employee employee = employees.get(i);
                showUpdateDeleteDialog(employee.getID(), employee.getEmail(),true);
                return true;
            }
        });
    }

    protected void onStart(){
        super.onStart();
        databaseEmployee.addValueEventListener((new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                employees.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    String email = postSnapshot.child("email").getValue(String.class);
                    String password = postSnapshot.child("password").getValue(String.class);
                    String firstName = postSnapshot.child("nameFirst").getValue(String.class);
                    String lastName = postSnapshot.child("nameLast").getValue(String.class);
                    String id = postSnapshot.getKey();

                    Employee employee = new Employee(email,password,firstName,lastName,id);
                    employees.add(employee);
                }
                EmployeeList employeesAdapter = new EmployeeList(AdminUserManagement.this,employees);
                employeeList.setAdapter(employeesAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }));

        databasePatients.addValueEventListener((new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                patients.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    String email = postSnapshot.child("email").getValue(String.class);
                    String password = postSnapshot.child("password").getValue(String.class);
                    String firstName = postSnapshot.child("nameFirst").getValue(String.class);
                    String lastName = postSnapshot.child("nameLast").getValue(String.class);
                    String id = postSnapshot.getKey();

                    Patient patient = new Patient(email,password,firstName,lastName,id);
                    patients.add(patient);
                }
                PatientList patientAdapter = new PatientList(AdminUserManagement.this,patients);
                patientList.setAdapter(patientAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }));

    }

    private void showUpdateDeleteDialog(final String productId, String productName, final boolean isEmployee) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_delete_dialog, null);
        dialogBuilder.setView(dialogView);

        final Button buttonDelete = (Button) dialogView.findViewById(R.id.deleteUserBtn);

        dialogBuilder.setTitle(productName);
        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUser(productId,isEmployee);
                b.dismiss();
            }
        });
    }

    private void deleteUser(String id, boolean isEmployee){
        DatabaseReference dR;
        if( isEmployee){
            dR = FirebaseDatabase.getInstance().getReference("employees").child(id);
        } else {
            dR = FirebaseDatabase.getInstance().getReference("patients").child(id);
        }
        dR.removeValue();
        Toast.makeText(this, "User Deleted", Toast.LENGTH_LONG).show();
    }
}

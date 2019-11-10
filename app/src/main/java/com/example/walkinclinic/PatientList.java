package com.example.walkinclinic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.walkinclinic.account.*;

import java.util.List;

public class PatientList extends ArrayAdapter<Patient> {

    private Activity context;
    List<Patient> patients;

    public PatientList(){
        super(new Activity(), R.layout.activity_patient_list);
    }

    public PatientList(Activity context, List<Patient> patients) {
        super(context, R.layout.activity_patient_list, patients);
        this.context = context;
        this.patients = patients;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_patient_list, null, true);

        TextView textViewEmail = (TextView) listViewItem.findViewById(R.id.textViewEmail);
        TextView textViewFirst = (TextView) listViewItem.findViewById(R.id.textViewFirst);
        TextView textViewLast = (TextView) listViewItem.findViewById(R.id.textViewLast);

        Patient patient = patients.get(position);
        textViewEmail.setText(patient.getEmail());
        textViewFirst.setText("First Name: "+patient.getNameFirst());
        textViewLast.setText("Last Name: "+patient.getNameLast());
        return listViewItem;
    }
}

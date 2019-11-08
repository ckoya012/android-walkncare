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

public class EmployeeList extends ArrayAdapter<Employee> {

    private Activity context;
    List<Employee> employees;

    public EmployeeList(){
       super(new Activity(), R.layout.activity_employee_list);
    }

    public EmployeeList(Activity context, List<Employee> employees) {
        super(context, R.layout.activity_employee_list, employees);
        this.context = context;
        this.employees = employees;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_employee_list, null, true);

        TextView textViewEmail = (TextView) listViewItem.findViewById(R.id.textViewEmail);
        TextView textViewFirst = (TextView) listViewItem.findViewById(R.id.textViewFirst);
        TextView textViewLast = (TextView) listViewItem.findViewById(R.id.textViewLast);

        Employee employee = employees.get(position);
        textViewEmail.setText(employee.getEmail());
        textViewFirst.setText("First Name: "+employee.getNameFirst());
        textViewLast.setText("Last Name: "+employee.getNameLast());
        return listViewItem;
    }
}

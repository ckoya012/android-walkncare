package com.example.walkinclinic;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.walkinclinic.account.Employee;

import java.util.List;

public class ClinicList extends ArrayAdapter<Employee> {
    private Activity context;
    List<Employee> employeeList;

    public ClinicList() {
        super(new Activity(), R.layout.activity_clinic_list);
    }

    public ClinicList(Activity context, List<Employee> employeeList) {
        super(context, R.layout.activity_clinic_list, employeeList);
        this.context = context;
        this.employeeList = employeeList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_clinic_list, null, true);

        TextView title = (TextView) listViewItem.findViewById(R.id.clinicNameTV);
        TextView address= (TextView) listViewItem.findViewById(R.id.clinicAddressTV);
        TextView phone = (TextView) listViewItem.findViewById(R.id.clinicPhone);
        TextView schedule = (TextView) listViewItem.findViewById(R.id.clinicSchedule);
        //RatingBar rating = (RatingBar) listViewItem.findViewById(R.id.clinicRating);


        Employee employee = employeeList.get(position);
        title.setText(String.valueOf(employee.getTitle()));
        address.setText(String.valueOf(employee.getAddress()));
        phone.setText(String.valueOf(employee.getPhoneNumber()));
        schedule.setText(String.valueOf(employee.getSchedule()));
        //rating.setRating(Float.valueOf(employee.getRating()));

        return listViewItem;
    }
}

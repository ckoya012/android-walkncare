package com.example.walkinclinic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.example.walkinclinic.account.Service;

import java.util.List;

public class ServiceListCheckable extends ArrayAdapter<Service> {

    private Activity context;
    List<Service> services;

    public ServiceListCheckable(){
        super(new Activity(), R.layout.activity_service_list_checkable);
    }

    public ServiceListCheckable(Activity context, List<Service> services) {
        super(context, R.layout.activity_service_list_checkable, services);
        this.context = context;
        this.services = services;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_service_list_checkable, null, true);

        TextView textViewService = listViewItem.findViewById(R.id.textViewService);
        TextView textViewRole = listViewItem.findViewById(R.id.textViewRole);

        listViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxOnClick(v);
            }
        });

        Service service = services.get(position);
        textViewService.setText(service.getService());
        textViewRole.setText("Administrator role: "+service.getRole());
        return listViewItem;
    }

    public void checkBoxOnClick(View listViewItem) {
        final CheckedTextView checkBox = listViewItem.findViewById(R.id.checkBox);
        final boolean isChecked = checkBox.isChecked();
        if (isChecked) {
            checkBox.setChecked(false);
        }
        else {
            checkBox.setChecked(true);
        }
    }
}

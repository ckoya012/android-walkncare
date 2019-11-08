package com.example.walkinclinic;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.walkinclinic.account.Patient;

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


public class ServiceList extends ArrayAdapter<Service> {

    private Activity context;
    List<Service> services;

    public ServiceList(){
        super(new Activity(), R.layout.activity_service_list);
    }

    public ServiceList(Activity context, List<Service> services) {
        super(context, R.layout.activity_service_list, services);
        this.context = context;
        this.services = services;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_service_list, null, true);

        TextView textViewService = (TextView) listViewItem.findViewById(R.id.textViewService);
        TextView textViewPrice = (TextView) listViewItem.findViewById(R.id.textViewPrice);
        TextView textViewRole = (TextView) listViewItem.findViewById(R.id.textViewRole);

        Service service = services.get(position);
        textViewService.setText(service.getService());
        textViewPrice.setText("Price of service: "+service.getPrice());
        textViewRole.setText("Administrator role: "+service.getRole());
        return listViewItem;
    }
}

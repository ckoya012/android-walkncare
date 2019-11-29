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
    private List<Service> services;
    private List<Service> services2;

    private List<Boolean> checkBoxState;


    public ServiceListCheckable(){
        super(new Activity(), R.layout.activity_service_list_checkable);
    }

    public ServiceListCheckable(Activity context, List<Service> services, List<Boolean> checkBoxState,List<Service> services2

    ) {
        super(context, R.layout.activity_service_list_checkable, services);
        this.context = context;
        this.services = services;
        this.services2= services2;
        this.checkBoxState = checkBoxState;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_service_list_checkable, null, true);

        TextView textViewService = listViewItem.findViewById(R.id.textViewService);
        TextView textViewRole = listViewItem.findViewById(R.id.textViewRole);
        TextView textViewPrice =listViewItem.findViewById(R.id.textViewPrice);
        CheckedTextView checkBox = listViewItem.findViewById(R.id.checkBox);

        Service service = services.get(position);

        // set UI element attributes
        textViewService.setText(service.getService());
        textViewRole.setText("Administrator role: "+service.getRole());





        // case: the current checkbox @ position is supposed to be checked
        if (checkBoxState.get(position)) {
            checkBox.setChecked(true);

            textViewPrice.setText("Rate of service: "+ services2.get(position).getPrice());



        }
        else {
            checkBox.setChecked(false);
            textViewPrice.setText("Rate of service: "+service.getPrice());

        }

        return listViewItem;
    }
}
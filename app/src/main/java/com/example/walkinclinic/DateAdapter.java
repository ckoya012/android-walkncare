package com.example.walkinclinic;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Date;
import java.util.List;


public class DateAdapter extends ArrayAdapter<Date> {

    private Activity context;
    List<Date> dates;

    public DateAdapter(){
        super(new Activity(), R.layout.activity_date_adapter);
    }

    public DateAdapter(Activity context, List<Date> dates) {
        super(context, R.layout.activity_date_adapter, dates);
        this.context = context;
        this.dates = dates;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_date_adapter, null, true);

        TextView textViewDate = (TextView) listViewItem.findViewById(R.id.dateTime);


        textViewDate.setText(dates.get(position).toString());
        return listViewItem;
    }
}

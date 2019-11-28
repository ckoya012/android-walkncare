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

import java.util.Date;
import java.util.List;

public class DateAdapterCheckable extends ArrayAdapter<Date> {

    private Activity context;
    private List<Date> dates;
    private List<Boolean> checkBoxState;

    public DateAdapterCheckable(){
        super(new Activity(), R.layout.activity_date_adapter);
    }

    public DateAdapterCheckable(Activity context, List<Date> dates, List<Boolean> checkBoxState) {
        super(context, R.layout.activity_date_adapter, dates);
        this.context = context;
        this.dates = dates;
        this.checkBoxState = checkBoxState;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_date_adapter, null, true);

        TextView textViewDate = (TextView) listViewItem.findViewById(R.id.dateTime);
        CheckedTextView checkBox = listViewItem.findViewById(R.id.checkBox);

        Date date = dates.get(position);

        // set UI element attributes
        textViewDate.setText(date.toString());

        // case: the current checkbox @ position is supposed to be checked
        if (checkBoxState.get(position)) {
            checkBox.setChecked(true);
        }
        else {
            checkBox.setChecked(false);
        }

        return listViewItem;
    }
}
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

public class ScheduleList extends ArrayAdapter<DailySchedule> {

    private Activity context;
    List<DailySchedule> schedules;

    public ScheduleList(){
        super(new Activity(), R.layout.schedule_list);
    }

    public ScheduleList(Activity context, List<DailySchedule> schedules) {
        super(context, R.layout.schedule_list, schedules);
        this.context = context;
        this.schedules = schedules;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.schedule_list, null, true);

        TextView textViewDay = (TextView) listViewItem.findViewById(R.id.dayOfWeek_textView);
        TextView textViewStart = (TextView) listViewItem.findViewById(R.id.startTime_textView);
        TextView textViewEnd = (TextView) listViewItem.findViewById(R.id.endTime_textView);

        DailySchedule dailySchedule = schedules.get(position);
        textViewDay.setText(dailySchedule.getDay());
        textViewStart.setText(dailySchedule.getTime1());
        textViewEnd.setText(dailySchedule.getTime2());

        return listViewItem;
    }
}

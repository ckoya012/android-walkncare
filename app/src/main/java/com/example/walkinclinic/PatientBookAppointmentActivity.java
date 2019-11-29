package com.example.walkinclinic;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.walkinclinic.account.Employee;
import com.example.walkinclinic.account.Patient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class PatientBookAppointmentActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Patient user;
    private Employee clinic;
    private String clinicName;
    private DatabaseReference ref;
    private String uid;
    private String selectedTime;
    private TextView textWaitTimeLabel, dateTime, test;
    private boolean delete;

    private Calendar cal;
    private int day;
    private int month;
    private int year;

    static final int DATE_PICKER_ID = 1111;

    ListView listViewDates;
    List<Date> dates;
    List<Boolean> checkBoxState; // this gets populated when comparing available services to associated services


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_book_appointment);

        Button button = (Button) findViewById(R.id.btnSelectDate);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DialogFragment datePicker = new DatePickerFragment();
//                datePicker.show(getSupportFragmentManager(), "date picker");


                cal = Calendar.getInstance();
                day = cal.get(Calendar.DAY_OF_MONTH);
                month = cal.get(Calendar.MONTH);
                year = cal.get(Calendar.YEAR);
                showDialog(DATE_PICKER_ID);

            }
        });

        clinic = (Employee) getIntent().getSerializableExtra("CLINIC_DATA");
        clinicName = clinic.getTitle();
        textWaitTimeLabel = findViewById(R.id.textViewWaitingTimeLabel);
        textWaitTimeLabel.setText("Current waiting time for " + clinicName + ":");

        user = (Patient) getIntent().getSerializableExtra("USER_DATA");
        uid = user.getID();
        ref = FirebaseDatabase.getInstance().getReference().child("patients").child(uid);

        dates = new ArrayList<>();
        checkBoxState = new ArrayList<>();

        listViewDates = findViewById(R.id.listViewDates);

        Button cancelBtn = (Button) findViewById(R.id.btnCancelAppt);
        test = findViewById(R.id.textViewTESTDATE);

    }

    @Override
    protected DatePickerDialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
                // create a new DatePickerDialog with values you want to show

                DatePickerDialog datePickerDialog = new DatePickerDialog(this, datePickerListener, year, month, day);
                Calendar calendar = Calendar.getInstance();

                calendar.add(Calendar.DATE, 0); // Add 0 days to Calendar
                Date newDate = calendar.getTime();
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                return datePickerDialog;
        }
        return null;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }


    public DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        // the callback received when the user "sets" the Date in the
        // DatePickerDialog
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, selectedYear);
            c.set(Calendar.MONTH, selectedMonth);
            c.set(Calendar.DAY_OF_MONTH, selectedDay);
            String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

            TextView selectedDateLabel = findViewById(R.id.textViewSelectedDate);
            selectedDateLabel.setText(currentDateString);

            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

            dates.clear();

            String fromTime = currentDateString + " 10:00 AM";
            String toTime = currentDateString + " 11:30 AM";
            formatAndAddTimes(fromTime, toTime);

            // create adapter
            DateAdapter adapter = new DateAdapter(PatientBookAppointmentActivity.this, dates);

            // attach adapter to ListView
            listViewDates.setAdapter(adapter);
        }
    };


    private void formatAndAddTimes(String fromTime, String toTime) {
        // Convert currentDateString into target format
        DateFormat originalFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy HH:mm a", Locale.ENGLISH);
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = originalFormat.parse(fromTime);
            date2 = originalFormat.parse(toTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar gc = new GregorianCalendar();
        Date time = date1;
        long timeInMs = date1.getTime();
        long date2InMs = date2.getTime();

        while (timeInMs != date2InMs) {
            gc.setTime(time);
            gc.add(Calendar.MINUTE, 15);
            Date d2 = gc.getTime();
            dates.add(d2);
            time = d2;
            timeInMs = time.getTime();
        }

    }

    public void onClickBookAppointment(View view) {

        TextView value = findViewById(R.id.dateTime);
        String text = value.getText().toString();

        TextView test = findViewById(R.id.textViewTESTDATE);
        test.setText(text);

        // book appointment time
        user.setAppointment(text);
        ref.child("appointments").setValue(user.getAppointment());

        Toast.makeText(PatientBookAppointmentActivity.this, "Appointment booked!", Toast.LENGTH_LONG).show();

    }

    public void onClickCancelAppointment(View view) {
        delete = true;
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(delete) {
                    String s1 = dataSnapshot.child("appointments").getValue(String.class);
                    ref.child("appointments").removeValue();
                    Toast.makeText(PatientBookAppointmentActivity.this, "Your appointment on " + s1 + " has been cancelled.", Toast.LENGTH_LONG).show();
                    delete = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}

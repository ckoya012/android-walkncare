package com.example.walkinclinic;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

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

    public Patient user;
    private DatabaseReference ref;
    private DatabaseReference scheduleRef;
    private String uid;
    private FirebaseAuth mAuth;


    DatabaseReference monday;
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
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        dates = new ArrayList<>();
        checkBoxState = new ArrayList<>();

        listViewDates = findViewById(R.id.listViewDates);
    }



    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        TextView selectedDateLabel = findViewById(R.id.textViewSelectedDate);
        selectedDateLabel.setText(currentDateString);

        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        dates.clear();


        TextView test = findViewById(R.id.textViewTESTDATE);


        String fromTime = currentDateString + " 10:00 AM";
        String toTime = currentDateString + " 11:30 AM";
        formatAndAddTimes(fromTime, toTime);

        // create adapter
        DateAdapter adapter = new DateAdapter(PatientBookAppointmentActivity.this, dates);

        // attach adapter to ListView
        listViewDates.setAdapter(adapter);




        // book appt time
//        user = (Patient) getIntent().getSerializableExtra("USER_DATA");
//        uid = user.getID();
//        user = (Patient) getIntent().getSerializableExtra("USER_DATA");
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference().child("patients").child(uid);



        user.setAppointment("January 1st, 2019");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(Patient.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        scheduleRef = FirebaseDatabase.getInstance().getReference().child("patients").child(uid).child("schedule");








    }


    private void formatAndAddTimes(String fromTime, String toTime) {
        // Convert currentDateString into target format
        DateFormat originalFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy HH:mm a", Locale.ENGLISH);
//        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm a");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = originalFormat.parse(fromTime);
            date2 = originalFormat.parse(toTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        String formattedDate = targetFormat.format(date2);



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

}

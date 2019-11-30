package com.example.walkinclinic;

import com.example.walkinclinic.account.Employee;

import java.sql.Time;
import java.util.Date;

public class Appointment {

    Employee clinic;
    String dayOfWeek;
    Date date;
    Time time;

    public Appointment() { }

    public Appointment (Employee clinic, String dayOfWeek, Date date, Time time) {
        this.clinic = clinic;
        this.date = date;
        this.dayOfWeek = dayOfWeek;
        this.time = time;
    }

    public Appointment (Employee clinic, String dayOfWeek, Date date) {
        this.clinic = clinic;
        this.date = date;
        this.dayOfWeek = dayOfWeek;
    }

    public void setClinic(Employee x) { clinic = x; }
    public void setDayOfWeek(String x) { dayOfWeek = x; }
    public void setDate(Date x) { date = x; }
    public void setTime(Time x) { time = x; }

    public Employee getClinic() { return clinic; }
    public String getDayOfWeek() { return dayOfWeek; }
    public Date getDate() { return date; }
    public Time getTime() { return time; }

}

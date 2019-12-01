package com.example.walkinclinic.account;


public class Patient extends UserAccount {

    private long appointment;
    private String appointmentDate;

    public Patient(String email, String password, String nameFirst, String nameLast, String id) {
        super(email, password, nameFirst, nameLast, id);
    }

    public Patient(String email, String password, String nameFirst, String nameLast, String id, long appointment) {
        super(email, password, nameFirst, nameLast, id);
        this.appointment = appointment;
    }

    public Patient(String email, String password, String nameFirst, String nameLast, String id, String appointmentDate) {
        super(email, password, nameFirst, nameLast, id);
        this.appointmentDate = appointmentDate;
        this.appointment = 0;
    }

    public Patient(String email, String password, String nameFirst, String nameLast, String id, long appointment, String appointmentDate) {
        super(email, password, nameFirst, nameLast, id);
        this.appointment = appointment;
        this.appointmentDate = appointmentDate;
    }

    public void setAppointment(long x) { appointment = x; }

    public long getAppointment() { return appointment; }

    public void setAppointmentDate(String x) { appointmentDate = x; }

    public String getAppointmentDate() { return appointmentDate; }
}

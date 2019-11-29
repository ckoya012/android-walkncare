package com.example.walkinclinic.account;


public class Patient extends UserAccount {

    private String appointment;

    public Patient(String email, String password, String nameFirst, String nameLast, String id) {
        super(email, password, nameFirst, nameLast, id);
    }

    public Patient(String email, String password, String nameFirst, String nameLast, String id, String appointment) {
        super(email, password, nameFirst, nameLast, id);
        this.appointment = appointment;
    }

    public void setAppointment(String x) { appointment = x; }

    public String getAppointment() { return appointment; }
}

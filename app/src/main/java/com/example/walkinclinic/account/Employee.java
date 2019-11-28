package com.example.walkinclinic.account;

import java.io.Serializable;
import java.util.ArrayList;

public class Employee extends UserAccount {
    private String address;
    private String title;
    private String insuranceTypes;
    private String paymentTypes;
    private String phoneNumber;
    private final String CLOSED = "  ";
    private String schedule;
    private float rating=0;
    private float numUsers=0;

    public Employee(String email, String password, String nameFirst, String nameLast, String id) {
        super(email, password, nameFirst, nameLast, id);
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String time1, String time2) {
        if(time1==null){
            return;
        }
        if (time1.compareTo(CLOSED)==0){
            schedule = "Closed";
        }else{
            schedule= "Open today from "+time1+" to "+time2;
        }

    }
    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        numUsers++;
        this.rating= (this.rating +rating)/numUsers;

    }

    public void setAddress(String x){
        address = x;
    }
    public void setTitle(String x){
        title = x;
    }
    public void setInsuranceTypes(String x){
        insuranceTypes = x;
    }
    public void setPaymentTypes(String x){
        paymentTypes = x;
    }
    public void setPhoneNumber(String x){
        phoneNumber = x;
    }

    public String getAddress(){
        return address;
    }

    public String getTitle(){
        return title;
    }

    public String getInsuranceTypes(){
        return insuranceTypes;
    }
    public String getPaymentTypes(){
        return paymentTypes;
    }
    public String getPhoneNumber(){
        return phoneNumber;
    }
}

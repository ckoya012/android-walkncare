package com.example.walkinclinic.account;

import java.io.Serializable;
import java.util.ArrayList;

public class Employee extends UserAccount {
    private String address;
    private String title;
    private String insuranceTypes;
    private String paymentTypes;
    private String phoneNumber;
    private ArrayList<String> associatedServices;

    public Employee(String email, String password, String nameFirst, String nameLast, String id) {
        super(email, password, nameFirst, nameLast, id);
        associatedServices = new ArrayList<String>();
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

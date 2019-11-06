package com.example.walkinclinic.account;

public class Service {
    private String service;
    private double price;
    private String role;


    public Service() {

    }

    public Service(String service) {
        this.service = service;
    }

    public Service(String service, double price) {
        this.service = service;
        this.price = price;
    }

    public Service(String service, double price, String role) {
        this.service = service;
        this.price = price;
        this.role = role;
    }


    public String getService() {
        return service;
    }

    public void setService(String s) {
        this.service = s;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double p) {
        this.price = p;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String r) {
        this.role = r;
    }
}


package com.example.walkinclinic.account;

public class Service {
    private String service;
    private double price;
    private String role;
    private String id;


    public Service() {

    }

    public Service(String service,String id) {
        this.service = service;
        this.id=id;
    }

   /*public Service (String service, double price,String id) {
        this.service = service;
       // this.price = price;
        this.id=id;

    }*/

    public Service(String service/*, double price*/, String role,String id) {
        this.service = service;
        //this.price = price;
        this.role = role;
        this.id=id;
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
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public void setRole(String r) {
        this.role = r;
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof Service){
            Service otherService = (Service) other;
            return this.id.equals(otherService.getId());
        }

        return false;
    }
}


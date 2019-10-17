package com.example.walkinclinic.account;

public class Employee extends UserAccount {
    public Employee(String email, String password, String nameFirst, String nameLast) {
        super(email, password, nameFirst, nameLast,true);
    }
}

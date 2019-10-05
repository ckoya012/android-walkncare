package com.example.walkinclinic.account;

public abstract class UserAccount extends Account {
    private String nameFirst;
    private String nameLast;


    public UserAccount(String email, String password, String nameFirst, String nameLast) {
        super(email, password);
        this.nameFirst = nameFirst;
        this.nameLast = nameLast;

    }
}

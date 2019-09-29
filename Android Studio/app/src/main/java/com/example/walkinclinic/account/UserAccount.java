package com.example.walkinclinic.account;

public abstract class UserAccount extends Account {
    private String nameFirst;
    private String nameLast;
    private int UID;

    public UserAccount(String email, String password, String nameFirst, String nameLast, int UID) {
        super(email, password);
        this.nameFirst = nameFirst;
        this.nameLast = nameLast;
        this.UID = UID;
    }
}

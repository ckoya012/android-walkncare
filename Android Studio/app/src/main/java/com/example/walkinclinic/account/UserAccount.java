package com.example.walkinclinic.account;

public abstract class UserAccount extends Account {
    private String nameFirst;
    private String nameLast;
    private boolean isEmployee;

    public UserAccount(String email, String password, String nameFirst, String nameLast, boolean isEmployee) {
        super(email, password);
        this.nameFirst = nameFirst;
        this.nameLast = nameLast;
        this.isEmployee = isEmployee;
    }

    public String getNameFirst() {
        return nameFirst;
    }

    public String getNameLast() {
        return nameLast;
    }
    public boolean getIsEmployee(){return isEmployee;}
}

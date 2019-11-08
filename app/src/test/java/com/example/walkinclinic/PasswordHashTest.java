package com.example.walkinclinic;


import static org.junit.Assert.*;
import org.junit.Test;

public class PasswordHashTest {
    @Test
    public void getHashedPassword(){
        String hashOfPassword = SignUpActivity.getHashedPassword("password");
        assertEquals("Hashing The Word password:", hashOfPassword, "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8");
    }
    @Test
    public void getWrongHash(){
        String hashOfPassword = SignUpActivity.getHashedPassword("password");
        assertNotEquals("Hashing The Word password:", hashOfPassword, "5e88f047151dd3gbbdd62a11ef721d154f");
    }
}

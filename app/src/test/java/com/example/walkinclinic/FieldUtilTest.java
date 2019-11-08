package com.example.walkinclinic;


import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class FieldUtilTest {
    @Test
    public void testFieldsAreValid(){
        boolean actual = FieldUtil.fieldsAreValid("Bob","Bobby","bob@gmail.com","password",null);
        assertTrue("Fields Are Valid Test: ", actual);
    }
    @Test
    public void testEmailsValid(){
        boolean actual = FieldUtil.emailIsValid("bob@gmail.com");
        assertTrue("Fields Are Valid Test: ", actual);
    }
    @Test
    public void testEmailsInvalid(){
        boolean actual = FieldUtil.emailIsValid("bobgmailcom");
        assertFalse("Fields Are Valid Test: ", actual);
    }
    @Test
    public void testPwdValid(){
        boolean actual = FieldUtil.validPWD("Password");
        assertTrue("Fields Are Valid Test: ", actual);
    }
    @Test
    public void testPwdInvalid(){
        boolean actual = FieldUtil.validPWD("5435");
        assertFalse("Fields Are Valid Test: ", actual);
    }

}

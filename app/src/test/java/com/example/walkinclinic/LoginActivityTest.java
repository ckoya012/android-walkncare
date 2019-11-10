package com.example.walkinclinic;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginActivityTest {
    @Test
    public void testEmailsValid(){
        boolean actual = LoginActivity.emailIsValid("bob@gmail.com");
        assertTrue("Fields Are Valid Test: ", actual);
    }
    @Test
    public void testEmailsInvalid(){
        boolean actual = LoginActivity.emailIsValid("bobgmailcom");
        assertFalse("Fields Are Valid Test: ", actual);
    }
}

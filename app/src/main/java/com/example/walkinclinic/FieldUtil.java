package com.example.walkinclinic;

import android.content.Context;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FieldUtil {

    public static boolean fieldsAreValid(String firstName, String lastName, String email, String pwd, Context c) {
        // check name length
        if (firstName.length() < 3) {
            Toast.makeText(c, "First name should be at least 3 characters", Toast.LENGTH_LONG).show();
            return false;
        }
        if (lastName.length() < 2) {
            Toast.makeText(c, "Last name should be at least 2 characters", Toast.LENGTH_LONG).show();
            return false;
        }
        // check email pattern

        if (!LoginActivity.emailIsValid(email)) {
            Toast.makeText(c, "Please enter a valid email", Toast.LENGTH_LONG).show();
            return false;
        }
        // check password length and match
        if (!validPWD(pwd)) {
            Toast.makeText(c, "Password must be at least 6 characters", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public static boolean validPWD(String pwd) {
       return (pwd.length() > 6);
    }
}

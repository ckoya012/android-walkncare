package com.example.walkinclinic;

import androidx.test.rule.ActivityTestRule;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

public class SignUpActivityTest {


    @Rule
    public ActivityTestRule<SignUpActivity> signUpActivityTestRule = new ActivityTestRule<SignUpActivity>(SignUpActivity.class);

    @Test
    public void checkFieldsExist() {
        onView(withId(R.id.nameFirst)).check(matches(withHint("First name")));
        onView(withId(R.id.nameLast)).check(matches(withHint("Last name")));
        onView(withId(R.id.email)).check(matches(withHint("E-mail")));
        onView(withId(R.id.password)).check(matches(withHint("Password")));
        onView(withId(R.id.isPatient)).check(matches(isClickable()));
        onView(withId(R.id.isClinicEmployee)).check(matches(isClickable()));
    }

    @Test
    public void checkText() {
        onView(withId(R.id.nameFirst)).perform(typeText("First Name"));
        onView(withId(R.id.nameLast)).perform(typeText("Last Name"));
        onView(withId(R.id.email)).perform(typeText("hello@hello.com"));
    }
}
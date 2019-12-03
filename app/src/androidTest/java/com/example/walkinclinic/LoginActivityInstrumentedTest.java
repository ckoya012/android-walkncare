package com.example.walkinclinic;

import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class LoginActivityInstrumentedTest {

    @Rule
    public ActivityTestRule<LoginActivity> logicActivityTestRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);

    @Test
    public void checkFieldsExist() {
        onView(withId(R.id.loginEmail)).check(matches(withHint("E-mail")));
        onView(withId(R.id.loginPwd)).check(matches(withHint("Password")));
    }

    @Test
    public void checkText() {
        onView(withId(R.id.loginEmail)).perform(typeText("hello@hello.com"));
        onView(withId(R.id.loginPwd)).perform(typeText("password"));
    }

    @Test
    public void testLaunch() {
        onView(withId(R.id.redirectSignup))
                .check(matches(withText("Not registered? Sign Up here!")));
        onView(withId(R.id.loginBtn)).check(matches(isClickable()));
    }
}
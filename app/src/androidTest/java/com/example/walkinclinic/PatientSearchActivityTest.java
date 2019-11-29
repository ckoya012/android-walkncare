package com.example.walkinclinic;

import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class PatientSearchActivityTest {

    @Rule
    public ActivityTestRule<PatientSearchActivity> pMainActivityTestRule = new ActivityTestRule<>(PatientSearchActivity.class);

    @Test
    // If I'm able to launch the view and it says 'logged in as patient', then activity launch is successful
    public void testLaunch() {
        onView(withId(R.id.welcomeMsgPat))
                .check(matches(withText("Welcome null! You are logged in as a patient.")));

//        View view = pMainActivity.findViewById(R.id.welcomeMsgPat);
//        assertNotNull(view);
    }

}
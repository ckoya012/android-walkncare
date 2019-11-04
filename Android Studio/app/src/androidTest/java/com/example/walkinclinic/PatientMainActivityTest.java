package com.example.walkinclinic;

import android.view.View;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class PatientMainActivityTest {

    @Rule
    public ActivityTestRule<PatientMainActivity> pMainActivityTestRule = new ActivityTestRule<PatientMainActivity>(PatientMainActivity.class);

    private PatientMainActivity pMainActivity = null;

    @Before
    public void setUp() throws Exception {
        pMainActivity = pMainActivityTestRule.getActivity();
    }

    @Test
    // If I'm able to find the view and it's not null, activity launch is successful
    public void testLaunch() {
        View view = pMainActivity.findViewById(R.id.welcomeMsgPat);
        assertNotNull(view);
    }

    @After
    public void tearDown() throws Exception {
        pMainActivity = null;
    }
}
package com.example.walkinclinic;

import org.junit.Test;

import static org.junit.Assert.*;

public class PatientBookAppointmentActivityTest {
    @Test
    public void testGetDayOfWeek() {
        String actual = PatientBookAppointmentActivity.getDayOfWeek(1);
        assertEquals("int 1 to dayOfWeek","7_sunday", actual);
    }

    @Test
    public void testGetDayOfWeekDefault() {
        String actual = PatientBookAppointmentActivity.getDayOfWeek(9999);
        assertEquals("int 9999 to dayOfWeek","1_monday", actual);
    }
}
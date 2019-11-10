package com.example.walkinclinic;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FieldUtilTestServices {

    @Test
    public void testServiceIsValid(){
        boolean actual = ServiceListViewerActivity.fieldsAreValid("Flu vaccine", "Nurse", null);
        assertTrue("Fields Are Valid Test: ", actual);
    }
    @Test
    public void testNameIsValid(){
        boolean actual = ServiceListViewerActivity.serviceNameIsValid("Physiotherapy");
        assertTrue("Fields Are Valid Test: ", actual);
    }
    @Test
    public void testNameIsInvalid(){
        boolean actual = ServiceListViewerActivity.serviceNameIsValid("L");
        assertFalse("Fields Are Valid Test: ", actual);
    }
    @Test
    public void testRoleInvalid(){
        boolean actual = ServiceListViewerActivity.roleIsValid("Doctor");
        assertTrue("Fields Are Valid Test: ", actual);
    }
    @Test
    public void testRoleIsInvalid(){
        boolean actual = ServiceListViewerActivity.roleIsValid("N");
        assertFalse("Fields Are Valid Test: ", actual);
    }

}

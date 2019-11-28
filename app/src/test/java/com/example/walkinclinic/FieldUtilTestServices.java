package com.example.walkinclinic;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FieldUtilTestServices {

    @Test
    public void testServiceIsValid(){
        boolean actual = AdminServiceListViewerActivity.fieldsAreValid("Flu vaccine", "Nurse", null);
        assertTrue("Fields Are Valid Test: ", actual);
    }
    @Test
    public void testNameIsValid(){
        boolean actual = AdminServiceListViewerActivity.serviceNameIsValid("Physiotherapy");
        assertTrue("Fields Are Valid Test: ", actual);
    }
    @Test
    public void testNameIsInvalid(){
        boolean actual = AdminServiceListViewerActivity.serviceNameIsValid("L");
        assertFalse("Fields Are Valid Test: ", actual);
    }
    @Test
    public void testRoleInvalid(){
        boolean actual = AdminServiceListViewerActivity.roleIsValid("Doctor");
        assertTrue("Fields Are Valid Test: ", actual);
    }
    @Test
    public void testRoleIsInvalid(){
        boolean actual = AdminServiceListViewerActivity.roleIsValid("N");
        assertFalse("Fields Are Valid Test: ", actual);
    }

}

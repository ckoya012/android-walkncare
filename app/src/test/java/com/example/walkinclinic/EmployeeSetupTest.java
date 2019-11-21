package com.example.walkinclinic;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

    public class EmployeeSetupTest {
        @Test
        public void testAtLeastOneChecked(){
            boolean actual = EmployeeSetupActivity.noneChecked("101");
            assertFalse("Fields Are Valid Test: ", actual);
        }

        @Test
        public void testNoneChecked(){
            boolean actual = EmployeeSetupActivity.noneChecked("000");
            assertTrue("Fields Are Valid Test: ", actual);
        }

    }

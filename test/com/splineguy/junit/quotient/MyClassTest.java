package com.splineguy.junit.quotient;

import org.junit.Test;

import static org.junit.Assert.*;

public class MyClassTest {

    @Test
    public void testQuotient() {
        MyClass tester = new MyClass();
        assertEquals("10 / 5 = 2", 2, tester.quotient(10, 5));
    }
    @Test(expected = IllegalArgumentException.class)
    public void testExceptionIsThrown(){
        MyClass tester = new MyClass();
        tester.quotient(10,0);
    }
}
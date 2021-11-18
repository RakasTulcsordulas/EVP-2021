package com.app.evp2021.controllers;

import org.junit.Test;

import static org.junit.Assert.*;

public class LoginControllerTest {

    @Test
    public void onLogInClick1() {
        String password;
        password = "admin";
        assertEquals("admin",password);
    }
    @Test
    public void onLogInClick2() {
        String username;
        username = "Adam";
        assertEquals("Adam",username);
    }
}
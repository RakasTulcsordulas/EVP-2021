package com.app.evp2021.controllers;

import com.app.evp2021.services.UserSession;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class UserSessionTest {

    @Test
    public void beforeConstuctorCalled() {
        assertTrue(UserSession.getSession() == null);
        assertFalse(UserSession.getSession() != null);
    }

    @Test
    public void onConstructorCalled() {
        UserSession.setSession("Zsolt", false);
        assertFalse(UserSession.getSession() == null);
    }

    @Test
    public void getEmployee() {
        UserSession.setSession("Zsolt", false);
        assertTrue(UserSession.getSession().getEmployee() == "Zsolt");
        assertFalse(UserSession.getSession().getEmployee() == "Adam");
    }

    @Test
    public void getLoggedIn() {
        UserSession.setSession("Zsolt", false);
        assertTrue(!UserSession.getSession().getLoggedIn());
        assertFalse(UserSession.getSession().getLoggedIn());
    }

    @Test
    public void setSessionAfterConstructor() {
        UserSession.setSession("Zsolt", false);
        assertTrue(!UserSession.getSession().getLoggedIn());
        //new instance gotten
        UserSession.setSession("Zsolt", true);
        assertTrue(UserSession.getSession().getLoggedIn());
    }

    @Test
    public void onLogout() {
        UserSession.setSession("Zsolt", true);
        assertTrue(UserSession.getSession().getLoggedIn());
        UserSession.logout();
        assertTrue(UserSession.getSession().getEmployee() == null);
        assertFalse(UserSession.getSession().getLoggedIn());
    }
}
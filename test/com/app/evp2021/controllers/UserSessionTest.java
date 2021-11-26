package com.app.evp2021.controllers;

import com.app.evp2021.services.UserSession;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class UserSessionTest {

    @Test
    public void beforeConstuctorCalled() {                      //Mielőtt belépne valaki alapbol az értékek null-ok és ezt ellenőrzi
        assertTrue(UserSession.getSession() == null);
        assertFalse(UserSession.getSession() != null);
    }

    @Test
    public void onConstructorCalled() {                         //Belépéskor a konstruktor megkapja ki lép be vagy hogy bevan-e lépve vagy nem változot
        UserSession.setSession("Zsolt", false);
        assertFalse(UserSession.getSession() == null);
    }

    @Test
    public void getEmployee() {                                 //Visszaadja a belépett személy nevét
        UserSession.setSession("Zsolt", false);
        assertTrue(UserSession.getSession().getEmployee() == "Zsolt");
        assertFalse(UserSession.getSession().getEmployee() == "Adam");
    }

    @Test
    public void getLoggedIn() {                                 //Visszaadja a belépett személy belépett-e vagy sem
        UserSession.setSession("Zsolt", false);
        assertTrue(!UserSession.getSession().getLoggedIn());
        assertFalse(UserSession.getSession().getLoggedIn());
    }

    @Test
    public void setSessionAfterConstructor() {                  //Felülírja a már meglévo belépett személy "session"-jet, ugyan az mintha elején belépne(, meglehet adni nevet és belépés státuszt)
        UserSession.setSession("Zsolt", false);
        assertTrue(!UserSession.getSession().getLoggedIn());
        //new instance gotten
        UserSession.setSession("Zsolt", true);
        assertTrue(UserSession.getSession().getLoggedIn());
    }

    @Test
    public void onLogout() {                                    //Kilepes, nulloz mindent
        UserSession.setSession("Zsolt", true);
        assertTrue(UserSession.getSession().getLoggedIn());
        UserSession.logout();
        assertTrue(UserSession.getSession().getEmployee() == null);
        assertFalse(UserSession.getSession().getLoggedIn());
    }
}
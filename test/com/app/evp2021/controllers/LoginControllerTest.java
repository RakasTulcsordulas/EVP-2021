package com.app.evp2021.controllers;

import com.app.sql.MySQLConnect;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class LoginControllerTest {

    MySQLConnect con = new MySQLConnect();

    @Test
    public void onLogInClick1() {
        try{
            con.establishConnection();
        }catch(SQLException err) {}

        assertTrue(con.checkUsernameExists("attila"));
        con.closeConnection();
    }
    @Test
    public void onLogInClick2() {
        try{
            con.establishConnection();
        }catch(SQLException err) {}

        assertTrue(con.checkUsernameExists("valami"));
        con.closeConnection();
    }
}
package com.app.evp2021.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.app.sql.*;

import javax.sql.RowSet;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    MySQLConnect con = new MySQLConnect();

    @FXML private Label welcomeText;
    @FXML private TextArea username;
    @FXML private PasswordField password;
    @FXML private Button logIn;
    @FXML private Label missing;

    //Adding user to database
    @FXML protected void onLogInClick() {
        con.establishConnection();

        if (con.checkPassword(username.getText(), password.getText())) {
            //TODO Admin page
            return;
        }
        con.closeConnection();
    }
}

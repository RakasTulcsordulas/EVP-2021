package com.app.evp2021;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.app.sql.*;

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

        if (username.getText() == "" || password.getText() == "") {
            missing.setVisible(true);
            return;
        }


        if (con.checkPassword(username.getText(), password.getText())) {
            //TODO Admin page
            return;
        }
        con.closeConnection();
    }
}

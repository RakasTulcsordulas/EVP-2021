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
    @FXML private TextArea emailAddress;
    @FXML private Button signUp;
    @FXML private Label missing;
    @FXML private DatePicker datePicker;

    //Adding user to database
    @FXML protected void onSignUpClick() {


        con.establishConnection();
        if (username.getText() != "" && password.getText() != "" && emailAddress.getText() != "") {

            if (    con.checkUsernameExists(username.getText()) == true &&
                    con.checkEmailExists(emailAddress.getText()) ==true) {
                System.out.println("Exists");
                return;
            }
            con.signUpNewUser(username.getText(), password.getText(), emailAddress.getText());
            con.closeConnection();
        } else {
            missing.setVisible(true);}
    }
}

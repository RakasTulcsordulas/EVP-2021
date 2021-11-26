package com.app.evp2021.controllers;

import com.app.evp2021.services.UserSession;
import com.app.evp2021.views.LandingPage;
import com.app.evp2021.views.LoginModal;
import com.app.sql.MySQLConnect;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class LoginModalController {
    @FXML private TextField username;
    @FXML private PasswordField psw;
    @FXML private Text errormsg;

    @FXML
    private void onLoginAttempt() throws Exception{
        if(username.getText().isEmpty() || psw.getText().isEmpty()) {
            errormsg.setText("Minden mező kitöltése kötelező!");
        }else{

            MySQLConnect dbConnection = new MySQLConnect();
            dbConnection.establishConnection();
            boolean loginAttempt = dbConnection.checkPassword(username.getText(), psw.getText());

            if(loginAttempt) {
                LoginModal.close();
                UserSession.setSession(username.getText(), true);
                LandingPage.refresh();
            }else{
                errormsg.setText("Autentikációs hiba történt!");
            }

        }
    }
}

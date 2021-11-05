package com.app.evp2021.controllers;

import com.app.evp2021.services.UserSession;
import com.app.evp2021.views.LandingPage;
import com.app.evp2021.views.LoginModal;
import com.app.sql.MySQLConnect;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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

            MySQLConnect con = new MySQLConnect();
            con.establishConnection();
            boolean login = con.checkPassword(username.getText(), psw.getText());

            if(login) {
                LoginModal.showSuccessPane("Sikeres belépés! Üdvözöllek " + username.getText());
                UserSession.setSession(username.getText(), true);
                LandingPage.refresh();
            }else{
                errormsg.setText("Autentikációs hiba történt!");
            }

        }
    }
}

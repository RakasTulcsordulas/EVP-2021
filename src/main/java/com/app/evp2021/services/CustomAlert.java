package com.app.evp2021.services;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class CustomAlert {

    public CustomAlert(Alert.AlertType type, String title, String headerText, String footerText, ButtonType buttonToClose) {
        Alert alertWindow = new Alert(type);
        alertWindow.setTitle(title);
        alertWindow.setHeaderText(headerText);
        alertWindow.setContentText(footerText);

        Optional<ButtonType> btnClickResult = alertWindow.showAndWait();
        if(btnClickResult.get() == buttonToClose) {
            Platform.exit();
        }
    }
}

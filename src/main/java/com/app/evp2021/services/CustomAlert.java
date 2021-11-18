package com.app.evp2021.services;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class CustomAlert {
    public CustomAlert(Alert.AlertType type, String title, String headerText, String footerText) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(footerText);

        alert.showAndWait();
    }

    public CustomAlert(Alert.AlertType type, String title, String headerText, String footerText, ButtonType buttonToClose) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(footerText);

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == buttonToClose) {
            Platform.exit();
        }
    }
}

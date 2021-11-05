package com.app.evp2021.views;

import com.app.evp2021.Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class LoginModal {

        public static void display() throws Exception
        {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-modal.fxml"));
            Scene scene1= new Scene(fxmlLoader.load());
            Stage popupwindow=new Stage();
            popupwindow.initModality(Modality.APPLICATION_MODAL);
            popupwindow.setResizable(false);

            popupwindow.setTitle("CinemApp - Admin belépés");

            Button exit_btn = (Button) scene1.lookup("#exit_btn");

            exit_btn.setOnAction(e -> popupwindow.close());

            scene1.getStylesheets().add(Main.class.getResource("css/bootstrap.css").toExternalForm());
            scene1.getStylesheets().add(Main.class.getResource("css/main.css").toExternalForm());

            popupwindow.setScene(scene1);
            popupwindow.showAndWait();

        }
}

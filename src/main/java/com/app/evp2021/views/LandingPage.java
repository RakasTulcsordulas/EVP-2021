package com.app.evp2021.views;

import com.app.evp2021.Main;
import com.app.evp2021.controllers.LandingPageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileInputStream;
import java.io.IOException;

public class LandingPage extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("landing-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("CinemApp - Főoldal");

        stage.setMinWidth(1024);
        stage.setMinHeight(768);

        stage.setMaximized(true);

        DatePicker dp = (DatePicker) scene.lookup("#datepicker");
        DatePickerSkin dps = new DatePickerSkin(dp);
        HBox holder = (HBox) scene.lookup("#dateholder");
        holder.getChildren().add(dps.getPopupContent());
        holder.getChildren().remove(dp);

        scene.getStylesheets().add(Main.class.getResource("css/bootstrap.css").toExternalForm());
        scene.getStylesheets().add(Main.class.getResource("css/main.css").toExternalForm());

        stage.setScene(scene);
        stage.show();


    }
}

package com.app.evp2021.views;

import com.app.evp2021.Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LandingPage extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(Main.class.getResource("landing-view.fxml"));
        stage.setTitle("CinemApp - Főoldal");
        stage.setScene(new Scene(root));
        stage.minWidthProperty().setValue(640);
        stage.minHeightProperty().setValue(480);
        stage.setMaximized(true);
        stage.show();
    }
}

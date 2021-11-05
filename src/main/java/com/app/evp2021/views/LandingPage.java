package com.app.evp2021.views;

import com.app.evp2021.Main;
import com.app.evp2021.controllers.LandingPageController;
import com.app.evp2021.services.UserSession;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileInputStream;
import java.io.IOException;

public class LandingPage extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    private static Stage _stage = null;

    @Override
    public void start(Stage stage) throws Exception{
        _stage = stage;

        Scene sc = getLoggedOutScene();

        showStage(sc);
    }

    public static void showStage (Scene sc) {
        _stage.close();
        _stage.setMinWidth(1024);
        _stage.setMinHeight(768);

        _stage.setMaximized(true);
        _stage.setScene(sc);
        _stage.show();
    }

    public static void refresh() throws Exception{
        boolean logged = UserSession.getSession().getLoggedIn();
        if(logged == true) { showStage(getLoggedInScene()); }else{ showStage(getLoggedOutScene());}
    }

    public static Scene getLoggedOutScene() throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("landing-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        _stage.setTitle("CinemApp - Főoldal");

        DatePicker dp = (DatePicker) scene.lookup("#datepicker");
        DatePickerSkin dps = new DatePickerSkin(dp);
        HBox holder = (HBox) scene.lookup("#dateholder");
        holder.getChildren().add(dps.getPopupContent());
        holder.getChildren().remove(dp);

        scene.getStylesheets().add(Main.class.getResource("css/bootstrap.css").toExternalForm());
        scene.getStylesheets().add(Main.class.getResource("css/main.css").toExternalForm());

        return scene;
    }

    public static Scene getLoggedInScene() throws Exception{
        System.out.println("meghivva");
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        _stage.setTitle("CinemApp - Admin");

        scene.getStylesheets().add(Main.class.getResource("css/bootstrap.css").toExternalForm());
        scene.getStylesheets().add(Main.class.getResource("css/main.css").toExternalForm());

        return scene;
    }
}

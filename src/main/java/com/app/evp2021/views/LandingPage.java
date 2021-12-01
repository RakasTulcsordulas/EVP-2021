package com.app.evp2021.views;

import com.app.evp2021.Main;
import com.app.evp2021.controllers.LandingPageController;
import com.app.evp2021.services.CustomAlert;
import com.app.evp2021.services.UserSession;
import com.app.sql.MySQLConnect;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.skin.DatePickerSkin;

import javafx.scene.layout.HBox;

import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.sql.SQLException;

public class LandingPage extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private static Stage _stage = null;
    private static Scene active_scene = null;
    private static LandingPageController active_controller = null;

    @Override
    public void start(Stage stage) throws Exception{
        _stage = stage;

        setLoggedOutScene();

        checkConnection();

        showStage(active_scene);
    }

    public static void showStage (Scene scene) {
        _stage.setMinWidth(1124);
        _stage.setMinHeight(868);

        _stage.setMaximized(true);
        _stage.setScene(scene);
        _stage.show();

        _stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                LoginModal.closeWindow();
            }
        });
    }

    public static void refresh() throws Exception{
        boolean loggedIn = UserSession.getSession().getLoggedIn();
        if(loggedIn == true) { setLoggedInScene(); }else{ setLoggedOutScene(); }
    }

    public static void setLoggedOutScene() throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("landing-view.fxml"));
        _stage.setTitle("CinemApp - Főoldal");

        if (active_scene == null) {
            active_scene = new Scene(fxmlLoader.load());
        }else{
            active_scene.setRoot(fxmlLoader.load());
        }

        DatePicker datePicker = (DatePicker) active_scene.lookup("#datepicker");
        DatePickerSkin datePickerSkin = new DatePickerSkin(datePicker);

        HBox dateHolderHbox = (HBox) active_scene.lookup("#dateholder");

        dateHolderHbox.getChildren().add(datePickerSkin.getPopupContent());
        dateHolderHbox.getChildren().remove(datePicker);

        setLogoVisible(true);

        active_scene.getStylesheets().add(Main.class.getResource("css/bootstrap.css").toExternalForm());
        active_scene.getStylesheets().add(Main.class.getResource("css/main.css").toExternalForm());
        active_controller = fxmlLoader.getController();
    }

    public static void setLoggedInScene() throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin-view.fxml"));
        _stage.setTitle("CinemApp - Admin");

        if (active_scene == null) {
            active_scene = new Scene(fxmlLoader.load());
        }else{
            active_scene.setRoot(fxmlLoader.load());
        }

        Button newMovieButton = (Button) active_scene.lookup("#new_movie");
        newMovieButton.setStyle("-fx-cursor: hand;");

        Button logoutButton = (Button) active_scene.lookup("#logout");
        logoutButton.setStyle("-fx-cursor: hand;");

        active_scene.getStylesheets().add(Main.class.getResource("css/bootstrap.css").toExternalForm());
        active_scene.getStylesheets().add(Main.class.getResource("css/main.css").toExternalForm());
        active_controller = fxmlLoader.getController();

        getAuditoriums(active_scene);
        getMovies(active_scene);
        active_controller.initializeAdminView();
    }

    private static void getAuditoriums(Scene sc) {
        active_controller.setAuditoriumsOnAdminView();
    }

    private static void getMovies(Scene sc) {
        active_controller.setMoviesOnAdminView();
    }



    public static void setLogoVisible(boolean b) {
        try{
            HBox logoHolderHbox = (HBox) active_scene.lookup("#logo_holder");
            logoHolderHbox.setVisible(b);
        }catch (RuntimeException err) {
            err.printStackTrace();
        }

    }

    public static void checkConnection() {
        try {
            MySQLConnect dbConnection = new MySQLConnect();
            dbConnection.establishConnection();
        }catch (SQLException err) {
            new CustomAlert(Alert.AlertType.ERROR, "Nincs kapcsolat", "Nem sikerült kapcsolódni az adatbázishoz!", "A program most bezárul!", ButtonType.OK);
        }
    }
}

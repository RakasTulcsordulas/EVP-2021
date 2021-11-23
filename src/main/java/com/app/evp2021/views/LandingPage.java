package com.app.evp2021.views;

import com.app.evp2021.Main;
import com.app.evp2021.controllers.LandingPageController;
import com.app.evp2021.services.CustomAlert;
import com.app.evp2021.services.UserSession;
import com.app.sql.MySQLConnect;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.skin.DatePickerSkin;

import javafx.scene.layout.AnchorPane;
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

    @Override
    public void start(Stage stage) throws Exception{
        _stage = stage;

        //active_scene = getLoggedOutScene();


        /*ArrayList<Object> arr = new ArrayList<Object>();
        for(int i = 0; i < 2; i++) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("audit-view.fxml"));
            AnchorPane a = fxmlLoader.load();
            Auditorium c = fxmlLoader.getController();
            c.create();
            c.setTitle("film"+i);
            Object[] view_contr = new Object[2];
            view_contr[0] = a;
            view_contr[1] = c;
            arr.add(view_contr);
            parent.getChildren().add(a);
            parent.setLeftAnchor(a, 0.0);
            parent.setRightAnchor(a, 0.0);
            parent.setTopAnchor(a, 0.0);
            parent.setBottomAnchor(a, 0.0);
            c.showSelf(false);
        }*/
        setLoggedOutScene();

        checkConnection();

        showStage(active_scene);



        /*Object[] o = (Object[]) arr.get(0);
        AnchorPane anchor = (AnchorPane) o[0];
        Auditorium control = (Auditorium) o[1];
        control.showSelf(true);
        control.setSeat(2,2, 1);*/
    }

    public static void showStage (Scene sc) {
        _stage.setMinWidth(1124);
        _stage.setMinHeight(868);

        _stage.setMaximized(true);
        _stage.setScene(sc);
        _stage.show();

        _stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                LoginModal.close();
            }
        });
    }

    public static void refresh() throws Exception{
        boolean logged = UserSession.getSession().getLoggedIn();
        if(logged == true) { setLoggedInScene(); }else{ setLoggedOutScene();}
    }

    public static void setLoggedOutScene() throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("landing-view.fxml"));
        _stage.setTitle("CinemApp - Főoldal");

        if (active_scene == null) {
            active_scene = new Scene(fxmlLoader.load());
        }else{
            active_scene.setRoot(fxmlLoader.load());
        }

        DatePicker dp = (DatePicker) active_scene.lookup("#datepicker");
        DatePickerSkin dps = new DatePickerSkin(dp);
        HBox holder = (HBox) active_scene.lookup("#dateholder");
        holder.getChildren().add(dps.getPopupContent());
        holder.getChildren().remove(dp);

        setLogoVisible(true);

        active_scene.getStylesheets().add(Main.class.getResource("css/bootstrap.css").toExternalForm());
        active_scene.getStylesheets().add(Main.class.getResource("css/main.css").toExternalForm());
    }

    public static void setLoggedInScene() throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin-view.fxml"));
        _stage.setTitle("CinemApp - Admin");

        if (active_scene == null) {
            active_scene = new Scene(fxmlLoader.load());
        }else{
            active_scene.setRoot(fxmlLoader.load());
        }

        Button new_movie = (Button) active_scene.lookup("#new_movie");
        new_movie.setStyle("-fx-cursor: hand;");

        Button logout = (Button) active_scene.lookup("#logout");
        logout.setStyle("-fx-cursor: hand;");

        active_scene.getStylesheets().add(Main.class.getResource("css/bootstrap.css").toExternalForm());
        active_scene.getStylesheets().add(Main.class.getResource("css/main.css").toExternalForm());

        getAuditoriums(active_scene);
    }

    private static void getAuditoriums(Scene sc) {
        Object[][] result = null;
        MySQLConnect con = new MySQLConnect();
        try {
            con.establishConnection();
            result = con.getAuditorium(null);
            for(int i = 1; i < result.length; i++){
                HBox container = (HBox) sc.lookup("#audit_btn_holder");
                Button btn = new Button(result[i][2].toString() + "" + i);
                btn.getStyleClass().add("btn");
                btn.getStyleClass().add("btn-primary");
                btn.setStyle("-fx-cursor: hand; -fx-padding: 10 5 10 5;");
                container.getChildren().add(btn);

                final int id = (Integer) result[i][1];

                btn.setOnMouseClicked(event -> {
                    try {
                        LandingPageController.showAuditorium(id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }catch (SQLException err){
            err.printStackTrace();
        }
    }



    public static void setLogoVisible(boolean b) {
        try{
            HBox logo_holder = (HBox) active_scene.lookup("#logo_holder");
            logo_holder.setVisible(b);
        }catch (RuntimeException err) {}

    }

    public static void checkConnection() {
        try {
            MySQLConnect con = new MySQLConnect();
            con.establishConnection();
        }catch (SQLException err) {
            new CustomAlert(Alert.AlertType.ERROR, "Nincs kapcsolat", "Nem sikerült kapcsolódni az adatbázishoz!", "A program most bezárul!", ButtonType.OK);
        }
    }
}

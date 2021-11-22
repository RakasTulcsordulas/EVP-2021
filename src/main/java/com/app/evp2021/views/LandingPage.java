package com.app.evp2021.views;

import com.app.evp2021.Main;
import com.app.evp2021.services.CustomAlert;
import com.app.evp2021.services.UserSession;
import com.app.sql.MySQLConnect;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;

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
        active_scene = getLoggedInScene();
        AnchorPane parent = (AnchorPane) active_scene.lookup("#holder");

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

        checkConnection();

        showStage(active_scene);

        setLogoVisible(true);



        /*Object[] o = (Object[]) arr.get(0);
        AnchorPane anchor = (AnchorPane) o[0];
        Auditorium control = (Auditorium) o[1];
        control.showSelf(true);
        control.setSeat(2,2, 1);*/
    }

    public static void showStage (Scene sc) {
        _stage.close();
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
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        _stage.setTitle("CinemApp - Admin");

        Button new_movie = (Button) scene.lookup("#new_movie");
        new_movie.setStyle("-fx-cursor: hand;");

        Button logout = (Button) scene.lookup("#logout");
        logout.setStyle("-fx-cursor: hand;");

        scene.getStylesheets().add(Main.class.getResource("css/bootstrap.css").toExternalForm());
        scene.getStylesheets().add(Main.class.getResource("css/main.css").toExternalForm());

        getAuditoriums();

        return scene;
    }

    private static void getAuditoriums() {
        Object[][] result = null;
        MySQLConnect con = new MySQLConnect();
        try {
            con.establishConnection();
            result = con.getAuditorium(null);
            System.out.println(result[1][0]);
            System.out.println(result[2][0]);
            System.out.println(result[3][0]);

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

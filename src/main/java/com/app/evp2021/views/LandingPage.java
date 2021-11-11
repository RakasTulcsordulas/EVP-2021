package com.app.evp2021.views;

import com.app.evp2021.Main;
import com.app.evp2021.controllers.Auditorium;
import com.app.evp2021.services.UserSession;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class LandingPage extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    private static Stage _stage = null;
    private static Scene active_scene = null;
    private static AnchorPane auditorium_holder = null;

    @Override
    public void start(Stage stage) throws Exception{
        _stage = stage;

        active_scene = getLoggedOutScene();
        AnchorPane parent = (AnchorPane) active_scene.lookup("#holder");
        FXMLLoader fxmlLoader;
        fxmlLoader = new FXMLLoader(Main.class.getResource("audit-view.fxml"));
        AnchorPane a = fxmlLoader.load();
        Auditorium c = fxmlLoader.<Auditorium>getController();
        c.create(18,18);


        fxmlLoader = new FXMLLoader(Main.class.getResource("audit-view.fxml"));
        AnchorPane a1 = fxmlLoader.load();
        Auditorium c1 = fxmlLoader.<Auditorium>getController();
        c1.create(18,10);

        parent.getChildren().add(a1);

        showStage(active_scene);

        setLogoVisible(false);
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

        scene.getStylesheets().add(Main.class.getResource("css/bootstrap.css").toExternalForm());
        scene.getStylesheets().add(Main.class.getResource("css/main.css").toExternalForm());

        return scene;
    }

    public static void setLogoVisible(boolean b) throws Exception {
        HBox logo_holder = (HBox) active_scene.lookup("#logo_holder");
        logo_holder.setVisible(b);
    }
}

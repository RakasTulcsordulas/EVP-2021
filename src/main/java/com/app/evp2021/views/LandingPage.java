package com.app.evp2021.views;

import com.app.evp2021.Main;
import com.app.evp2021.controllers.Auditorium;
import com.app.evp2021.services.UserSession;
import javafx.application.Application;
<<<<<<< Updated upstream
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
=======
<<<<<<< Updated upstream
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
=======
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
>>>>>>> Stashed changes
>>>>>>> Stashed changes
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.skin.DatePickerSkin;
<<<<<<< Updated upstream
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
=======
<<<<<<< Updated upstream
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
=======
import javafx.scene.layout.*;
>>>>>>> Stashed changes
>>>>>>> Stashed changes
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.ArrayList;

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

<<<<<<< Updated upstream
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
=======
<<<<<<< Updated upstream
        Scene sc = getLoggedOutScene();

        showStage(sc);
=======
        active_scene = getLoggedOutScene();
        AnchorPane parent = (AnchorPane) active_scene.lookup("#holder");
        ArrayList<Object> arr = new ArrayList<Object>();
        for(int i = 0; i < 2; i++) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("audit-view.fxml"));
            AnchorPane a = fxmlLoader.load();
            Auditorium c = fxmlLoader.getController();
            c.create(18,18);
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
        }
        showStage(active_scene);

        setLogoVisible(false);

        Object[] o = (Object[]) arr.get(0);
        AnchorPane a = (AnchorPane) o[0];
        Auditorium c = (Auditorium) o[1];
        c.showSelf(true);
        c.setSeat(1,1, 1);
>>>>>>> Stashed changes
>>>>>>> Stashed changes
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

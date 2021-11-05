package com.app.evp2021.views;

import com.app.evp2021.Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class LoginModal {

        private static AnchorPane success =  null;
        private static AnchorPane login =  null;
        private static Scene scene = null;
        private static Stage modal = null;

        public static void display() throws Exception
        {
            System.out.println("asd");
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-modal.fxml"));
            scene = new Scene(fxmlLoader.load());
            modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setResizable(false);
            modal.setAlwaysOnTop(true);

            modal.setTitle("CinemApp - Admin belépés");

            Button exit_btn = (Button) scene.lookup("#exit_btn");
            exit_btn.setOnAction(e -> modal.close());

            success =  (AnchorPane) scene.lookup("#success_login");
            login =  (AnchorPane) scene.lookup("#login_pane");
            success.setVisible(false);

            scene.getStylesheets().add(Main.class.getResource("css/bootstrap.css").toExternalForm());
            scene.getStylesheets().add(Main.class.getResource("css/main.css").toExternalForm());

            modal.setScene(scene);
            modal.showAndWait();
        }

        public static void showSuccessPane(String message) {
            login.setVisible(false);
            success.setVisible(true);

            Button success_exit_btn = (Button) scene.lookup("#success_exit_btn");
            success_exit_btn.setOnAction(e -> modal.close());

            Text msg = (Text) scene.lookup("#success_message");
            msg.setText(message);
        }
}

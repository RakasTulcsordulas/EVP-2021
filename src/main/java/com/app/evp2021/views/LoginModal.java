package com.app.evp2021.views;

import com.app.evp2021.Main;

import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.layout.AnchorPane;

import javafx.stage.Modality;

import javafx.stage.Stage;

/**
 * Creates login window.
 */
public class LoginModal {

        private static Scene scene = null;
        private static Stage modal = null;

    /**
     * Creates login display view from fxml.
     */
        public static void displayWindow() throws Exception
        {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-modal.fxml"));
            scene = new Scene(fxmlLoader.load());
            modal = new Stage();

            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setResizable(false);
            modal.setAlwaysOnTop(true);

            modal.setTitle("CinemApp - Admin belépés");

            Button exit_btn = (Button) scene.lookup("#exit_btn");
            exit_btn.setOnAction(e -> modal.close());

            scene.getStylesheets().add(Main.class.getResource("css/bootstrap.css").toExternalForm());
            scene.getStylesheets().add(Main.class.getResource("css/main.css").toExternalForm());

            modal.setScene(scene);
            modal.showAndWait();
        }
    /**
     * Closes the window.
     */
        public static void closeWindow() {
            if(modal != null) {
                modal.close();
            }
        }
}

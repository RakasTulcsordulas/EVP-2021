package com.app.evp2021.views;

import com.app.evp2021.Main;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Creates and displays screening window.
 */
public class ScreeningSetup {

    private static Scene scene = null;
    private static Stage modal = null;

    /**
     * Creates screening window.
     * @param screeningView Node from fxml.
     */
    public static void createWindow(Node screeningView)
    {
        scene = new Scene((Parent) screeningView);
        modal = new Stage();

        modal.initModality(Modality.APPLICATION_MODAL);
        modal.setResizable(false);

        modal.setMinWidth(761);
        modal.setMinHeight(480);

        modal.setTitle("CinemApp - Műsor felugró");

        scene.getStylesheets().add(Main.class.getResource("css/bootstrap.css").toExternalForm());
        scene.getStylesheets().add(Main.class.getResource("css/main.css").toExternalForm());

        modal.setScene(scene);
    }
    /**
     * Display created screening window.
     */
    public static void display() {
        modal.showAndWait();
    }

    /**
     * Returns Stage.
     * @return modal.
     */
    public static Stage getStage() {
        return modal;
    }
}

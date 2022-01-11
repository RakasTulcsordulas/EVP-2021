package com.app.evp2021.views;

import com.app.evp2021.Main;
import com.app.evp2021.controllers.ScreeningSummaryController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Sums up the screening dates.
 */
public class ScreeningSummary {

    private static Scene scene = null;
    private static Stage modal = null;

    /**
     * Creates the window of the Summary.
     */
    public static ScreeningSummaryController createWindow()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("screening-summary-view.fxml"));
        try {

            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        modal = new Stage();

        modal.initModality(Modality.APPLICATION_MODAL);
        modal.setResizable(false);

        modal.setMinWidth(761);
        modal.setMinHeight(868);

        modal.setTitle("CinemApp - Műsor összegzés");

        scene.getStylesheets().add(Main.class.getResource("css/bootstrap.css").toExternalForm());
        scene.getStylesheets().add(Main.class.getResource("css/main.css").toExternalForm());

        modal.setScene(scene);

        return fxmlLoader.getController();
    }

    /**
     * Display window.
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

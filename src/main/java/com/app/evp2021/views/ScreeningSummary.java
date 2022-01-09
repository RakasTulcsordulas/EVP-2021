package com.app.evp2021.views;

import com.app.evp2021.Main;
import com.app.evp2021.controllers.ScreeningSummaryController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ScreeningSummary {

    private static Scene scene = null;
    private static Stage modal = null;


    public static ScreeningSummaryController createWindow() throws Exception
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("screening-summary-view.fxml"));
        scene = new Scene(fxmlLoader.load());
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

    public static void display() {
        modal.showAndWait();
    }

    public static Stage getStage() {
        return modal;
    }
}

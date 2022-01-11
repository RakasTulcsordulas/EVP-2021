package com.app.evp2021.views;

import com.app.evp2021.Main;
import javafx.application.Application;
import javafx.fxml.FXML;
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
/**
 * Class that handles the PopUp of the auditorium.
 */
public class AuditoriumCreationPopup {

    private static Scene scene = null;
    private static Stage modal = null;
    /**
     * Creates window with Node that contains generated grid.
     * @param auditoriumView Fix grid.
     */
    public static void createWindow(Node auditoriumView)
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("popup-audit-creator.fxml"));
       try {
           scene = new Scene(fxmlLoader.load());
       }
       catch (IOException e) {
           e.printStackTrace();
       }
        modal = new Stage();

        modal.initModality(Modality.APPLICATION_MODAL);
        modal.setResizable(false);

        modal.setMinWidth(761);
        modal.setMinHeight(868);

        modal.setTitle("CinemApp - Terem felugró");

        scene.getStylesheets().add(Main.class.getResource("css/bootstrap.css").toExternalForm());
        scene.getStylesheets().add(Main.class.getResource("css/main.css").toExternalForm());

        modal.setScene(scene);

        attachAuditorium(auditoriumView);

    }
    /**
     * Display window.
     */
    public static void display() {
        modal.showAndWait();
    }
    /**
     * Returns stage.
     * @return modal.
     */
    public Stage getStage() {
        return modal;
    }
    /**
     * Attaches the node to the window.
     * @param auditoriumView Fix grid.
     */
    private static void attachAuditorium(Node auditoriumView){
        AnchorPane auditoriumHolder = (AnchorPane) scene.lookup("#holder");

        auditoriumHolder.getChildren().add(auditoriumView);
        auditoriumHolder.setTopAnchor(auditoriumView, 0.0d);
        auditoriumHolder.setBottomAnchor(auditoriumView, 0.0d);
        auditoriumHolder.setLeftAnchor(auditoriumView, 0.0d);
        auditoriumHolder.setRightAnchor(auditoriumView, 0.0d);

    }
}

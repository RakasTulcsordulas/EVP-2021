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

public class AuditCreationPopup {

    private static Scene scene = null;
    private static Stage modal = null;

    public static void create(Node audit) throws Exception
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("popup-audit-creator.fxml"));
        scene = new Scene(fxmlLoader.load());
        modal = new Stage();

        modal.initModality(Modality.APPLICATION_MODAL);
        modal.setResizable(false);

        modal.setMinWidth(761);
        modal.setMinHeight(868);

        modal.setTitle("CinemApp - Terem felugró");

        scene.getStylesheets().add(Main.class.getResource("css/bootstrap.css").toExternalForm());
        scene.getStylesheets().add(Main.class.getResource("css/main.css").toExternalForm());

        modal.setScene(scene);
        attachAuditorium(audit);

    }

    public static void display() {
        modal.showAndWait();
    }

    public Stage getStage() {
        return modal;
    }

    private static void attachAuditorium(Node audit){
        AnchorPane holder = (AnchorPane) scene.lookup("#holder");

        holder.getChildren().add(audit);
        holder.setTopAnchor(audit, 0.0d);
        holder.setBottomAnchor(audit, 0.0d);
        holder.setLeftAnchor(audit, 0.0d);
        holder.setRightAnchor(audit, 0.0d);

    }
}

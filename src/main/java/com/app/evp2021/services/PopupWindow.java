package com.app.evp2021.services;

import com.app.evp2021.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class PopupWindow {
    private Scene scene = null;
    private Stage window = null;
    private TYPE type = null;
    private int onCloseValue = -1;

    public enum TYPE {PRIMARY, SUCCESS, ERROR, YESNO};

    public PopupWindow(TYPE type, String windowTitle, String message, Object[] reservationInfo) {
        this.type = type;
        switch (type){
            case PRIMARY:
                createPrimaryWindow(windowTitle, message, reservationInfo);
                break;
            case SUCCESS:
                createSuccessWindow(windowTitle, message);
                break;
            case ERROR:
                createErrorWindow(windowTitle, message);
                break;
            case YESNO:
                createYesNoWindow(windowTitle, message);
                break;
        }
    }

    private void createPrimaryWindow(String windowTitle, String message, Object[] reservationInfo) {
        loadFXML("primary-popup.fxml");

        window.initModality(Modality.APPLICATION_MODAL);
        window.setResizable(false);
        window.setAlwaysOnTop(true);
        window.setMinWidth(400);
        window.setMinHeight(200);

        window.setTitle("CinemApp - " + windowTitle);

        setMessage(message);

        Button okButton = (Button) scene.lookup("#btn_ok");
        okButton.setOnAction(e -> closeWindow());
    }

    private void createSuccessWindow(String windowTitle, String message) {
        loadFXML("success-popup.fxml");

        window.initModality(Modality.APPLICATION_MODAL);
        window.setResizable(false);
        window.setAlwaysOnTop(true);

        window.setTitle("CinemApp - " + windowTitle);

        setMessage(message);

        Button okButton = (Button) scene.lookup("#btn_ok");
        okButton.setOnAction(e -> closeWindow());
    }

    private void createErrorWindow(String windowTitle, String message) {
        loadFXML("error-popup.fxml");

        window.initModality(Modality.APPLICATION_MODAL);
        window.setResizable(false);
        window.setAlwaysOnTop(true);

        window.setTitle("CinemApp - " + windowTitle);

        setMessage(message);

        Button okButton = (Button) scene.lookup("#btn_ok");
        okButton.setOnAction(e -> closeWindow());
    }

    private void createYesNoWindow(String windowTitle, String message) {
        loadFXML("YesNo-popup.fxml");

        window.initModality(Modality.APPLICATION_MODAL);
        window.setResizable(false);
        window.setAlwaysOnTop(true);

        window.setTitle("CinemApp - " + windowTitle);

        setMessage(message);

        Button yesButton = (Button) scene.lookup("#btn_Yes");
        yesButton.setOnAction(e -> { onCloseValue = 1; closeWindow(); });

        Button noButton = (Button) scene.lookup("#btn_No");
        noButton.setOnAction(e -> { onCloseValue = 0; closeWindow(); });
    }

    private void setMessage(String message) {
        Text messageField = (Text) scene.lookup("#message");
        messageField.setText(message);
    }

    private void loadFXML(String fxmlFileName){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("popups/" + fxmlFileName));
            scene = new Scene(fxmlLoader.load());
            window = new Stage();
            window.setScene(scene);

            scene.getStylesheets().add(Main.class.getResource("css/bootstrap.css").toExternalForm());
            scene.getStylesheets().add(Main.class.getResource("css/main.css").toExternalForm());
        }catch (IOException err) {
            err.printStackTrace();
        }
    }

    public int displayWindow()
    {
        window.showAndWait();

        return onCloseValue;
    }

    public void closeWindow() {
        if(window != null) {
            window.close();
        }
    }
}

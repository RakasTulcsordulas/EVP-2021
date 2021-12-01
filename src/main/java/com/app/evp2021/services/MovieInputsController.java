package com.app.evp2021.services;

import com.app.sql.MySQLConnect;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.sql.SQLException;

public class MovieInputsController {
    private final VBox inputHolder;
    private Object[] inputFields;
    /**
     0-title, 1-directors, 2-cast, 3-description, 4-rating, 5-auditorium, 6-duration, 7-screeningboxes
     8-screeningdrops,
     */
    private final Text inputSectionTitle;
    private final Button confirmButton;
    private final Button deleteButton;
    private final Label screenOnceText;
    private final CheckBox screenOnceCheckBox;

    public MovieInputsController(VBox inputHolder, Object[] inputFields, Text inputSectionTitle, Button confirmButton, Button deleteButton, Label screenOnceText, CheckBox screenOnceCheckBox) {
        this.inputHolder = inputHolder;
        this.inputFields = inputFields;
        this.inputSectionTitle = inputSectionTitle;
        this.confirmButton = confirmButton;
        this.deleteButton = deleteButton;
        this.screenOnceText = screenOnceText;
        this.screenOnceCheckBox = screenOnceCheckBox;
    }

    private Tooltip setTooltip(Node node, String text) {
        Tooltip tooltip = new Tooltip();
        tooltip.setShowDelay(Duration.millis(100));
        tooltip.setPrefWidth(300);
        tooltip.setWrapText(true);
        tooltip.setText(text);
        tooltip.setStyle("-fx-font-size: 12px;");
        Tooltip.install(node,tooltip);

        return tooltip;
    }

    private void removeTooltip(Node node, Tooltip tooltip) {
        Tooltip.uninstall(node, tooltip);
    }

    private void showInputFields(boolean toggle){
        inputHolder.setVisible(toggle);
    }

    private void resetInputFields() {
        ((TextField)inputFields[0]).setText("");
        ((TextField)inputFields[1]).setText("");
        ((TextField)inputFields[2]).setText("");
        ((TextField)inputFields[3]).setText("");
        ((TextField)inputFields[6]).setText("");
        setAuditoriumsInDropdown(null);
        setRatingsInDropdown(null);
        resetScreeningInputs();
        resetErrors();
    }

    private void setAuditoriumsInDropdown(Object auditorium){
        ((ChoiceBox)inputFields[5]).getItems().clear();

        MySQLConnect dbConnection = new MySQLConnect();
        try {
            dbConnection.establishConnection();
        }catch (SQLException err){
            err.printStackTrace();
        }

        Object[][] resultAuditoriums = dbConnection.getAuditorium(null);

        dbConnection.closeConnection();

        for(int i = 1; i < resultAuditoriums.length; i++) {
            ((ChoiceBox)inputFields[5]).getItems().add(resultAuditoriums[i][2]);

            if(auditorium != null && resultAuditoriums[i][1] == auditorium) {
                ((ChoiceBox)inputFields[5]).setValue(resultAuditoriums[i][2]);
            }
        }
    }

    private void setRatingsInDropdown(Object rating) {
        ((ChoiceBox)inputFields[4]).getItems().clear();

        ((ChoiceBox)inputFields[4]).getItems().add(6);
        ((ChoiceBox)inputFields[4]).getItems().add(12);
        ((ChoiceBox)inputFields[4]).getItems().add(16);
        ((ChoiceBox)inputFields[4]).getItems().add(18);

        if(rating != null) {
            ((ChoiceBox)inputFields[4]).setValue(rating);
        }else{
            ((ChoiceBox)inputFields[4]).setValue(6);
        }
    }

    public void addNewMovie() {
        showInputFields(true);
        resetInputFields();
        setUpInputFields();
    }

    private void setUpInputFields() {
        setTooltip(screenOnceText, "Ez a funkció hasznos ha egy filmet csakis EGYSZER szeretnénk játszani, a jelőlő négyzeteknél egyet bepipálva adhatjuk meg mikor legyen játszva a film, ha már ay a nap elmúlt, a következő hátre fog csúszni. Ha több van bejelölve, akkor is csak egy időpont lesz kiválasztva.");

        setRatingsInDropdown(null);
        setAuditoriumsInDropdown(null);

        inputSectionTitle.setText("Új film hozzáadása");

        confirmButton.setStyle("-fx-cursor: hand;");
        deleteButton.setStyle("-fx-cursor: none;");
    }
}

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
    private TextField movieTitleInput;
    private TextField movieDirectorInput;
    private TextField movieCastInput;
    private TextArea movieDescriptionInput;
    private ChoiceBox movieRatingDropdown;
    private ChoiceBox movieAuditoriumDropdown;
    private TextField movieDurationInput;
    private CheckBox[] movieScreeningBoxes;
    private ChoiceBox[] movieScreeningTimesDropdowns;

    /**
     0-title, 1-directors, 2-cast, 3-description, 4-rating, 5-auditorium, 6-duration, 7-screeningboxes
     8-screeningdrops,
     */
    private final Text inputSectionTitle;
    private final Button confirmButton;
    private final Button deleteButton;
    private final Label screenOnceText;
    private final CheckBox screenOnceCheckBox;

    public TextField getMovieTitleInput() {
        return movieTitleInput;
    }

    public TextField getMovieDirectorInput() {
        return movieDirectorInput;
    }

    public TextField getMovieCastInput() {
        return movieCastInput;
    }

    public TextArea getMovieDescriptionInput() {
        return movieDescriptionInput;
    }

    public ChoiceBox getMovieRatingDropdown() {
        return movieRatingDropdown;
    }

    public ChoiceBox getMovieAuditoriumDropdown() {
        return movieAuditoriumDropdown;
    }

    public TextField getMovieDurationInput() {
        return movieDurationInput;
    }

    public CheckBox[] getMovieScreeningBoxes() {
        return movieScreeningBoxes;
    }

    public ChoiceBox[] getMovieScreeningTimesDropdowns() {
        return movieScreeningTimesDropdowns;
    }

    public Label getScreenOnceText() {
        return screenOnceText;
    }

    public CheckBox getScreenOnceCheckBox() {
        return screenOnceCheckBox;
    }

    public MovieInputsController(VBox inputHolder, Object[] inputFields, Text inputSectionTitle, Button confirmButton, Button deleteButton, Label screenOnceText, CheckBox screenOnceCheckBox) {
        this.inputHolder = inputHolder;
        this.movieTitleInput = (TextField) inputFields[0];
        this.movieDirectorInput = (TextField) inputFields[1];
        this.movieCastInput = (TextField) inputFields[2];
        this.movieDescriptionInput = (TextArea) inputFields[3];
        this.movieRatingDropdown = (ChoiceBox) inputFields[4];
        this.movieAuditoriumDropdown = (ChoiceBox) inputFields[5];
        this.movieDurationInput = (TextField) inputFields[6];
        this.inputSectionTitle = inputSectionTitle;
        this.confirmButton = confirmButton;
        this.deleteButton = deleteButton;
        this.screenOnceText = screenOnceText;
        this.screenOnceCheckBox = screenOnceCheckBox;

        this.movieScreeningBoxes = (CheckBox[]) inputFields[7];
        this.movieScreeningTimesDropdowns = (ChoiceBox[]) inputFields[8];
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

    public void closeInputFields(){
        showInputFields(false);
        resetInputFields();
    }

    private void resetInputFields() {
        movieTitleInput.setText("");
        movieDirectorInput.setText("");
        movieCastInput.setText("");
        movieDescriptionInput.setText("");
        movieDurationInput.setText("");
        setAuditoriumsInDropdown(null);
        setRatingsInDropdown(null);
        resetScreeningInputs();
        resetErrors();
    }

    private void setAuditoriumsInDropdown(Object auditorium){
        movieAuditoriumDropdown.getItems().clear();

        MySQLConnect dbConnection = new MySQLConnect();
        try {
            dbConnection.establishConnection();
        }catch (SQLException err){
            err.printStackTrace();
        }

        Object[][] resultAuditoriums = dbConnection.getAuditorium(null);

        dbConnection.closeConnection();

        for(int i = 1; i < resultAuditoriums.length; i++) {
            movieAuditoriumDropdown.getItems().add(resultAuditoriums[i][2]);

            if(auditorium != null && resultAuditoriums[i][1] == auditorium) {
                movieAuditoriumDropdown.setValue(resultAuditoriums[i][2]);
            }
        }
    }

    private void setRatingsInDropdown(Object rating) {
        movieRatingDropdown.getItems().clear();

        movieRatingDropdown.getItems().add(6);
        movieRatingDropdown.getItems().add(12);
        movieRatingDropdown.getItems().add(16);
        movieRatingDropdown.getItems().add(18);

        if(rating != null) {
            movieRatingDropdown.setValue(rating);
        }else{
            movieRatingDropdown.setValue(6);
        }
    }

    public void addNewMovie() {
        showInputFields(true);
        deleteButton.setDisable(true);
        resetInputFields();
        setUpInputFields();
    }

    public void editExistingMovie(Object[] resultMovie){
        showInputFields(true);
        resetInputFields();
        deleteButton.setDisable(false);
        setUpInputs(resultMovie[2], resultMovie[3], resultMovie[4], resultMovie[5], resultMovie[6], null, resultMovie[7], null, false);
    }

    private void setUpInputFields() {
        setTooltip(screenOnceText, "Ez a funkció hasznos ha egy filmet csakis EGYSZER szeretnénk játszani, a jelőlő négyzeteknél egyet bepipálva adhatjuk meg mikor legyen játszva a film, ha már ay a nap elmúlt, a következő hátre fog csúszni. Ha több van bejelölve, akkor is csak egy időpont lesz kiválasztva.");

        setRatingsInDropdown(null);
        setAuditoriumsInDropdown(null);

        inputSectionTitle.setText("Új film hozzáadása");

        confirmButton.setStyle("-fx-cursor: hand;");
        deleteButton.setStyle("-fx-cursor: none;");
    }

    public void setUpInputs(Object movieTitle, Object movieDirectors, Object movieCast, Object movieDescription, Object movieRating, Object movieAuditorium, Object movieDuration, Object[] movieScreening, boolean screeningOnlyOnce) {
        setTooltip(screenOnceText, "Ez a funkció hasznos ha egy filmet csakis EGYSZER szeretnénk játszani, a jelőlő négyzeteknél egyet bepipálva adhatjuk meg mikor legyen játszva a film, ha már ay a nap elmúlt, a következő hátre fog csúszni. Ha több van bejelölve, akkor is csak egy időpont lesz kiválasztva.");
        inputSectionTitle.setText(movieTitle + " című film szerkesztése");

        setRatingsInDropdown(movieRating);

        movieTitleInput.setText(movieTitle.toString());
        movieDirectorInput.setText((movieDirectors != null) ? movieDirectors.toString() : "");
        movieCastInput.setText((movieCast != null) ? movieCast.toString() : "");
        movieDescriptionInput.setText((movieDescription != null) ? movieDescription.toString() : "");
        movieDurationInput.setText((movieDuration != null) ? movieDuration.toString() : "");

        setAuditoriumsInDropdown(movieAuditorium);
    }

    private void resetErrors() {
        removeErrorClass(movieTitleInput);
        removeErrorClass(movieAuditoriumDropdown);
        removeErrorClass(movieDurationInput);

        Tooltip tooltip = new Tooltip();
        removeTooltip(movieTitleInput, tooltip);
        removeTooltip(movieAuditoriumDropdown, tooltip);
        removeTooltip(movieDurationInput, tooltip);
    }

    private void resetScreeningInputs() {
        Tooltip t[] = new Tooltip[7];
        for(int i = 0; i < 7; i++) {
            removeErrorClass(movieScreeningBoxes[i]);
            removeTooltip(movieScreeningBoxes[i], t[i]);
            removeErrorClass(movieScreeningTimesDropdowns[i]);
            removeTooltip(movieScreeningTimesDropdowns[i], t[i]);

            movieScreeningBoxes[i].setSelected(false);
            movieScreeningTimesDropdowns[i].setValue(null);
            movieScreeningTimesDropdowns[i].getItems().clear();
        }
    }

    private void setErrorClass(Node node){
        if(node instanceof TextField && !(node.getStyleClass().contains("input-error"))){
            node.getStyleClass().add("input-error");
        }else if((node instanceof ChoiceBox || node instanceof CheckBox ) && !(node.getStyleClass().contains("choice-error"))){
            node.getStyleClass().add("choice-error");
        }
    }

    private void removeErrorClass(Node node) {
        if(node instanceof TextField && (node.getStyleClass().contains("input-error"))){
            node.getStyleClass().remove("input-error");
        }else if((node instanceof ChoiceBox || node instanceof CheckBox ) && (node.getStyleClass().contains("choice-error"))){
            node.getStyleClass().remove("choice-error");
        }
    }

    /**
     INPUT FIELD VALIDATIONS
     */
    private boolean checkInputs() {
        boolean errorFound = true;
        Tooltip t1 = null, t2 = null, t3 = null;

        if(movieTitleInput.getText().isEmpty()) {
            setErrorClass(movieTitleInput);
            t1 = setTooltip(movieTitleInput, "Ezt a mezőt kötelező kitölteni!");
            errorFound = false;
        }else{
           removeErrorClass(movieTitleInput);
           if(t1 != null){ removeTooltip(movieTitleInput, t1); }
        }

        if(isScreeningSelected()){
            if(movieAuditoriumDropdown.getValue() == null) {
                setErrorClass(movieAuditoriumDropdown);
                t2 = setTooltip(movieAuditoriumDropdown, "Ezt a mezőt kötelező kitölteni!");
                errorFound = false;
            }else{
                removeErrorClass(movieAuditoriumDropdown);
                if(t2 != null) { removeTooltip(movieAuditoriumDropdown, t2); }
            }
        }


        if(movieDurationInput.getText().isEmpty()) {
            setErrorClass(movieDurationInput);
            t3 = setTooltip(movieDurationInput, "Ezt a mezőt kötelező kitölteni!");
            errorFound = false;
        }else{
            try{
                int dur = Integer.parseInt(movieDurationInput.getText());

                if(dur < 10 || dur > 250) {
                    setErrorClass(movieDurationInput);
                    t3 = setTooltip(movieDurationInput, "10 és 250 között kell lennie a hossznak!");
                    errorFound = false;
                }else{

                    removeErrorClass(movieDurationInput);
                    if(t3 != null) { removeTooltip(movieDurationInput, t3); }
                }

            }catch (NumberFormatException err) {
                t3 = setTooltip(movieDurationInput, "Számot kell megadni film hossznak.");
                setErrorClass(movieDurationInput);
                errorFound = false;
            }
        }

        return errorFound;
    }

    private boolean isScreeningSelected() {
        for(int i = 0; i < movieScreeningBoxes.length; i++) {
            if(movieScreeningBoxes[i].isSelected()) {
                return true;
            }
        }
        return false;
    }

    private boolean isScreeningTimeSelected() {
        for(int i = 0; i < movieScreeningTimesDropdowns.length; i++) {
            if(movieScreeningTimesDropdowns[i].getValue() != null) {
                return true;
            }
        }
        return false;
    }

    private boolean checkScreeningInput() {
        boolean errorFound = true;
        Tooltip t[] = new Tooltip[7];

        for(int i = 0; i < 7; i++) {
            if(isScreeningSelected() || isScreeningTimeSelected()){
                if(movieScreeningBoxes[i].isSelected() && movieScreeningTimesDropdowns[i].getValue() == null){
                    setErrorClass(movieScreeningTimesDropdowns[i]);
                    t[i] = setTooltip(movieScreeningTimesDropdowns[i], "Adott naphoz kötelező megjelőlni kezdődátumot.");
                    errorFound = false;
                }else if(movieScreeningTimesDropdowns[i].getValue() != null && !movieScreeningBoxes[i].isSelected()) {
                    setErrorClass(movieScreeningBoxes[i]);
                    t[i] = setTooltip(movieScreeningBoxes[i], "Adott dátumhoz kötelező kiválasztani a napot is!");
                    errorFound = false;
                }
            }else{
                removeErrorClass(movieScreeningBoxes[i]);
                removeTooltip(movieScreeningBoxes[i], t[i]);
                removeErrorClass(movieScreeningTimesDropdowns[i]);
                removeTooltip(movieScreeningTimesDropdowns[i], t[i]);
            }
        }

        return errorFound;
    }

    public Object[] validateInputFields() {
        if(checkInputs() && checkScreeningInput()){
            return new Object[]{
                    (movieTitleInput.getText().isEmpty()) ? null : movieTitleInput.getText(),
                    (movieDirectorInput.getText().isEmpty()) ? null : movieDirectorInput.getText(),
                    (movieCastInput.getText().isEmpty()) ? null : movieCastInput.getText(),
                    (movieDescriptionInput.getText().isEmpty()) ? null : movieDescriptionInput.getText(),
                    (movieRatingDropdown.getValue() == null) ? null : movieRatingDropdown.getValue().toString(),
                    (movieAuditoriumDropdown.getValue() == null) ? null : movieAuditoriumDropdown.getValue().toString(),
                    (movieDurationInput.getText().isEmpty()) ? null : movieDurationInput.getText(),
                    (screenOnceCheckBox.isSelected())
            };
        }else return null;
    }
}

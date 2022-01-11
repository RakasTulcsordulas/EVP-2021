package com.app.evp2021.services;


import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Controls the inputs for movies.
 */
public class MovieInputsController {
    private final VBox inputHolder;
    private TextField movieTitleInput;
    private TextField movieDirectorInput;
    private TextField movieCastInput;
    private TextArea movieDescriptionInput;
    private ChoiceBox movieRatingDropdown;
    private TextField movieDurationInput;

    /**
     0-title, 1-directors, 2-cast, 3-description, 4-rating, 5-duration
     */
    private final Text inputSectionTitle;
    private final Button confirmButton;
    private final Button deleteButton;
    /**
     * Setter of the movie input controller.
     * @param inputHolder View box, contains the elements.
     * @param inputFields An array of objects.
     * @param inputSectionTitle Text input.
     * @param confirmButton Button of confirmation.
     * @param deleteButton Button of deleting.
     */
    public MovieInputsController(VBox inputHolder, Object[] inputFields, Text inputSectionTitle, Button confirmButton, Button deleteButton) {
        this.inputHolder = inputHolder;
        this.movieTitleInput = (TextField) inputFields[0];
        this.movieDirectorInput = (TextField) inputFields[1];
        this.movieCastInput = (TextField) inputFields[2];
        this.movieDescriptionInput = (TextArea) inputFields[3];
        this.movieRatingDropdown = (ChoiceBox) inputFields[4];
        this.movieDurationInput = (TextField) inputFields[5];
        this.inputSectionTitle = inputSectionTitle;
        this.confirmButton = confirmButton;
        this.deleteButton = deleteButton;

    }
    /**
     * Setter of the Tooltip.
     * @param node View box, contains the elements.
     * @param text String of texts.
     */
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
    /**
     * Destructor of the Tooltip.
     * @param node View box, contains the elements.
     * @param tooltip An array of objects.
     */
    private void removeTooltip(Node node, Tooltip tooltip) {
        Tooltip.uninstall(node, tooltip);
    }
    /**
     * Sets the visibility of the inputHolder.
     * @param toggle Boolean.
     */
    private void showInputFields(boolean toggle){
        inputHolder.setVisible(toggle);
    }
    /**
     * Close input fields.
     */
    public void closeInputFields(){
        showInputFields(false);
        resetInputFields();
    }
    /**
     * Resets the input fields.
     */
    private void resetInputFields() {
        movieTitleInput.setText("");
        movieDirectorInput.setText("");
        movieCastInput.setText("");
        movieDescriptionInput.setText("");
        movieDurationInput.setText("");
        setRatingsInDropdown(null);
        resetErrors();
    }
    /**
     * Sets the rating of the movies.
     * @param rating Array of the ratings.
     */
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
    /**
     * Adds new movies.
     */
    public void addNewMovie() {
        showInputFields(true);
        deleteButton.setDisable(true);
        resetInputFields();
        setUpInputFields();
    }
    /**
     * Editing existing movies.
     * @param resultMovie Array of the result movies.
     */
    public void editExistingMovie(Object[] resultMovie){
        showInputFields(true);
        resetInputFields();
        deleteButton.setDisable(false);
        setUpInputs(resultMovie[2], resultMovie[3], resultMovie[4], resultMovie[5], resultMovie[6], resultMovie[7]);
    }
    /**
     * Adds a new movie and initialize dropdown field.
     */
    private void setUpInputFields() {
        setRatingsInDropdown(null);

        inputSectionTitle.setText("Új film hozzáadása");

        confirmButton.setStyle("-fx-cursor: hand;");
        deleteButton.setStyle("-fx-cursor: none;");
    }
    /**
     * Editing existing movie and setup input fields from database.
     * @param movieTitle Movies' title.
     * @param movieDirectors Movies' directors.
     * @param movieCast Movies' casts.
     * @param movieDescription Movies' descriptions.
     * @param movieRating Movies' ratings.
     * @param movieDuration Movies' durations.
     */
    public void setUpInputs(Object movieTitle, Object movieDirectors, Object movieCast, Object movieDescription, Object movieRating, Object movieDuration) {
        inputSectionTitle.setText(movieTitle + " című film szerkesztése");

        setRatingsInDropdown(movieRating);

        movieTitleInput.setText(movieTitle.toString());
        movieDirectorInput.setText((movieDirectors != null) ? movieDirectors.toString() : "");
        movieCastInput.setText((movieCast != null) ? movieCast.toString() : "");
        movieDescriptionInput.setText((movieDescription != null) ? movieDescription.toString() : "");
        movieDurationInput.setText((movieDuration != null) ? movieDuration.toString() : "");
    }
    /**
     * Resetting all the errors.
     */
    private void resetErrors() {
        removeErrorClass(movieTitleInput);
        removeErrorClass(movieDurationInput);

        Tooltip tooltip = new Tooltip();
        removeTooltip(movieTitleInput, tooltip);
        removeTooltip(movieDurationInput, tooltip);
    }
    /**
     * Adds an Error class to the node.
     * @param node UI element.
     */
    private void setErrorClass(Node node){
        if(node instanceof TextField && !(node.getStyleClass().contains("input-error"))){
            node.getStyleClass().add("input-error");
        }else if((node instanceof ChoiceBox || node instanceof CheckBox ) && !(node.getStyleClass().contains("choice-error"))){
            node.getStyleClass().add("choice-error");
        }
    }
    /**
     * Removes an Error class to the node.
     * @param node UI element.
     */
    private void removeErrorClass(Node node) {
        if(node instanceof TextField && (node.getStyleClass().contains("input-error"))){
            node.getStyleClass().remove("input-error");
        }else if((node instanceof ChoiceBox || node instanceof CheckBox ) && (node.getStyleClass().contains("choice-error"))){
            node.getStyleClass().remove("choice-error");
        }
    }

    /**
     * Input fields validations
     * @return errorFound True/False
     */
    private boolean checkInputs() {
        boolean errorFound = true;
        Tooltip t1 = null, t2 = null;

        if(movieTitleInput.getText().isEmpty()) {
            setErrorClass(movieTitleInput);
            t1 = setTooltip(movieTitleInput, "Ezt a mezőt kötelező kitölteni!");
            errorFound = false;
        }else{
           removeErrorClass(movieTitleInput);
           if(t1 != null){ removeTooltip(movieTitleInput, t1); }
        }


        if(movieDurationInput.getText().isEmpty()) {
            setErrorClass(movieDurationInput);
            t2 = setTooltip(movieDurationInput, "Ezt a mezőt kötelező kitölteni!");
            errorFound = false;
        }else{
            try{
                int dur = Integer.parseInt(movieDurationInput.getText());

                if(dur < 10 || dur > 250) {
                    setErrorClass(movieDurationInput);
                    t2 = setTooltip(movieDurationInput, "10 és 250 között kell lennie a hossznak!");
                    errorFound = false;
                }else{

                    removeErrorClass(movieDurationInput);
                    if(t2 != null) { removeTooltip(movieDurationInput, t2); }
                }

            }catch (NumberFormatException err) {
                t2 = setTooltip(movieDurationInput, "Számot kell megadni film hossznak.");
                setErrorClass(movieDurationInput);
                errorFound = false;
            }
        }

        return errorFound;
    }
    /**
     * Gives back the database ready object.
     */
    public Object[] validateInputFields() {
        if(checkInputs()){
                Object[] returnValues;

                returnValues = new Object[]{
                        (movieTitleInput.getText().isEmpty()) ? null : movieTitleInput.getText(),
                        (movieDirectorInput.getText().isEmpty()) ? null : movieDirectorInput.getText(),
                        (movieCastInput.getText().isEmpty()) ? null : movieCastInput.getText(),
                        (movieDescriptionInput.getText().isEmpty()) ? null : movieDescriptionInput.getText(),
                        (movieRatingDropdown.getValue() == null) ? null : movieRatingDropdown.getValue().toString(),
                        (movieDurationInput.getText().isEmpty()) ? null : movieDurationInput.getText()
                };

                return returnValues;
        }else return null;
    }
}

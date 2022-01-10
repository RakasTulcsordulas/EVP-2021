package com.app.evp2021.controllers;

import com.app.sql.MySQLConnect;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Screening window controller.
 */
public class ScreeningController {
    @FXML AnchorPane screening;
    @FXML Button next_btn;
    @FXML Button back_btn;
    @FXML Circle circle1, circle2, circle3, circle4;
    private Circle[] circles;
    @FXML Text text1, text2, text3, text4;
    private Text[] texts;
    @FXML Line line1, line2, line3;
    private Line[] lines;
    @FXML VBox step1_box, step2_box, step3_box, step4_box;
    @FXML VBox step1_movie_list;
    @FXML DatePicker step2_date_picker;
    @FXML VBox step2_picker_holder;
    @FXML VBox step3_time_list;
    @FXML Text step4_auditorium, step4_title, step4_date, step4_time;

    private int selectedMovieId = -1;
    private int selectedMovieNodeId = -1;
    private LocalDate selectedDate;
    private int stepCount = 1;

    private int selectedAuditoriumId = 0;

    private String selectedTime = "";
    private Stage parentStage;
    /**
     * Next button = Step forward
     * @param event
     */
    @FXML void onNextButtonClicked(MouseEvent event) {
        if(this.stepCount <= 4) {
            this.stepCount++;
        }

        setStep(this.stepCount);
    }
    /**
     * Back button = Step backward
     * @param event
     */
    @FXML void onBackButtonClicked(MouseEvent event) {
        if(this.stepCount >= 2 && this.stepCount <= 4) {
            this.stepCount--;
        }

        setStep(this.stepCount);
    }
    /**
     * Hide all the boxes.
     */
    private void hideAllBox(){
        step1_box.setVisible(false);
        step2_box.setVisible(false);
        step3_box.setVisible(false);
        step4_box.setVisible(false);
    }
    /**
     * Initialize before showing the window.
     * @param auditoriumId Id of the auditorium.
     * @param parentStage Parent of the window stage.
     */
    public void initialize(int auditoriumId, Stage parentStage) {
        this.circles = new Circle[]{circle1, circle2, circle3, circle4};
        this.lines = new Line[]{line1, line2, line3};
        this.texts = new Text[]{text1, text2, text3, text4};
        this.selectedAuditoriumId = auditoriumId;
        this.parentStage = parentStage;
        setStep(this.stepCount);
    }
    /**
     * Calls the corresponding function based on the argument.
     * @param i number of the xth step
     */
    private void setStep(int i) {
        switch (i) {
            case 1:
                this.step1();
                break;
            case 2:
                this.step2();
                break;
            case 3:
                this.step3();
                break;
            case 4:
                this.step4();
                break;
            case 5:
                this.saveScreening();
                break;
        }
    }
    /**
     * Saves the screening of an auditorium.
     */
    private void saveScreening() {
        try{
            MySQLConnect dbConnection = new MySQLConnect();
            dbConnection.establishConnection();

            dbConnection.insertScreening(this.selectedMovieId, this.selectedAuditoriumId, Timestamp.valueOf(this.selectedDate.toString() + " " + this.selectedTime + ":00"));
            dbConnection.closeConnection();
        }catch (SQLException error) {
            error.printStackTrace();
        }

        parentStage.close();
    }
    /**
     * Switch between next and finish button.
     * @param toggle True / False
     */
    private void setButtonToFinish(boolean toggle){
        if(toggle) {
            next_btn.setDisable(false);
            next_btn.setText("Befejezés");
            next_btn.getStyleClass().add("btn-success");
        }else{
            next_btn.setDisable(false);
            next_btn.setText("Tovább");
            next_btn.getStyleClass().removeAll("btn-success");
            next_btn.getStyleClass().add("btn-primary");
        }
    }
    /**
     * Shows summary of the selected data.
     */
    private void step4() {
        this.setButtonToFinish(true);
        fillToStep(4);
        this.hideAllBox();
        step4_box.setVisible(true);

        try {
            MySQLConnect dbConnection = new MySQLConnect();
            dbConnection.establishConnection();
            Object[][] movie = dbConnection.getMovie(this.selectedMovieId, null, null);
            step4_title.setText((String) movie[1][2]);

            String auditoriumName = dbConnection.getAuditorium(this.selectedAuditoriumId)[1][2].toString();

            step4_auditorium.setText(auditoriumName);

            dbConnection.closeConnection();
        }catch (SQLException error) {
            error.printStackTrace();
        }

        step4_date.setText(this.selectedDate.toString());
        step4_time.setText(this.selectedTime);
    }
    /**
     * Generates date times to the movies based on previous screenings.
     */
    private void step3(){
        this.setButtonToFinish(false);
        fillToStep(3);
        next_btn.setDisable(true);
        this.hideAllBox();
        step3_box.setVisible(true);

        Timestamp selectedDateTs = java.sql.Timestamp.valueOf(this.selectedDate.toString() + " 00:00:00");
        try{
            MySQLConnect dbConnection = new MySQLConnect();
            dbConnection.establishConnection();

            Object[][] resultScreenings = dbConnection.getScreening(null,null, this.selectedAuditoriumId, selectedDateTs, null);
            if(resultScreenings.length > 1) {

                this.generateIdealSchedule(resultScreenings);

            }else{
                this.generateEmptySchedule();
            }
        }catch(SQLException error) {
            error.printStackTrace();
        }

        if(this.selectedTime != "") {
            setSelectedTime(this.selectedTime);
        }
    }
    /**
     * Generates the date times for the movies.
     */
    private void generateIdealSchedule(Object[][] resultScreenings) {
        MySQLConnect dbCon = new MySQLConnect();
        try{
            dbCon.establishConnection();
            Object[][] selectedMovie = dbCon.getMovie(this.selectedMovieId, null, null);

            LocalTime time = LocalTime.of(10,00);

            for(int i = 1; i < resultScreenings.length; i++){
                LocalTime startOfMovie = LocalTime.of(
                        Integer.parseInt(resultScreenings[i][4].toString().split(" ")[1].split(":")[0]),
                        Integer.parseInt(resultScreenings[i][4].toString().split(" ")[1].split(":")[1]));
                while(time.isBefore(startOfMovie)) {
                    if(time.plusMinutes((Integer) selectedMovie[1][7] + 15).isBefore(startOfMovie)){
                        this.addTimeToSchedule(time.toString());
                    }
                    time = time.plusMinutes(30);
                }

                Object[][] movie = dbCon.getMovie(resultScreenings[i][2], null, null);
                time = time.plusMinutes((Integer) movie[1][7] + 10);
            }
            if(time.isBefore(LocalTime.of(22,30))) {
                while (time.isBefore(LocalTime.of(22,30))){
                    this.addTimeToSchedule(time.toString());
                    time = time.plusMinutes(30);
                }
            }
        }catch(SQLException error) {
            error.printStackTrace();
        }
    }
    /**
     * Generates the date times for the movies at each 30 min.
     */
    private void generateEmptySchedule() {
        if(step3_time_list.getChildren().size() == 26) {
            step3_time_list.getChildren().clear();
        }

        int halfHourHelper = 0;

        for (double i = 10.0; i < 23.0; i = i + 0.5) {
            String buttonString;
            if(halfHourHelper == 0) {
                buttonString = "" + Math.round(Math.floor(i)) + ":00";
                halfHourHelper = 1;
            }else{
                halfHourHelper = 0;
                buttonString = "" + Math.round(Math.floor(i)) + ":30";
            }

            Button timeButton = new Button(buttonString);
            timeButton.getStyleClass().add("btn");
            timeButton.getStyleClass().add("btn-primary");
            timeButton.setStyle("-fx-cursor: hand;");
            timeButton.setCursor(Cursor.HAND);
            timeButton.setPrefWidth(250.0);

            timeButton.setOnMouseClicked(event -> {
                setSelectedTime(buttonString);
            });
            step3_time_list.getChildren().add(timeButton);

        }

        if(this.selectedTime != "") {
            this.setSelectedTime(this.selectedTime);
        }
    }
    /**
     * Adds a button to the scroll pane in step3.
     */
    private void addTimeToSchedule(String time) {
        Button timeButton = new Button(time);
        timeButton.getStyleClass().add("btn");
        timeButton.getStyleClass().add("btn-primary");
        timeButton.setStyle("-fx-cursor: hand;");
        timeButton.setCursor(Cursor.HAND);
        timeButton.setPrefWidth(250.0);

        timeButton.setOnMouseClicked(event -> {
            setSelectedTime(time);
        });
        step3_time_list.getChildren().add(timeButton);
    }
    /**
     * Generates date picker.
     */
    private void step2() {
        this.setButtonToFinish(false);
        fillToStep(2);
        next_btn.setDisable(true);
        back_btn.setDisable(false);
        this.hideAllBox();
        step2_box.setVisible(true);

        step2_date_picker.setShowWeekNumbers(false);

        DatePickerSkin datePickerSkin = new DatePickerSkin(step2_date_picker);
        step2_picker_holder.getChildren().clear();
        step2_picker_holder.getChildren().add(datePickerSkin.getPopupContent());
        screening.getChildren().remove(step2_date_picker);

        datePickerSkin.getPopupContent().setOnMouseClicked(event -> {
            LocalDate currentDate = LocalDate.now();
            if(step2_date_picker.getValue().compareTo(currentDate) >= 0) {
                this.selectedDate = step2_date_picker.getValue();
                next_btn.setDisable(false);
            }else{
                next_btn.setDisable(true);
            }

        });

        if(this.selectedDate != null) {
            step2_date_picker.setValue(this.selectedDate);
            next_btn.setDisable(false);
        }
    }
    /**
     * List all movies.
     */
    private void step1() {
        this.setButtonToFinish(false);
        fillToStep(1);
        Object[][] movies = null;
        this.hideAllBox();
        step1_box.setVisible(true);
        back_btn.setDisable(true);

        step1_movie_list.getChildren().clear();

        try{
            MySQLConnect dbConnection = new MySQLConnect();

            dbConnection.establishConnection();

            movies = dbConnection.getMovie(null,null,null);

        }catch (SQLException error) {
            error.printStackTrace();
        }

        for(int i = 1; i < movies.length; i++) {
            Button movieButton = new Button(movies[i][2].toString());
            movieButton.getStyleClass().add("btn");
            movieButton.getStyleClass().add("btn-primary");
            movieButton.setStyle("-fx-cursor: hand;");
            movieButton.setCursor(Cursor.HAND);
            movieButton.setPrefWidth(250.0);

            int index = i;
            Object[] movie = movies[i];
            movieButton.setOnMouseClicked(event -> {
                setSelectedMovie(index-1, (int) movie[1]);
            });
            step1_movie_list.getChildren().add(movieButton);

        }

        if(this.selectedMovieId != -1 && this.selectedMovieNodeId != -1) {
            setSelectedMovie(this.selectedMovieNodeId, this.selectedMovieId);
        }
    }
    /**
     * Saves selected movies.
     * @param nodeId Index of the children element in scroll pane.
     * @param movieId Id of the movies.
     */
    private void setSelectedMovie(int nodeId, int movieId) {
        if(this.selectedMovieNodeId != -1 && this.selectedMovieId != -1) {
            Button oldButton = (Button) step1_movie_list.getChildren().get(this.selectedMovieNodeId);
            oldButton.getStyleClass().removeAll("btn-danger");
        }


        this.selectedMovieNodeId = nodeId;
        this.selectedMovieId = movieId;

        Button movieButton = (Button) step1_movie_list.getChildren().get(nodeId);
        movieButton.getStyleClass().add("btn-danger");

        if(next_btn.isDisabled() && (this.selectedMovieNodeId != -1 && this.selectedMovieId != -1)) {
            next_btn.setDisable(false);
        }
    }
    /**
     * Saves selected time.
     * @param time Date time of the movies.
     */
    private void setSelectedTime(String time) {
        if(this.selectedTime != "") {
            for(Node element : step3_time_list.getChildren()){
                element.getStyleClass().removeAll("btn-danger");
            }
        }


        this.selectedTime = time;

        for(Node element : step3_time_list.getChildren()){
            Button button = (Button) element;
            if(button.getText().equals(this.selectedTime)) {
                element.getStyleClass().add("btn-danger");
            }

        }

        if(next_btn.isDisabled() && this.selectedTime != "") {
            next_btn.setDisable(false);
        }
    }
    /**
     * Indicates the amount of steps.
     * @param step Step.
     */
    private void fillToStep(int step) {
        Color primary = Color.web("#337ab7");

        for(int i = 0; i < 4; i++) {
            circles[i].setFill(Color.WHITE);
            texts[i].setFill(Color.BLACK);
            if(i < 3) {
                lines[i].setStroke(Color.WHITE);
            }
        }

        for(int i = 0; i < step; i++) {
            circles[i].setFill(primary);
            texts[i].setFill(Color.WHITE);

            if(i >= 1) {
                lines[i-1].setStroke(primary);
            }
        }

    }
}

package com.app.evp2021.controllers;

import com.app.sql.MySQLConnect;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ScreeningSummaryController {

    @FXML
    HBox screening_date_holder_hbox;
    @FXML
    TabPane screening_tabpane;

    public void initialize() {
        screening_date_holder_hbox.setFocusTraversable(false);
        screening_tabpane.setFocusTraversable(false);
        try{
            MySQLConnect dbConnection = new MySQLConnect();
            dbConnection.establishConnection();

            Object[][] screenings = dbConnection.getScreening(null,null,null,null);
            List<String> dates = new ArrayList<>();
            for(int i = 1; i < screenings.length; i++) {

                String date = screenings[i][4].toString().split(" ")[0];
                if(!dates.contains(date)) {
                    dates.add(date);
                    Button dateButton = new Button(date);
                    dateButton.getStyleClass().add("btn");
                    dateButton.getStyleClass().add("btn-primary");
                    dateButton.setStyle("-fx-cursor: hand;");
                    dateButton.setCursor(Cursor.HAND);
                    dateButton.setFocusTraversable(false);
                    dateButton.setPrefWidth(250.0);

                    dateButton.setOnMouseClicked(event -> {
                        this.setSelectedDate(date);
                        this.generateTabsBasedOnDate(date);
                    });

                    screening_date_holder_hbox.getChildren().add(dateButton);

                }
                dbConnection.closeConnection();
            }
        }catch (SQLException error) {
            error.printStackTrace();
        }
    }

    private void generateTabsBasedOnDate(String date) {
        screening_tabpane.getTabs().clear();
        try{
            MySQLConnect dbConnection = new MySQLConnect();
            dbConnection.establishConnection();

            Object[][] specificScreening = dbConnection.getScreening(null, null, null, Timestamp.valueOf(date + " 00:00:00"));
            List<String> auditoriums = new ArrayList<>();
            for(int i = 1; i < specificScreening.length; i++) {
                String auditorium = specificScreening[i][3].toString();

                if(!auditoriums.contains(auditorium)) {
                    auditoriums.add(auditorium);
                    GridPane screeningGrid = new GridPane();
                    screeningGrid.setHgap(20);
                    screeningGrid.setVgap(20);
                    AnchorPane tabBody = new AnchorPane(screeningGrid);
                    tabBody.setStyle("-fx-background-color: #e1e1e1");

                    tabBody.setTopAnchor(screeningGrid, 10.0);
                    tabBody.setLeftAnchor(screeningGrid, 10.0);
                    tabBody.setRightAnchor(screeningGrid, 10.0);
                    tabBody.setBottomAnchor(screeningGrid, 10.0);


                    this.listScreenings(date, Integer.parseInt(auditorium), screeningGrid);

                    String auditoriumName = dbConnection.getAuditorium(auditorium)[1][2].toString();

                    Tab tab = new Tab(auditoriumName, tabBody);
                    tab.setStyle("-fx-focus-color: transparent;");
                    tab.getContent().setFocusTraversable(false);
                    
                    screening_tabpane.setFocusTraversable(false);
                    screening_tabpane.getTabs().add(tab);
                }

            }
        }catch(SQLException error){
            error.printStackTrace();
        }
    }

    private void listScreenings(String date, int auditoriumId, GridPane grid) {
        try {
            MySQLConnect dbConnection = new MySQLConnect();
            dbConnection.establishConnection();

            Object[][] screenings = dbConnection.getScreening(null, null, auditoriumId, Timestamp.valueOf(date + " 00:00:00"));
            List<String> movies = new ArrayList<>();

            for (int i = 1; i < screenings.length; i++) {
                Object[][] movieData = dbConnection.getMovie(screenings[i][2], null, null);
                if(!movies.contains(screenings[i][2].toString())) {
                    movies.add(screenings[i][2].toString());
                    Text movieName = new Text(movieData[1][2].toString());
                    movieName.setStyle("-fx-font-size: 22; -fx-font-weight: 700;");

                    grid.add(movieName, 0,i,1,1);
                }
                LocalTime screeningFrom = LocalTime.parse(screenings[i][4].toString().split(" ")[1].split(":")[0] + ":" +
                        screenings[i][4].toString().split(" ")[1].split(":")[1]);
                LocalTime screeningTo = screeningFrom.plusMinutes((int) movieData[1][7]);

                Text screeningFromTo = new Text(screeningFrom.toString() + " - " + screeningTo.toString());
                screeningFromTo.setStyle("-fx-font-weight: 700;");
                screeningFromTo.setStyle("-fx-font-size: 16;");
                grid.add(screeningFromTo, i, movies.indexOf(screenings[i][2].toString())+1, 1,1);


            }
            dbConnection.closeConnection();
        }catch(SQLException error) {
            error.printStackTrace();
        }

    }

    private void setSelectedDate(String date) {
        for(Node element : screening_date_holder_hbox.getChildren()){
            element.getStyleClass().removeAll("btn-danger");
        }


        for(Node element : screening_date_holder_hbox.getChildren()){
            Button button = (Button) element;
            if(button.getText().equals(date)) {
                element.getStyleClass().add("btn-danger");
            }

        }
    }
}

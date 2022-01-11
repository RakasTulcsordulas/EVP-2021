package com.app.evp2021.controllers;

import com.app.evp2021.Main;
import com.app.evp2021.services.PopupWindow;
import com.app.evp2021.services.UserSession;
import com.app.evp2021.views.ScreeningSetup;
import com.app.sql.MySQLConnect;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Controls the auditoriums.
 */
public class AuditoriumController {


    @FXML private AnchorPane seat_holder;
    @FXML private Text movie_title;


    @FXML private GridPane rootGridPane = null;
    @FXML private AnchorPane auditorium;
    @FXML private Button audit_btn;
    @FXML private Button audit_btn1;
    @FXML private String title = null;

    @FXML private int _button_action = 0;

    @FXML private HBox legends;

    @FXML private Stage parentStage;

    @FXML private Object[] actionParams;

    @FXML private Text reserved_text;

    @FXML private List<String> reservedSeats = new ArrayList<>();


    /**
     * It generates auditorium rooms where you can reserve seats.
     * @param addSeatClass It adds seats to the auditorium.
     * @param startHidden All seats starts hidden as default.
     */
    void createAuditorium(boolean addSeatClass, boolean startHidden) {
        rootGridPane = new GridPane();
        rootGridPane.setHgap(12);
        rootGridPane.setVgap(12);

        audit_btn.setCursor(Cursor.HAND);

        for (int row = 0; row < 18; row++) {
            for (int col = 0; col < 19; col ++) {

                Label seatText = new Label();
                seatText.setWrapText(true);

                StackPane seatPane = new StackPane();
                seatPane.getChildren().add(seatText);
                StackPane.setAlignment(seatText, Pos.CENTER);
                seatPane.setMaxHeight(50);
                seatPane.setMaxWidth(50);

                if(col == 0) {
                    seatText.setText(""+(row+1));
                    seatPane.setCursor(Cursor.NONE);
                }else{
                    if(col <= 9) {
                        seatText.setText((9-col)+1 + "b");
                    }else{
                        seatText.setText(col-9 + "j");
                    }
                    if(addSeatClass){
                        seatPane.getStyleClass().add("seat");
                    }

                    if(!startHidden) {
                        seatPane.getStyleClass().add("text-white");
                        addClass(seatPane, "btn-success");
                    }else{
                        seatPane.setVisible(false);
                    }

                }

                int seatIndex = ((row)*19)+col;
                int finalRow = row+1;
                int finalCol = col;
                seatPane.setOnMouseClicked(event ->  {
                    paneClick(seatIndex, finalRow, finalCol);
                });

                rootGridPane.add(seatPane, col, row);
            }
        }
        for (int col = 0; col < 19; col++) {
            rootGridPane.getColumnConstraints().add(new ColumnConstraints(25, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, HPos.CENTER, true));

        }
        for (int row = 0; row < 18; row++) {
            rootGridPane.getRowConstraints().add(new RowConstraints(25, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, VPos.CENTER, true));
        }
        seat_holder.getChildren().add(rootGridPane);

        seat_holder.setLeftAnchor(rootGridPane, 0.0);
        seat_holder.setRightAnchor(rootGridPane, 0.0);
        seat_holder.setTopAnchor(rootGridPane, 0.0);
        seat_holder.setBottomAnchor(rootGridPane, 0.0);
    }
    /**
     * Setter of the parent stage.
     * @param parentStage It adds seats to the auditorium.
     */
    void setParentStage(Stage parentStage) {
        this.parentStage = parentStage;
    }
    /**
     * It generates auditorium rooms' seats into an array.
     * @param seats It adds seats to the auditorium.
     */
    void setAllSeat(Object[][] seats)  {
        for(int i = 1; i < seats.length; i++) {
            setSeat((Integer) seats[i][2], (Integer) seats[i][3], 0);
        }
    }

    /**
     * Set all seats based on reservation.
     * @param seats Array of seats.
     * @param screeningId Object of IDs.
     */
    void setAllSeatBasedOnreservation(Object[][] seats, Object screeningId) {
        Object[][] reservedSeats = null;
        try{
            MySQLConnect dbConn = new MySQLConnect();
            dbConn.establishConnection();

           reservedSeats = dbConn.getSeatReserved(null, null, screeningId);
        }catch (SQLException error) {
            error.printStackTrace();
        }

        for(int i = 1; i < seats.length; i++) {
            if(reservedSeats.length > 1) {
                for(int j = 1; j < reservedSeats.length; j++) {
                    int seatId = (int) seats[i][1];
                    int reservedSeatId = (int) reservedSeats[j][2];
                    if(seatId == reservedSeatId) {
                        setSeat((Integer) seats[i][2], (Integer) seats[i][3], 1);
                    }
                    else{
                        setSeat((Integer) seats[i][2], (Integer) seats[i][3], 0);
                    }
                }
            }else{
                setSeat((Integer) seats[i][2], (Integer) seats[i][3], 0);
            }
        }
    }

    /**
     * It generates auditorium rooms' seats into an array.
     * @param index It adds seats to the auditorium.
     * @param row Seat rows of the auditorium(s).
     * @param col Seat column of the auditorium(s).
     */
    private void paneClick(int index, int row, int col) {
        if(_button_action == 1) {
            try {
                StackPane seatPane = (StackPane) rootGridPane.getChildren().get(index);
                if(hasClass(seatPane, "btn-success")){
                    setSeat(index, 2);
                }else{
                    setSeat(index, 0);
                }
            }catch (Exception err){
                System.out.println(err);
            }
        }else if(_button_action == 0) {
            try {
                StackPane seatPane = (StackPane) rootGridPane.getChildren().get(index);
                if(!hasClass(seatPane, "btn-warning") && !hasClass(seatPane, "btn-danger")){
                    reservedSeats.add(row + ";" + col);
                    setSeat(index, 3);
                }else{
                    reservedSeats.remove(row + ";" + col);
                    setSeat(index, 4);
                }

                if(reservedSeats.size() >= 1) {
                    audit_btn.setDisable(false);
                }else{
                    audit_btn.setDisable(true);
                }
            }catch (Exception err){
                System.out.println(err);
            }
        }
    }
    /**
     * Sets the visibility of the legends.
     * @param t Boolean type, Visibility.
     */
    void toggleLegend(boolean t) {
        legends.setVisible(t);
    }
    /**
     * Sets the title of the movie.
     * @param s Movie title.
     */
    void setTitle(String s){
        movie_title.setText(s);
    }

    /**
     * Gives back a seat's number (row and col).
     * @param row Seat rows of the auditorium(s).
     * @param col Seat column of the auditorium(s).
     * @return Returns the inner text of the seat label with the given parameters.
     */
    private String getSeatLabel(int row, int col) {
        int index = ((row-1)*19)+col;
        StackPane seatPane = (StackPane) rootGridPane.getChildren().get(index);
        Label seatLabel = (Label) seatPane.getChildren().get(0);
        return seatLabel.getText();
    }
    /**
     * Sets the rows and columns of the auditorium(s).
     * @param row Seat rows of the auditorium(s).
     * @param col Seat column of the auditorium(s).
     * @param status Checks the reservation status of the seats.
     */
    void setSeat(int row, int col, int status) {
        if(row <= 0 || col <= 0)
        {
            System.err.println("0-nál nagyobb szám elvárt!");
        }
        else {
            int index = ((row - 1) * 19) + col;
            StackPane seatPane = (StackPane) rootGridPane.getChildren().get(index);

            switch (status) {
                case -1:
                    seatPane.setVisible(false);
                    seatPane.getStyleClass().removeAll();
                    addClass(seatPane, "seat");
                    break;
                case 0:
                    seatPane.setVisible(true);
                    addClass(seatPane, "btn-success");
                    seatPane.setCursor(Cursor.HAND);
                    break;
                case 1:
                    seatPane.setVisible(true);
                    addClass(seatPane, "btn-danger");
                    break;
                case 2:
                    seatPane.setVisible(true);
                    removeClass(seatPane, "btn-success");
                    break;
                case 3:
                    seatPane.setVisible(true);
                    seatPane.getStyleClass().removeAll();
                    addClass(seatPane, "btn-warning");
                    seatPane.setCursor(Cursor.HAND);
                    break;
                case 4:
                    seatPane.setVisible(true);
                    removeClass(seatPane, "btn-warning");
                    seatPane.setCursor(Cursor.HAND);
                    break;
            }
        }
    }
    /**
     * Sets the title of the movie.
     * @param index Determines the number of seats for each auditorium.
     * @param status Checks the reservation status of the seats.
     */
    void setSeat(int index, int status){
        if(index <= 0 || index > 342){
            System.err.println("0-nál nagyobb szám elvárt!");
        }
        else {
            StackPane seatPane = (StackPane) rootGridPane.getChildren().get(index);

            switch (status) {
                case -1:
                    seatPane.setVisible(false);
                    seatPane.getStyleClass().removeAll();
                    addClass(seatPane, "seat");
                    break;
                case 0:
                    seatPane.setVisible(true);
                    addClass(seatPane, "btn-success");
                    seatPane.setCursor(Cursor.HAND);
                    break;
                case 1:
                    seatPane.setVisible(true);
                    addClass(seatPane, "btn-danger");
                    break;
                case 2:
                    seatPane.setVisible(true);
                    removeClass(seatPane, "btn-success");
                    break;
                case 3:
                    seatPane.setVisible(true);
                    addClass(seatPane, "btn-warning");
                    seatPane.setCursor(Cursor.HAND);
                    break;
                case 4:
                    seatPane.setVisible(true);
                    removeClass(seatPane, "btn-warning");
                    seatPane.setCursor(Cursor.HAND);
                    break;
            }
        }
    }
    /**
     * Creates a Node, if it's not existing.
     * @param node View box, contains the elements.
     * @param className The name of the Class to be added.
     */
    private void addClass(Node node, String className){
        if(!node.getStyleClass().contains(className)){
            node.getStyleClass().add(className);
        }
    }
    /**
     * Removes a Node, if it's existing.
     * @param node View box, contains the elements.
     * @param className The name of the Class to be added.
     */
    private void removeClass(Node node, String className){
        if(node.getStyleClass().contains(className)){
            node.getStyleClass().remove(className);
        }
    }
    /**
     * Returns the type of the class.
     * @param node View box, contains the elements.
     * @param className The name of the Class to be added.
     * @return Returns true node if the Node contains the given classname.
     */
    private boolean hasClass(Node node, String className){
        return node.getStyleClass().contains(className);
    }
    /**
     * Sets the button texts in the auditorium(s).
     * @param text String of the button(s).
     */
    void setButtonText(String text){
        audit_btn.setText(text);
    }

    /**
     * Sets the text of the SecondButton.
     * @param text Button's text.
     */
    void setSecondButtonText(String text) { audit_btn1.setText(text); }

    /**
     * Sets the SecondButton's visibility.
     * @param toggle True/False
     */
    void setSecondButtonVisible(boolean toggle) { audit_btn1.setVisible(toggle); }

    /**
     * Sets the SecondButton's visibility.
     * @param b True/False
     */
    void showSelf(boolean b){
        auditorium.setVisible(b);
    }
    /**
     * Sets the buttons' types.
     * @param actionId Integer.
     */
    void setActionButtonType(int actionId) {
        switch (actionId) {
            case 0: _button_action = 0; break;
            case 1: //terem letrohzas
                _button_action = 1;
                audit_btn.setDisable(false);
                break;
            case 2: //terem torles
                _button_action = 2;
                audit_btn.setDisable(false);
                removeClass(audit_btn, "btn-primary");
                addClass(audit_btn, "btn-danger");
                break;
        }
    }
    /**
     * Sets the buttons' types.
     */
    @FXML
    void onActionButtonClicked() {
        if(_button_action == 0){
            String reservationInfo = "Székek: \n";
            for(int i = 0; i < reservedSeats.size(); i++) {
                int row = Integer.parseInt(reservedSeats.get(i).split(";")[0]);
                int col = Integer.parseInt(reservedSeats.get(i).split(";")[1]);
                reservationInfo = reservationInfo  + "["+ row + ".sor " + getSeatLabel(row, col) + "]; ";
            }

            PopupWindow confirmWindow =
                    new PopupWindow(PopupWindow.TYPE.YESNO, "Foglalás megerősítése",
                            "Szeretnéd biztosan lefoglalni az alábbi székeket?\n\n" + reservationInfo, null);

            int confirm = confirmWindow.displayWindow();
            if(confirm == 1) {
                try {
                    MySQLConnect dbConnection = new MySQLConnect();
                    dbConnection.establishConnection();
                    String generatedToken = generateReservationToken();
                    int idOfReservation = dbConnection.insertReservation(actionParams[0], null, generatedToken);

                    if(idOfReservation != -1) {
                        for(int i = 0; i < reservedSeats.size(); i++) {
                            int row = Integer.parseInt(reservedSeats.get(i).split(";")[0]);
                            int col = Integer.parseInt(reservedSeats.get(i).split(";")[1]);
                            Object[][] seat = dbConnection.getSeat(null, row, col, actionParams[1]);
                            dbConnection.insertSeatReserved(seat[1][1], idOfReservation, actionParams[0]);
                        }

                        PopupWindow successWindow =
                                new PopupWindow(PopupWindow.TYPE.SUCCESS, "Foglalás sikeres!",
                                        "Foglalásod sikeresen végbement! Kérlek az alábbi azonosítót hozd magaddal a film kezdete előtt:\n\n" + generatedToken, null);
                        parentStage.close();

                        successWindow.displayWindow();
                    }
                }catch (SQLException error) {
                    error.printStackTrace();
                }
            }

        }else if(_button_action == 1) {
            MySQLConnect dbConnection = null;
            try{
                dbConnection = new MySQLConnect();
                dbConnection.establishConnection();
            }catch (SQLException err){
                err.printStackTrace();
            }

            int numberOfAuditoriums = dbConnection.getAuditorium(null).length;
            int insertId = dbConnection.insertAuditorium("T" + numberOfAuditoriums, 18*19);

            for (int row = 0; row < 18; row++) {
                for (int col = 0; col < 19; col ++) {

                    if(col > 0) {
                        int index = ((row)*19)+col;
                        StackPane seatPane = (StackPane) rootGridPane.getChildren().get(index);
                        if(hasClass(seatPane, "btn-success")){
                            dbConnection.insertSeat(row+1, col, insertId);
                        }
                    }
                }
            }

            dbConnection.closeConnection();
            audit_btn.setDisable(true);
            parentStage.close();

        }else if(_button_action == 2 && actionParams[0] != null) {
            MySQLConnect dbConnection = null;
            try{
                dbConnection = new MySQLConnect();
                dbConnection.establishConnection();
            }catch (SQLException err){
                err.printStackTrace();
            }

            dbConnection.deleteSeats((Integer) actionParams[0]);
            dbConnection.deleteAuditorium((Integer) actionParams[0]);

            dbConnection.closeConnection();
            audit_btn.setDisable(true);
            parentStage.close();
        }
    }

    /**
     * Creates a token for each Reservation.
     * @return returnString, generated token.
     */
    private String generateReservationToken() {
        String returnString = "";
        List<String> finalString = new ArrayList<>();
        List<String> letterUpper =
                new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
                        "R", "S", "T", "U", "V", "W", "X", "Y", "Z"));
        List<String> letterLower = new ArrayList<>();
        for(String letter : letterUpper) {
            letterLower.add(letter.toLowerCase());
        }
        List<String> numbers =  new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9"));

        letterUpper.addAll(letterLower);
        letterUpper.addAll(numbers);
        finalString.addAll(letterUpper);

        Collections.shuffle(finalString);
        for(int i = 10; i <= 25; i++) {
            returnString += finalString.get(i);
        }

        return returnString;
    }

    @FXML
    /**
     * Creates a new window with a button.
     */
    void onSecondButtonClicked() {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("screening-setup-view.fxml"));
        AnchorPane rootAnchorPane = null;
        try {
            rootAnchorPane = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ScreeningSetup.createWindow(rootAnchorPane);
        ScreeningController Controller = fxmlLoader.getController();

        Controller.initialize((Integer) actionParams[0], ScreeningSetup.getStage());

        ScreeningSetup.display();
    }

    /**
     * Sets the action of a button.
     * @param params Sets the actionParams to the auditorium controller.
     */
    public void setActionButtonParams(Object[] params) {
        this.actionParams = params;
    }
}

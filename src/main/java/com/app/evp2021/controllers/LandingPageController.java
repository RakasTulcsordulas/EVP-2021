package com.app.evp2021.controllers;

import com.app.evp2021.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import com.app.sql.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

public class LandingPageController{


    @FXML private Text current_date;
    @FXML private DatePicker date_picker;
    @FXML private ScrollPane movie_scroll;
    @FXML private void onActionPerformed(ActionEvent event) {
        current_date.setText("Kiválasztott dátum: " + date_picker.getValue().toString());
    }

    @FXML private void listMovies() {
        MySQLConnect con = new MySQLConnect();
        con.establishConnection();
        String time = date_picker.getValue().toString() + " 12:00:00";
        Timestamp ts = Timestamp.valueOf("2021-11-04 20:09:23");
        System.out.println(ts);
        Object[] res = con.getScreening(null,null,null,ts);
        System.out.println(res);
    }

    public void initialize() {
        date_picker.setValue(LocalDate.now());
        current_date.setText("Kiválasztott dátum: " + date_picker.getValue().toString());
    }
}

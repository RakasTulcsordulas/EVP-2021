package com.app.evp2021.controllers;

import com.app.evp2021.Main;
import com.app.evp2021.views.LoginModal;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import com.app.sql.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class LandingPageController{


    @FXML private Text current_date;
    @FXML private DatePicker date_picker;
    @FXML private ScrollPane movie_scroll;
    @FXML private VBox movie_list;
    @FXML private void onActionPerformed(ActionEvent event) {
        current_date.setText("Kiválasztott dátum: " + date_picker.getValue().toString());
    }

    @FXML private void listMovies() {
        /*MySQLConnect con = new MySQLConnect();
        con.establishConnection();
        String time = date_picker.getValue().toString() + " 12:00:00";
        Timestamp ts = Timestamp.valueOf("2021-11-04 20:09:23");*/
        movie_list.getChildren().clear();
        String[][] res = {{"Elkurtuk", "Terem1", "2021-11-04 11:09:23"}, {"Dune", "Terem2", "2021-11-04 20:09:23"}, {"Elkurtuk", "Terem1", "2021-11-04 15:09:23"}};
        for(int i=0; i< res.length; i++){
            Button b = new Button(res[i][0] + " | " + res[i][1] + " | " + res[i][2].substring(11,res[i][2].length()));
            b.getStyleClass().add("btn-primary");
            b.setMinWidth(movie_scroll.getWidth()-25);
            movie_list.getChildren().add(b);
        }
        for(int i=0; i< res.length; i++){
            Button b = new Button(res[i][0] + " | " + res[i][1] + " | " + res[i][2].substring(11,res[i][2].length()));
            b.getStyleClass().add("btn-primary");
            b.setMinWidth(movie_scroll.getWidth()-25);
            movie_list.getChildren().add(b);
        }
        for(int i=0; i< res.length; i++){
            Button b = new Button(res[i][0] + " | " + res[i][1] + " | " + res[i][2].substring(11,res[i][2].length()));
            b.getStyleClass().add("btn-primary");
            b.setMinWidth(movie_scroll.getWidth()-25);
            movie_list.getChildren().add(b);
        }
    }

    @FXML
    private void openAdminModal() {
        System.out.println("nyom");
        try {
            LoginModal.display();
        }catch (Exception e){

        }

    }

    public void initialize() {
        date_picker.setValue(LocalDate.now());
        current_date.setText("Kiválasztott dátum: " + date_picker.getValue().toString());
        movie_list.setSpacing(20);
    }
}

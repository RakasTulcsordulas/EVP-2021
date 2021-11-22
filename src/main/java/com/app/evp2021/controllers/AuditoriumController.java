package com.app.evp2021.controllers;

import com.app.evp2021.Main;
import com.app.sql.MySQLConnect;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;


public class AuditoriumController {


    @FXML private AnchorPane seat_holder;
    @FXML private Text movie_title;


    @FXML private GridPane root = null;
    @FXML private AnchorPane auditorium;
    @FXML private Button audit_btn;
    @FXML private String title = null;

    @FXML private int _button_action = 0;

    public void create() {
        root = new GridPane();
        root.setHgap(12);
        root.setVgap(12);

        audit_btn.setCursor(Cursor.HAND);

        for (int row = 0; row < 18; row++) {
            for (int col = 0; col < 19; col ++) {
                Label text = new Label();
                text.setWrapText(true);

                StackPane p = new StackPane();
                p.getChildren().add(text);
                StackPane.setAlignment(text, Pos.CENTER);
                p.setMaxHeight(50);
                p.setMaxWidth(50);

                if(col == 0) {
                    text.setText(""+(row+1));
                    p.setCursor(Cursor.NONE);
                }else{
                    if(col <= 9) {
                        text.setText((9-col)+1 + "b");
                    }else{
                        text.setText(col-9 + "j");
                    }
                    p.getStyleClass().add("seat");
                    addClass(p, "btn-success");
                }

                int index = ((row)*19)+col;
                p.setOnMouseClicked(event ->  {
                    paneClick(index);
                });

                root.add(p, col, row);
            }
        }
        for (int i = 0; i < 19; i++) {
            root.getColumnConstraints().add(new ColumnConstraints(25, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, HPos.CENTER, true));

        }
        for (int i = 0; i < 18; i++) {
            root.getRowConstraints().add(new RowConstraints(25, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, VPos.CENTER, true));
        }
        seat_holder.getChildren().add(root);

        seat_holder.setLeftAnchor(root, 0.0);
        seat_holder.setRightAnchor(root, 0.0);
        seat_holder.setTopAnchor(root, 0.0);
        seat_holder.setBottomAnchor(root, 0.0);



        int ti = 18 * 19;
        title = String.valueOf(ti);
        movie_title.setText(title);
    }

    private void paneClick(int index) {
        if(_button_action == 1) {
            try {
                StackPane e = (StackPane) root.getChildren().get(index);
                if(hasClass(e, "btn-success")){
                    setSeat(index, 2);
                }else{
                    setSeat(index, 0);
                }

            }catch (Exception err){
                System.out.println(err);
            }
        }



    }

    public void setTitle(String s){
        movie_title.setText(s);
    }

    public void setSeat(int row, int col, int status) throws Exception{
        if(row <= 0 || col <= 0) throw new Exception("0-nál nagyobb szám elvárt!");

        int index = ((row-1)*19)+col;
        StackPane e = (StackPane) root.getChildren().get(index);
        switch (status){
            case -1:
                e.setVisible(false);
                e.getStyleClass().removeAll();
                addClass(e, "seat");
                break;
            case 0:
                e.setVisible(true);
                addClass(e, "btn-success");
                break;
            case 1:
                e.setVisible(true);
               addClass(e, "btn-danger");
                break;
            case 2:
                e.setVisible(true);
                removeClass(e, "btn-success");
                break;
        }
    }

    public void setSeat(int index, int status) throws Exception{
        if(index <= 0 || index > 342) throw new Exception("0-nál nagyobb szám elvárt!");
        StackPane e = (StackPane) root.getChildren().get(index);
        switch (status){
            case -1:
                e.setVisible(false);
                e.getStyleClass().removeAll();
                addClass(e, "seat");
                break;
            case 0:
                e.setVisible(true);
                addClass(e, "btn-success");
                break;
            case 1:
                e.setVisible(true);
                addClass(e, "btn-danger");
                break;
            case 2:
                e.setVisible(true);
                removeClass(e, "btn-success");
                break;
        }
    }

    private void addClass(Node n, String className){
        if(!n.getStyleClass().contains(className)){
            n.getStyleClass().add(className);
        }
    }

    private void removeClass(Node n, String className){
        if(n.getStyleClass().contains(className)){
            n.getStyleClass().remove(className);
        }
    }

    private boolean hasClass(Node n, String className){
        return n.getStyleClass().contains(className);
    }

    public void setButtonText(String text){
        audit_btn.setText(text);
    }

    public void showSelf(boolean b){
        auditorium.setVisible(b);
    }

    public void setActionButtonType(int i) {
        switch (i) {
            case 0: _button_action = 0; break;
            case 1:
                _button_action = 1;
                audit_btn.setDisable(false);
                break;
        }
    }

    @FXML
    void onActionButtonClicked(MouseEvent event) {
        if(_button_action == 0){
            //reserv
        }else if(_button_action == 1) {
            int num = 1;
            MySQLConnect con = null;
            try{
                con = new MySQLConnect();
                con.establishConnection();
            }catch (SQLException err){}

            int id = con.insertAuditorium("T", 18*19);
            System.out.println(id);

            for (int row = 0; row < 18; row++) {
                for (int col = 0; col < 19; col ++) {

                    if(col > 0) {
                        int index = ((row)*19)+col;
                        StackPane e = (StackPane) root.getChildren().get(index);
                        if(hasClass(e, "btn-success")){
                            Label l = (Label) e.getChildren().get(0);
                            System.out.println("Sor:" + (row+1) + ", Oszlop: " + col + ", Szekszam: "  + l.getText());
                            con.insertSeat(row+1, col, id);
                        }
                    }
                }
            }
            System.out.println("admin ment");
        }
    }
}

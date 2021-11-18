package com.app.evp2021.controllers;

import com.app.evp2021.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Collection;


public class Auditorium {


    @FXML private AnchorPane seat_holder;
    @FXML private Text movie_title;


    @FXML private GridPane root = null;
    @FXML private AnchorPane auditorium;
    private String title = null;

    public void create() {
        root = new GridPane();
        root.setHgap(12);
        root.setVgap(12);


        final int size_row = 18;
        final int size_col = 19;

        for (int row = 0; row < size_row; row++) {
            for (int col = 0; col < size_col; col ++) {
                Label text = new Label();
                text.setWrapText(true);

                text.getStyleClass().add("seat");

                StackPane p = new StackPane();
                p.getChildren().add(text);
                StackPane.setAlignment(text, Pos.CENTER);
                p.setMaxHeight(50);
                p.setMaxWidth(50);

                if(col == 0) {
                    text.setText(""+(row+1));
                    text.setStyle("-fx-color: black");
                }else{
                    if(col <= 9) {
                        text.setText((9-col)+1 + "b");
                    }else{
                        text.setText(col-9 + "j");
                    }
                    text.getStyleClass().add("seat");
                    p.setVisible(false);
                }

                root.add(p, col, row);
            }
        }
        for (int i = 0; i < size_col; i++) {
            root.getColumnConstraints().add(new ColumnConstraints(25, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, HPos.CENTER, true));

        }
        for (int i = 0; i < size_row; i++) {
            root.getRowConstraints().add(new RowConstraints(25, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, VPos.CENTER, true));
        }
        seat_holder.getChildren().add(root);

        seat_holder.setLeftAnchor(root, 0.0);
        seat_holder.setRightAnchor(root, 0.0);
        seat_holder.setTopAnchor(root, 0.0);
        seat_holder.setBottomAnchor(root, 0.0);



        int ti = size_row * size_col;
        title = String.valueOf(ti);
        movie_title.setText(title);
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
                break;
            case 0:
                e.setVisible(true);
                e.getStyleClass().add("btn-success");
                break;
            case 1:
                e.setVisible(true);
                e.getStyleClass().add("btn-danger");
                break;
        }
        Label t = (Label) e.getChildren().get(0);
        System.out.println(t.getText() + " - " + status);
    }

    public void showSelf(boolean b){
        auditorium.setVisible(b);
    }
}

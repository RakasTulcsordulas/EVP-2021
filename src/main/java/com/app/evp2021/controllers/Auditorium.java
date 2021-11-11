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

public class Auditorium extends AnchorPane{

    @FXML private AnchorPane seat_holder;
    @FXML private Text movie_title;

    private static GridPane root = null;
    private String title = null;

    public void create(int _col, int _row) {
        root = new GridPane();
        root.setHgap(12);
        root.setVgap(12);
        final int size_row = _row;
        final int size_col = _col;
        final String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r"};
        for (int row = 0; row < size_row; row++) {
            for (int col = 0; col < size_col; col ++) {
                Label text = new Label();
                text.setWrapText(true);
                text.setText(col+1 + letters[row]);
                text.getStyleClass().add("seat");
                StackPane p = new StackPane();
                p.getChildren().add(text);
                StackPane.setAlignment(text, Pos.CENTER);
                p.setMaxHeight(50);
                p.setMaxWidth(50);
                p.getStyleClass().add("btn-danger");
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
}

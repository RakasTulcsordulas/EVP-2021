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

<<<<<<< Updated upstream
public class Auditorium extends AnchorPane{
=======
public class Auditorium {
>>>>>>> Stashed changes

    @FXML private AnchorPane seat_holder;
    @FXML private Text movie_title;

<<<<<<< Updated upstream
    private static GridPane root = null;
    private String title = null;

=======
    @FXML private GridPane root = null;
    @FXML private AnchorPane auditorium;
    private String title = null;

    public Auditorium() {
        System.out.println("constr");
    }

>>>>>>> Stashed changes
    public void create(int _col, int _row) {
        root = new GridPane();
        root.setHgap(12);
        root.setVgap(12);
<<<<<<< Updated upstream
        final int size_row = _row;
        final int size_col = _col;
=======
        final int size_row = 18;
        final int size_col = 19;
>>>>>>> Stashed changes
        final String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r"};
        for (int row = 0; row < size_row; row++) {
            for (int col = 0; col < size_col; col ++) {
                Label text = new Label();
                text.setWrapText(true);
<<<<<<< Updated upstream
                text.setText(col+1 + letters[row]);
                text.getStyleClass().add("seat");
=======

>>>>>>> Stashed changes
                StackPane p = new StackPane();
                p.getChildren().add(text);
                StackPane.setAlignment(text, Pos.CENTER);
                p.setMaxHeight(50);
                p.setMaxWidth(50);
<<<<<<< Updated upstream
                p.getStyleClass().add("btn-danger");
=======
                p.setId("" + ((row-1)*19)+col);

                if(col == 0) {
                    text.setText(""+(row+1));
                }else{
                    if(col <= 9) {
                        text.setText((9-col)+1 + "b");
                    }else{
                        text.setText(col-9 + "j");
                    }
                    text.getStyleClass().add("seat");
                }
                p.setVisible(false);
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream


        int ti = size_row * size_col;
        title = String.valueOf(ti);
        movie_title.setText(title);
    }
=======
    }

    public void setTitle(String s){
        movie_title.setText(s);
    }

    public void setSeat(int row, int col, int status) throws Exception{
        if(row <= 0 || col <= 0) throw new Exception("0-nál nagyobb szám elvárt!");
        StackPane e = (StackPane) root.lookup("#" + ((row-1)*19)+col);
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


>>>>>>> Stashed changes
}

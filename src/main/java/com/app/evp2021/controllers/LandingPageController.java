package com.app.evp2021.controllers;

import com.app.evp2021.Main;
import com.app.evp2021.services.UserSession;
import com.app.evp2021.views.AuditoriumCreationPopup;
import com.app.evp2021.views.LandingPage;
import com.app.evp2021.views.LoginModal;
import com.app.sql.MySQLConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.sql.SQLException;

public class LandingPageController{
    @FXML private VBox movie_holder;

    @FXML private Text current_date;
    @FXML private DatePicker date_picker;
    @FXML private ScrollPane movie_scroll;
    @FXML private Label once_text;
    @FXML private VBox movie_inputs;
    @FXML private Text input_title;
    @FXML private Button save_movie;
    @FXML private Button delete_movie;
    @FXML private HBox audit_btn_holder;

    @FXML private TextField movie_title_input;
    @FXML private TextField director_input;
    @FXML private TextField cast_input;
    @FXML private TextArea description_input;
    @FXML private ChoiceBox rating_drop;
    @FXML private ChoiceBox movie_room;
    @FXML private TextField duration_input;
    @FXML private CheckBox check_1;
    @FXML private CheckBox check_2;
    @FXML private CheckBox check_3;
    @FXML private CheckBox check_4;
    @FXML private CheckBox check_5;
    @FXML private CheckBox check_6;
    @FXML private CheckBox check_7;
    @FXML private ChoiceBox choice_1;
    @FXML private ChoiceBox choice_2;
    @FXML private ChoiceBox choice_3;
    @FXML private ChoiceBox choice_4;
    @FXML private ChoiceBox choice_5;
    @FXML private ChoiceBox choice_6;
    @FXML private ChoiceBox choice_7;
    private CheckBox[] screening_day_boxes;
    private ChoiceBox[] screening_day_times;
    private boolean new_movie;

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

    private void removeTooltip(Node node, Tooltip tooltip) {
        Tooltip.uninstall(node, tooltip);
    }

    @FXML private void onActionPerformed(ActionEvent event) {
        current_date.setText("Kiválasztott dátum: " + date_picker.getValue().toString());
    }

    @FXML private void listMovies() {
        /**
         * LISTAZAS UTAN, UGYE MEGVAN AZ ADOTT SCREENING ID, MELYIK TEREM, FILM ID, UTANA HA NYOMUNK A FILMRE AKKOR TEREMID ALAPJAN
         * LEKERJUK HANY SZEKES A TEREM, ES HOL VANNAK SZEKEK, BEADJUK A FUGGVENYNEK EGY ARRAYLISTET AMI TARTALMAZ 2 INDEXES OBJECTETKET AMI A SOR, OSZLOPA A SZEKNEK
         * HA MEGVANNAK A SZEKEK, SCREENING ID ALAPJAN LEKERJUK A RESERVATIONOKET ES OTT A setSEAT-el ATRAKJUK FOGLALTRA OKET, MAJD EZEK UTAN MUTATJUK CSAK MEG A TERMET
         */
        /** -------------------------DUMMY DATA--------------------------------------------------------- */
        movie_holder.getChildren().clear();
        String[][] res = {{"Elkurtuk", "Terem1", "2021-11-04 11:09:23"}, {"Dune", "Terem2", "2021-11-04 20:09:23"}, {"Elkurtuk", "Terem1", "2021-11-04 15:09:23"}};
        for(int i=0; i< res.length; i++){
            Button b = new Button(res[i][0] + " | " + res[i][1] + " | " + res[i][2].substring(11,res[i][2].length()));
            b.getStyleClass().add("btn-primary");
            System.out.println(movie_scroll.getWidth()-25);
            b.setMinWidth(movie_scroll.getWidth()-25);
            movie_holder.getChildren().add(b);
        }
        for(int i=0; i< res.length; i++){
            Button b = new Button(res[i][0] + " | " + res[i][1] + " | " + res[i][2].substring(11,res[i][2].length()));
            b.getStyleClass().add("btn-primary");
            b.setMinWidth(movie_scroll.getWidth()-25);
            movie_holder.getChildren().add(b);
        }
        for(int i=0; i< res.length; i++){
            Button b = new Button(res[i][0] + " | " + res[i][1] + " | " + res[i][2].substring(11,res[i][2].length()));
            b.getStyleClass().add("btn-primary");
            b.setMinWidth(movie_scroll.getWidth()-25);
            movie_holder.getChildren().add(b);
        }
        /** -------------------------DUMMY DATA--------------------------------------------------------- */
    }

    @FXML
    private void openAdminModal() {
        try {
            LoginModal.displayWindow();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @FXML
    void logoutAdmin(MouseEvent event) throws Exception{
        UserSession.getSession().logout();
        LandingPage.refresh();
    }


    @FXML
    void addMovie(MouseEvent event) {
        new_movie = true;
        delete_movie.setDisable(true);
        setUpInputs();
    }

    @FXML
    void addNewAuditorium(MouseEvent event) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("audit-view.fxml"));
        AnchorPane rootAnchorPane = fxmlLoader.load();

        AuditoriumController Controller = fxmlLoader.getController();
        Controller.createAuditorium(true, false);
        Controller.setTitle("Terem létrehozás");
        Controller.setButtonText("Terem mentése");
        Controller.setActionButtonType(1);

        AuditoriumCreationPopup popupAuditoriumWindow = new AuditoriumCreationPopup();

        popupAuditoriumWindow.createWindow(rootAnchorPane);

        Controller.setParentStage(popupAuditoriumWindow.getStage());

        popupAuditoriumWindow.display();

        LandingPage.refresh();

    }

    public static void showAuditorium(int id) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("audit-view.fxml"));
        AnchorPane root = fxmlLoader.load();

        AuditoriumController Controller = fxmlLoader.getController();
        Controller.createAuditorium(false, true);
        Controller.setTitle("Terem mutatás");
        Controller.setButtonText("Terem törlése");
        Controller.setActionButtonType(2);
        Controller.setActionButtonParams(new Object[]{id});

        Controller.toggleLegend(false);

        MySQLConnect dbConnection = new MySQLConnect();
        try {
            Object[][] resultSeats = null;

            dbConnection.establishConnection();
            resultSeats = dbConnection.getSeat(null,null,null, id);
            Controller.setAllSeat(resultSeats);

        } catch (SQLException err) {
            err.printStackTrace();
        }

        AuditoriumCreationPopup popupAuditoriumWindow = new AuditoriumCreationPopup();
        popupAuditoriumWindow.createWindow(root);
        Controller.setParentStage(popupAuditoriumWindow.getStage());
        popupAuditoriumWindow.display();

        LandingPage.refresh();

    }

    public void setUpInputs(Object movie_title, Object directors, Object cast, Object desc, Object rating, Object auditroom, Object[] screening, boolean one_screening) {
        setTooltip(once_text, "Ez a funkció hasznos ha egy filmet csakis EGYSZER szeretnénk játszani, a jelőlő négyzeteknél egyet bepipálva adhatjuk meg mikor legyen játszva a film, ha már ay a nap elmúlt, a következő hátre fog csúszni. Ha több van bejelölve, akkor is csak egy időpont lesz kiválasztva.");
        input_title.setText(movie_title + " című film szerkesztése");
        rating_drop.getItems().add(6);
        rating_drop.getItems().add(12);
        rating_drop.getItems().add(16);
        rating_drop.getItems().add(18);

        rating_drop.setValue(rating);

        movie_title_input.setText(movie_title.toString());
        director_input.setText((directors != null) ? directors.toString() : "");
        cast_input.setText((cast != null) ? cast.toString() : "");
        description_input.setText((desc != null) ? desc.toString() : "");
        //auditorium names must include the number of the room, not enough to put index after when listing
        //--> when creating an auditorium get the number of the existing ones and then automatically add the +1 to the name
    }

    public void setUpInputs() {

        setTooltip(once_text, "Ez a funkció hasznos ha egy filmet csakis EGYSZER szeretnénk játszani, a jelőlő négyzeteknél egyet bepipálva adhatjuk meg mikor legyen játszva a film, ha már ay a nap elmúlt, a következő hátre fog csúszni. Ha több van bejelölve, akkor is csak egy időpont lesz kiválasztva.");

        rating_drop.getItems().add(6);
        rating_drop.getItems().add(12);
        rating_drop.getItems().add(16);
        rating_drop.getItems().add(18);

        rating_drop.setValue(6);

        movie_inputs.setVisible(true);
        input_title.setText("Új film hozzáadása");

        save_movie.setStyle("-fx-cursor: hand;");
        delete_movie.setStyle("-fx-cursor: none;");

        screening_day_boxes = new CheckBox[]{check_1, check_2, check_3, check_4, check_5, check_6, check_7};
        screening_day_times = new ChoiceBox[]{choice_1, choice_2, choice_3, choice_4, choice_5, choice_6, choice_7};
        MySQLConnect dbConnection = new MySQLConnect();
        try {
            dbConnection.establishConnection();
        }catch (SQLException err){
            err.printStackTrace();
        }

        Object[][] resultAuditoriums = dbConnection.getAuditorium(null);

        dbConnection.closeConnection();

        for(int i = 1; i < resultAuditoriums.length; i++) {
            movie_room.getItems().add(resultAuditoriums[i][2] + "" + i);
        }
    }

    @FXML
    void deleteMovie(MouseEvent event) {

    }

    @FXML
    void closeInputs(MouseEvent event) {
        resetInputs();
        movie_inputs.setVisible(false);
    }

    public boolean checkInputs() {
        boolean errorFound = true;
        Tooltip t1 = null, t2 = null, t3 = null;

        if(movie_title_input.getText().isEmpty()) {
            movie_title_input.getStyleClass().add("input-error");
            setErrorClass(movie_title_input);
            t1 = setTooltip(movie_title_input, "Ezt a mezőt kötelező kitölteni!");
            errorFound = false;
        }else{
           removeErrorClass(movie_title_input);
            if(t1 != null){ removeTooltip(movie_title_input, t1); }
        }

        if(isScreeningSelected()){
            if(movie_room.getValue() == null) {
                setErrorClass(movie_room);
                t2 = setTooltip(movie_room, "Ezt a mezőt kötelező kitölteni!");
                errorFound = false;
            }else{
                removeErrorClass(movie_room);
                if(t2 != null) { removeTooltip(movie_room, t2); }
            }
        }


        if(duration_input.getText().isEmpty()) {
            setErrorClass(duration_input);
            t3 = setTooltip(duration_input, "Ezt a mezőt kötelező kitölteni!");
            errorFound = false;
        }else{
            try{
                int dur = Integer.parseInt(duration_input.getText());

                if(dur < 10 || dur > 250) {
                    setErrorClass(duration_input);
                    t3 = setTooltip(duration_input, "10 és 250 között kell lennie a hossznak!");
                    errorFound = false;
                }else{

                    removeErrorClass(duration_input);
                    if(t3 != null) { removeTooltip(duration_input, t3); }
                }

            }catch (NumberFormatException err) {
                t3 = setTooltip(duration_input, "Számot kell megadni film hossznak.");
                setErrorClass(duration_input);
                errorFound = false;
            }
        }

        return errorFound;
    }

    private boolean isScreeningSelected() {
        for(int i = 0; i < screening_day_boxes.length; i++) {
            if(screening_day_boxes[i].isSelected()) return true;
        }
        return false;
    }

    public void setErrorClass(Node node){
        if(node instanceof TextField && !(node.getStyleClass().contains("input-error"))){
            node.getStyleClass().add("input-error");
        }else if((node instanceof ChoiceBox || node instanceof CheckBox ) && !(node.getStyleClass().contains("choice-error"))){
            node.getStyleClass().add("choice-error");
        }
    }

    public void removeErrorClass(Node node) {
        if(node instanceof TextField && (node.getStyleClass().contains("input-error"))){
            node.getStyleClass().remove("input-error");
        }else if((node instanceof ChoiceBox || node instanceof CheckBox ) && (node.getStyleClass().contains("choice-error"))){
            node.getStyleClass().remove("choice-error");
        }
    }

    public boolean checkScreeningInput() {
        boolean errorFound = true;
        Tooltip t[] = new Tooltip[7];

        for(int i = 0; i < 7; i++) {
            removeErrorClass(screening_day_boxes[i]);
            removeTooltip(screening_day_boxes[i], t[i]);
            
            if(screening_day_boxes[i].isSelected() && screening_day_times[i].getValue() == null){
                setErrorClass(screening_day_times[i]);
                t[i] = setTooltip(screening_day_times[i], "Adott naphoz kötelező megjelőlni kezdődátumot.");
                errorFound = false;
            }else if(screening_day_times[i].getValue() != null && !screening_day_boxes[i].isSelected()) {
                setErrorClass(screening_day_times[i]);
                t[i] = setTooltip(screening_day_times[i], "Adott dátumhoz kötelező kiválasztani a napot is!");
                errorFound = false;
            }
        }

        return errorFound;
    }

    private void resetInputs() {
        movie_title_input.setText("");
        director_input.setText("");
        cast_input.setText("");
        description_input.setText("");
        duration_input.setText("");
        movie_room.setValue(null);
    }
    @FXML
    void saveMovie(MouseEvent event) throws Exception {
        if(new_movie){
            boolean inputValid = checkInputs();
            boolean boxesValid =  checkScreeningInput();
            if(inputValid && boxesValid){
                MySQLConnect dbConnection = new MySQLConnect();
                try{
                    dbConnection.establishConnection();
                }catch (SQLException err) {
                    err.printStackTrace();
                }

                dbConnection.insertNewMovie(
                        movie_title_input.getText(),
                        (director_input.getText().isEmpty()) ? null : director_input.getText(),
                        (cast_input.getText().isEmpty()) ? null : cast_input.getText(),
                        (description_input.getText().isEmpty()) ? null : description_input.getText(),
                        rating_drop.getValue().toString(),
                        duration_input.getText()
                );

                movie_inputs.setVisible(false);
                resetInputs();
                LandingPage.refresh();
            }
        }else{
            //edited movie save
        }
    }

    public void setMoviesOnAdminView() {
        Object[][] resultMovies = null;
        MySQLConnect dbConnection = new MySQLConnect();
        try {
            dbConnection.establishConnection();
            resultMovies = dbConnection.getMovie(null, null, null);

            for(int i = 1; i < resultMovies.length; i++){

                Button movieButton = new Button(resultMovies[i][2].toString());
                movieButton.getStyleClass().add("btn");
                movieButton.getStyleClass().add("btn-primary");
                movieButton.setStyle("-fx-cursor: hand;");
                movieButton.setPrefWidth(286.0);


                movie_holder.getChildren().add(movieButton);
                movie_holder.setFillWidth(true);
                movie_holder.setSpacing(20.0);

                final int rowIndex = i;
                final Object[][] movie = resultMovies;

                movieButton.setOnMouseClicked(event -> {
                    try {
                        movie_inputs.setVisible(true);
                        setUpInputs(movie[rowIndex][2], movie[rowIndex][3], movie[rowIndex][4], movie[rowIndex][5], movie[rowIndex][6], movie[rowIndex][7],null, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }catch (SQLException err){
            err.printStackTrace();
        }catch (Exception err) {
            err.printStackTrace();
        }
    }

    public void setAuditoriumsOnAdminView() {
        Object[][] resultAuditoriums = null;
        MySQLConnect dbConnection = new MySQLConnect();
        try {
            dbConnection.establishConnection();
            resultAuditoriums = dbConnection.getAuditorium(null);
            for(int i = 1; i < resultAuditoriums.length; i++){

                Button auditoriumButton = new Button(resultAuditoriums[i][2].toString());

                auditoriumButton.getStyleClass().add("btn");
                auditoriumButton.getStyleClass().add("btn-primary");
                auditoriumButton.setStyle("-fx-cursor: hand; -fx-padding: 10 5 10 5;");
                audit_btn_holder.getChildren().add(auditoriumButton);
                audit_btn_holder.setSpacing(20.0);

                final int id = (Integer) resultAuditoriums[i][1];

                auditoriumButton.setOnMouseClicked(event -> {
                    try {
                        showAuditorium(id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }catch (SQLException err){
            err.printStackTrace();
        }
    }
}

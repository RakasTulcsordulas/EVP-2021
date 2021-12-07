package com.app.evp2021.controllers;

import com.app.evp2021.Main;
import com.app.evp2021.services.MovieInputsController;
import com.app.evp2021.services.UserSession;
import com.app.evp2021.views.AuditoriumCreationPopup;
import com.app.evp2021.views.LandingPage;
import com.app.evp2021.views.LoginModal;
import com.app.sql.MySQLConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.sql.SQLException;

public class LandingPageController{
    @FXML private VBox movie_holder;

    @FXML private Text current_date;
    @FXML private DatePicker date_picker;
    @FXML private ScrollPane movie_scroll;
    @FXML private VBox movie_inputs;
    @FXML private Text input_title;
    @FXML private Button save_movie;
    @FXML private Button delete_movie;
    @FXML private HBox audit_btn_holder;
    @FXML private Text fromToDate;
    @FXML private GridPane screening_holder_grid;

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
    private int editMovieId;
    private MovieInputsController movieInputsController = null;



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
    private void onScreeningCheckBoxClicked(MouseEvent event) {
        movieInputsController.fillScreeningTimes();
    }


    @FXML
    void logoutAdmin(MouseEvent event) throws Exception{
        UserSession.getSession().logout();
        LandingPage.refresh();
    }


    @FXML
    void addMovie(MouseEvent event) {
        movieInputsController.addNewMovie();
        new_movie = true;
    }

    @FXML
    void deleteMovie(MouseEvent event) {

    }

    @FXML
    void refreshAdminView(MouseEvent event) throws Exception {
        LandingPage.refresh();
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

    @FXML
    void closeInputs(MouseEvent event) {
        movieInputsController.closeInputFields();
    }



    @FXML
    void saveMovie(MouseEvent event) throws Exception {
        if(new_movie){
            Object[] validationValues = movieInputsController.validateInputFields();

            if(validationValues != null){
                MySQLConnect dbConnection = new MySQLConnect();
                try{
                    dbConnection.establishConnection();
                }catch (SQLException err) {
                    err.printStackTrace();
                }

                dbConnection.insertNewMovie(
                        (String) validationValues[0],
                        (String) validationValues[1],
                        (String) validationValues[2],
                        (String) validationValues[3],
                        (String) validationValues[4],
                        (String) validationValues[6]
                );

                movieInputsController.closeInputFields();
                LandingPage.refresh();
            }
        }else{
            Object[] validationValues = movieInputsController.validateInputFields();

            if(validationValues != null){
                MySQLConnect dbConnection = new MySQLConnect();
                try{
                    dbConnection.establishConnection();
                }catch (SQLException err) {
                    err.printStackTrace();
                }

                dbConnection.updateMovie(
                        editMovieId,
                        (String) validationValues[0],
                        (String) validationValues[1],
                        (String) validationValues[2],
                        (String) validationValues[3],
                        (String) validationValues[4],
                        (String) validationValues[6]
                );

                movieInputsController.closeInputFields();
                LandingPage.refresh();
            }
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
                final Object[][] movies = resultMovies;

                movieButton.setOnMouseClicked(event -> {
                    movieInputsController.editExistingMovie(movies[rowIndex]);
                    editMovieId = (Integer) movies[rowIndex][1];
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
    //set up initial things
    public void initializeAdminView() throws Exception {
        if(movieInputsController == null) {
            screening_day_boxes = new CheckBox[]{check_1, check_2, check_3, check_4, check_5, check_6, check_7};
            screening_day_times = new ChoiceBox[]{choice_1, choice_2, choice_3, choice_4, choice_5, choice_6, choice_7};
            Object[] inputFields = {movie_title_input, director_input, cast_input, description_input, rating_drop, movie_room, duration_input, screening_day_boxes, screening_day_times};
            try {
                movieInputsController = new MovieInputsController(movie_inputs, inputFields, input_title, save_movie, delete_movie, fromToDate, screening_holder_grid);
            }catch (Exception err){
                err.printStackTrace();
            }

        }
    }
}

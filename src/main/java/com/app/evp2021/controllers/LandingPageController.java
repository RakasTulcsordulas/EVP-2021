package com.app.evp2021.controllers;

import com.app.evp2021.Main;
import com.app.evp2021.services.MovieInputsController;
import com.app.evp2021.services.UserSession;
import com.app.evp2021.views.*;
import com.app.sql.MySQLConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * Controls the Landing page (First page)
 */
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

    @FXML private TextField duration_input;

    private boolean new_movie;
    private int editMovieId;
    @FXML private Button list_movies_btn;
    private MovieInputsController movieInputsController = null;

    @FXML private void openScreeningWindow(MouseEvent event) throws Exception {

        ScreeningSummaryController Controller = ScreeningSummary.createWindow();

        ScreeningSummary.display();
    }

    /**
     * Listens whenever the datepicker changes value.
     * @param event ActionEvent.
     */
    @FXML private void onActionPerformed(ActionEvent event) {
        current_date.setText("Kiválasztott dátum: " + date_picker.getValue().toString());
        movie_holder.getChildren().clear();

        if(date_picker.getValue().isBefore(LocalDate.now())) {
            list_movies_btn.setDisable(true);
        }else{
            list_movies_btn.setDisable(false);
        }
    }
    /**
     * Lists all the currently available movies.
     */
    @FXML private void listMovies() {
        movie_holder.getChildren().clear();
        try{
            MySQLConnect dbConnection = new MySQLConnect();
            dbConnection.establishConnection();

            Object[][] screenings = dbConnection.getScreening(null,null,null, Timestamp.valueOf(date_picker.getValue().toString() + " 00:00:00"), "ORDER BY screening_start ASC");
            if(screenings.length > 1) {
                for(int i = 1; i < screenings.length; i++) {
                    String movieName = dbConnection.getMovie(screenings[i][2], null,null)[1][2].toString();
                    String auditoriumName = dbConnection.getAuditorium(screenings[i][3])[1][2].toString();
                    String startingTime = screenings[i][4].toString().split(" ")[1].substring(0,5);

                    Button movieButton = new Button(movieName + " | " + auditoriumName + " | " + startingTime);
                    movieButton.getStyleClass().add("btn-primary");
                    movieButton.setMinWidth(movie_scroll.getWidth()-25);
                    movieButton.setFocusTraversable(false);
                    int index = i;
                    movieButton.setOnMouseClicked(e -> {
                        try {
                            showAuditoriumForUser(screenings[index][3], movieName, startingTime, screenings[index][1]);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                    movie_holder.getChildren().add(movieButton);
                }
            }else{
                Text noMovieText = new Text("Erre a dátumra nincs listázható műsor!");
                noMovieText.getStyleClass().add("text-danger");
                noMovieText.setStyle("-fx-font-size: 18;");
                movie_holder.getChildren().add(noMovieText);
            }
        }catch (SQLException error){
            error.printStackTrace();
        }
    }

    @FXML
    /**
     * Opens up the admin interface.
     */
    private void openAdminModal() {
        try {
            LoginModal.displayWindow();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    /**
     * Log out the admin interface.
     * @param MouseEvent event
     */
    void logoutAdmin(MouseEvent event) throws Exception{
        UserSession.getSession().logout();
        LandingPage.refresh();
    }


    @FXML
    /**
     * Adds a new movie to the list.
     * @param MouseEvent event
     */
    void addMovie(MouseEvent event) {
        movieInputsController.addNewMovie();
        new_movie = true;
    }

    @FXML
    /**
     * Deletes a movie from the list.
     * @param MouseEvent event
     */
    void deleteMovie(MouseEvent event) throws Exception {
        try {
            MySQLConnect connection = new MySQLConnect();
            connection.establishConnection();
            connection.deleteScreening(editMovieId, null, null);
            connection.deleteMovie(editMovieId);
        } catch (SQLException err){
            err.printStackTrace();
        } finally {
            LandingPage.refresh();
        }
    }

    @FXML
    /**
     * Refreshes the admin interface.
     * @param MouseEvent event
     */
    void refreshAdminView(MouseEvent event) throws Exception {
        LandingPage.refresh();
    }

    @FXML
    /**
     * Creates a new auditorium.
     * @param MouseEvent event
     */
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
    /**
     * Fetches the seats from the database and creates a display of them for admins.
     * @param id event
     */
    public static void showAuditoriumForAdmin(int id) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("audit-view.fxml"));
        AnchorPane root = fxmlLoader.load();

        AuditoriumController Controller = fxmlLoader.getController();
        Controller.createAuditorium(false, true);
        Controller.setTitle("Terem mutatás");
        Controller.setButtonText("Terem törlése");
        Controller.setActionButtonType(2);
        Controller.setActionButtonParams(new Object[]{id});

        Controller.setSecondButtonText("Műsor beállítás");
        Controller.setSecondButtonVisible(true);

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

    public static void showAuditoriumForUser(Object id, String movieName, String startingTime, Object screeningId) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("audit-view.fxml"));
        AnchorPane root = fxmlLoader.load();

        AuditoriumController Controller = fxmlLoader.getController();
        Controller.createAuditorium(false, true);
        Controller.setTitle(movieName + " - Kezdés:" + startingTime);
        Controller.setButtonText("Foglalás");
        Controller.setActionButtonType(0);
        Controller.setActionButtonParams(new Object[]{screeningId, id});

        Controller.setSecondButtonVisible(false);

        Controller.toggleLegend(true);

        MySQLConnect dbConnection = new MySQLConnect();
        try {
            Object[][] resultSeats;

            dbConnection.establishConnection();
            resultSeats = dbConnection.getSeat(null,null,null, id);
            Controller.setAllSeatBasedOnreservation(resultSeats, screeningId);

        } catch (SQLException err) {
            err.printStackTrace();
        }

        AuditoriumCreationPopup popupAuditoriumWindow = new AuditoriumCreationPopup();
        popupAuditoriumWindow.createWindow(root);
        Controller.setParentStage(popupAuditoriumWindow.getStage());
        popupAuditoriumWindow.display();
    }

    @FXML
    /**
     * Disables the displays of the movie controllers.
     * @param MouseEvent event
     */
    void closeInputs(MouseEvent event) {
        movieInputsController.closeInputFields();
    }



    @FXML
    /**
     * Saves movies to the database.
     * @param MouseEvent event
     */
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

                int insertId = dbConnection.insertNewMovie(
                        (String) validationValues[0],
                        (String) validationValues[1],
                        (String) validationValues[2],
                        (String) validationValues[3],
                        (String) validationValues[4],
                        (String) validationValues[5]
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

                for(int i = 0; i < 6; i++) {
                    System.out.println(validationValues[i]);
                }

                dbConnection.updateMovie(
                        editMovieId,
                        (String) validationValues[0],
                        (String) validationValues[1],
                        (String) validationValues[2],
                        (String) validationValues[3],
                        (String) validationValues[4],
                        (String) validationValues[5]
                );

                movieInputsController.closeInputFields();
                LandingPage.refresh();
            }
        }
    }
    /**
     * Shows the movies from the database at the admin interface.
     */
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
                movieButton.setFocusTraversable(false);


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
    /**
     * Set the auditoriums on the admin view.
     */
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
                auditoriumButton.setFocusTraversable(false);
                audit_btn_holder.getChildren().add(auditoriumButton);
                audit_btn_holder.setSpacing(20.0);

                final int id = (Integer) resultAuditoriums[i][1];

                auditoriumButton.setOnMouseClicked(event -> {
                    try {
                        this.showAuditoriumForAdmin(id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }catch (SQLException err){
            err.printStackTrace();
        }
    }
    /**
     * Initializes the admin view.
     */
    //set up initial things
    public void initializeAdminView() throws Exception {
        if(movieInputsController == null) {
            Object[] inputFields = {movie_title_input, director_input, cast_input, description_input, rating_drop, duration_input};
            try {
                movieInputsController = new MovieInputsController(movie_inputs, inputFields, input_title, save_movie, delete_movie);
            }catch (Exception err){
                err.printStackTrace();
            }

        }
    }

    public void setTodayAsSelectedDate() {
        date_picker.setValue(LocalDate.now());
        current_date.setText("Kiválasztott dátum: " + date_picker.getValue().toString());
    }
}

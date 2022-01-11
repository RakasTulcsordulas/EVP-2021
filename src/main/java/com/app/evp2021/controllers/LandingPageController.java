package com.app.evp2021.controllers;

import com.app.evp2021.Main;
import com.app.evp2021.services.MovieInputsController;
import com.app.evp2021.services.PopupWindow;
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

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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


    @FXML private TextField reservation_token_field;
    /**
     * Opens screening summary pop up window.
     */
    @FXML private void openScreeningWindow() {

        ScreeningSummaryController Controller = ScreeningSummary.createWindow();

        ScreeningSummary.display();
    }

    @FXML private void onReservationDeleteButtonClicked() {
            try{
                MySQLConnect dbConnection = new MySQLConnect();
                dbConnection.establishConnection();

                Object[][] screening = dbConnection.getScreening(null,null,null,null,null);
                ArrayList<Integer> expiredReservationIds = new ArrayList<>();
                for(int i = 1; i < screening.length; i++) {
                    String start = screening[i][4].toString().substring(0, 16);
                    LocalDateTime startDateTime = LocalDateTime.of(LocalDate.parse(start.split(" ")[0]), LocalTime.parse(start.split(" ")[1]));
                    if(startDateTime.isBefore(LocalDateTime.now())){
                        expiredReservationIds.add((int) screening[1][1]);
                        //dbConnection.deleteReservation(null, screening[1][1]);
                    }
                }

                LocalDateTime nowDateTime = LocalDateTime.now();
                PopupWindow yesNoWindow = new PopupWindow(PopupWindow.TYPE.YESNO, "Lejárt foglalások törlése", "Szeretnéd törölni [" + nowDateTime.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm")) + "] dátumig a lejárt a foglalásokat?  \nÖsszesen: " + (expiredReservationIds.size()),null);

                int response = yesNoWindow.displayWindow();
                if(response == 1) {
                    for (int i = 0; i < expiredReservationIds.size(); i++) {
                        dbConnection.deleteReservation(null, expiredReservationIds.get(i));
                    }
                }
            }catch (SQLException error) {
                error.printStackTrace();
            }
    }

    /**
     * Checks reservation token when button clicked.
     */
    @FXML private void onReservationCheckButtonClicked() {
        if(!reservation_token_field.getText().isEmpty()) {
            try {

                MySQLConnect dbConn = new MySQLConnect();
                dbConn.establishConnection();

                Object[][] reservation = dbConn.getReservation(null,null,null, reservation_token_field.getText(), null);

                if(reservation.length == 2) {
                    Object[][] screening = dbConn.getScreening(reservation[1][2],null,null,null, null);
                    String screeningStartDate = screening[1][4].toString().split(" ")[0];
                    String[] screeningStartTime = screening[1][4].toString().split(" ")[1].split(":");
                    LocalDateTime screeningStart = LocalDateTime.of(
                            LocalDate.parse(screeningStartDate),
                            LocalTime.of(Integer.parseInt(screeningStartTime[0]), Integer.parseInt(screeningStartTime[1]))
                    );
                    screeningStart.minusMinutes(15);
                    if(LocalDateTime.now().isBefore(screeningStart)) {

                        if((int) reservation[1][5] == 0) {
                            int employeeId = (int) dbConn.getEmployee(null, UserSession.getSession().getEmployee())[1][1];

                            dbConn.activateReservation(reservation_token_field.getText(), employeeId);

                            PopupWindow successWindow = new PopupWindow(PopupWindow.TYPE.SUCCESS, "Foglalás sikeresen aktiválva.", "A foglalás sikeresen fel lett véve, fizetés megkezdhető!", null);
                            reservation_token_field.clear();
                            successWindow.displayWindow();
                        }else{
                            PopupWindow errorWindow = new PopupWindow(PopupWindow.TYPE.ERROR, "A foglalás már felvéve!", "A foglalás már nem érvényes mert már beváltották!", null);
                            reservation_token_field.clear();
                            errorWindow.displayWindow();
                        }

                    }else{
                        PopupWindow errorWindow = new PopupWindow(PopupWindow.TYPE.ERROR, "A foglalás lejárt!", "A foglalás már nem érvényes ezért törlésre kerül. A film kezdete előtt 15 percel kell beváltani!", null);
                        reservation_token_field.clear();
                        dbConn.deleteReservation(reservation[1][1], null);
                        errorWindow.displayWindow();
                    }

                }else{
                    String errorString = "A megadott foglalási token ("+reservation_token_field.getText()+") nem található a rendszerben!";

                    PopupWindow errorWindow = new PopupWindow(PopupWindow.TYPE.ERROR, "Nem található!", errorString, null);
                    reservation_token_field.clear();
                    errorWindow.displayWindow();
                }
            }catch (SQLException error) {
                error.printStackTrace();
            }
        }
    }

    /**
     * Listens whenever the date picker changes value.
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
            Text noMovieText = new Text("Erre a dátumra nincs listázható műsor!");
            noMovieText.getStyleClass().add("text-danger");
            noMovieText.setStyle("-fx-font-size: 18;");

            if(screenings.length > 1) {
                for(int i = 1; i < screenings.length; i++) {
                    LocalDateTime screeningStartLocalDateTimeFormat = LocalDateTime.of(LocalDate.parse(
                            screenings[i][4].toString().split(" ")[0]), LocalTime.parse(screenings[i][4].toString().split(" ")[1].substring(0,5)));
                    if(screeningStartLocalDateTimeFormat.isAfter(LocalDateTime.now())){
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
                }

                if(movie_holder.getChildren().size() == 0) {
                    movie_holder.getChildren().add(noMovieText);
                }
            }else{
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
     */
    void logoutAdmin(){
        UserSession.getSession().logout();
        LandingPage.refresh();
    }


    @FXML
    /**
     * Adds a new movie to the list.
     */
    void addMovie() {
        movieInputsController.addNewMovie();
        new_movie = true;
    }

    @FXML
    /**
     * Deletes a movie from the list.
     */
    void deleteMovie(){
        PopupWindow areYouSureWindow = new PopupWindow(PopupWindow.TYPE.YESNO, "Film törlése", "Biztosan szeretnéd törölni ezt a filmet? A foglalások és a műsor is törlésre fog kerülni!", null);
        int response = areYouSureWindow.displayWindow();
        if(response == 1){
            try {
                MySQLConnect connection = new MySQLConnect();
                connection.establishConnection();

                Object[][] screeningsBasedOnMovie = connection.getScreening(null, editMovieId, null,null,null);
                for(int i = 1; i < screeningsBasedOnMovie.length; i++) {
                    connection.deleteReservation(null, screeningsBasedOnMovie[1][1]);
                }
                connection.deleteScreening(null, editMovieId, null, null);
                connection.deleteMovie(editMovieId);
            } catch (SQLException err){
                err.printStackTrace();
            } finally {
                LandingPage.refresh();
            }
        }
    }

    @FXML
    /**
     * Refreshes the admin interface.
     */
    void refreshAdminView() {
        LandingPage.refresh();
    }

    @FXML
    /**
     * Creates a new auditorium.
     */
    void addNewAuditorium() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("audit-view.fxml"));
        AnchorPane rootAnchorPane = null;
        try{
             rootAnchorPane = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
     * @param id Integer
     */
    public static void showAuditoriumForAdmin(int id) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("audit-view.fxml"));
        AnchorPane root = null;
        try{
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    /**
     * Creates/shows the auditorium for the user.
     * @param id Object of integers.
     * @param movieName Name of the movie(s).
     * @param startingTime Starting time.
     * @param screeningId Object of Screening IDs.
     */
    public static void showAuditoriumForUser(Object id, String movieName, String startingTime, Object screeningId)  {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("audit-view.fxml"));
        AnchorPane root = null;
        try{
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
     */
    void closeInputs() {
        movieInputsController.closeInputFields();
    }

    @FXML
    /**
     * Saves movies to the database.
     */
    void saveMovie() {
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
    public void initializeAdminView() {
        if(movieInputsController == null) {
            Object[] inputFields = {movie_title_input, director_input, cast_input, description_input, rating_drop, duration_input};
            try {
                movieInputsController = new MovieInputsController(movie_inputs, inputFields, input_title, save_movie, delete_movie);
            }catch (Exception err){
                err.printStackTrace();
            }

        }
    }

    /**
     * Sets the default date to today, and chose one.
     */
    public void setTodayAsSelectedDate() {
        date_picker.setValue(LocalDate.now());
        current_date.setText("Kiválasztott dátum: " + date_picker.getValue().toString());
    }
}

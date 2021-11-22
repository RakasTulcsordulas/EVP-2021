package com.app.evp2021.controllers;

import com.app.evp2021.Main;
import com.app.evp2021.services.UserSession;
import com.app.evp2021.views.AuditCreationPopup;
import com.app.evp2021.views.LandingPage;
import com.app.evp2021.views.LoginModal;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class LandingPageController{


    @FXML private Text current_date;
    @FXML private DatePicker date_picker;
    @FXML private ScrollPane movie_scroll;
    @FXML private VBox movie_list;
    @FXML private Label once_text;
    @FXML private VBox movie_inputs;
    @FXML private Text input_title;
    @FXML private Button save_movie;
    @FXML private Button delete_movie;

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
    @FXML private CheckBox once_checkbox;
    private CheckBox[] screening_day_boxes;
    private ChoiceBox[] screening_day_times;

    private Tooltip setTooltip(Node n, String text) {
        Tooltip t = new Tooltip();
        t.setShowDelay(Duration.millis(100));
        t.setPrefWidth(300);
        t.setWrapText(true);
        t.setText(text);
        t.setStyle("-fx-font-size: 12px;");
        Tooltip.install(n,t);

        return t;
    }

    private void removeTooltip(Node n, Tooltip t) {
        Tooltip.uninstall(n, t);
    }
    private boolean new_movie;
    @FXML private void onActionPerformed(ActionEvent event) {
        current_date.setText("Kiválasztott dátum: " + date_picker.getValue().toString());
    }

    @FXML private void listMovies() {
        /*MySQLConnect con = new MySQLConnect();
        con.establishConnection();
        String time = date_picker.getValue().toString() + " 12:00:00";
        Timestamp ts = Timestamp.valueOf("2021-11-04 20:09:23");*/
        /**
         * LISTAZAS UTAN, UGYE MEGVAN AZ ADOTT SCREENING ID, MELYIK TEREM, FILM ID, UTANA HA NYOMUNK A FILMRE AKKOR TEREMID ALAPJAN
         * LEKERJUK HANY SZEKES A TEREM, ES HOL VANNAK SZEKEK, BEADJUK A FUGGVENYNEK EGY ARRAYLISTET AMI TARTALMAZ 2 INDEXES OBJECTETKET AMI A SOR, OSZLOPA A SZEKNEK
         * HA MEGVANNAK A SZEKEK, SCREENING ID ALAPJAN LEKERJUK A RESERVATIONOKET ES OTT A setSEAT-el ATRAKJUK FOGLALTRA OKET, MAJD EZEK UTAN MUTATJUK CSAK MEG A TERMET
         */
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
        try {
            LoginModal.display();
        }catch (Exception e){

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
    void addNewAudit(MouseEvent event) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("audit-view.fxml"));
        AnchorPane root = fxmlLoader.load();
        AuditoriumController controller = fxmlLoader.getController();
        controller.create();
        controller.setTitle("Terem létrehozás");
        controller.setButtonText("Terem mentése");
        controller.setActionButtonType(1);

        AuditCreationPopup popup = new AuditCreationPopup();
        popup.display(root);
        //bezaras utan mehet a termek frissitese
        System.out.println("asd");
    }

    public void setUpInputs(String title, String movie_title, String directors, String desc, int rating, int auditroom, Object[] screening, boolean one_screening) {

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


    }

    @FXML
    void deleteMovie(MouseEvent event) {

    }

    public boolean checkInputs() {
        boolean b = true;
        Tooltip t1 = null, t2 = null, t3 = null, t4 = null;

        if(movie_title_input.getText().isEmpty()) {
            movie_title_input.getStyleClass().add("input-error");
            setErrorClass(movie_title_input);
            t1 = setTooltip(movie_title_input, "Ezt a mezőt kötelező kitölteni!");
            b = false;
        }else{
           removeErrorClass(movie_title_input);
            removeTooltip(movie_title_input, t1);
        }

        if(movie_room.getValue() == null) {
            setErrorClass(movie_room);
            t2 = setTooltip(movie_room, "Ezt a mezőt kötelező kitölteni!");
            b = false;
        }else{
            removeErrorClass(movie_room);
            removeTooltip(movie_room, t2);
        }

        if(duration_input.getText().isEmpty()) {
            setErrorClass(duration_input);
            t3 = setTooltip(duration_input, "Ezt a mezőt kötelező kitölteni!");
            b = false;
        }else{
            try{
                int dur = Integer.parseInt(duration_input.getText());

                if(dur < 10 || dur > 250) {
                    setErrorClass(duration_input);
                    t3 = setTooltip(duration_input, "10 és 250 között kell lennie a hossznak!");
                    b = false;
                }else{

                    removeErrorClass(duration_input);
                    removeTooltip(duration_input, t3);
                }

            }catch (NumberFormatException err) {
                t3 = setTooltip(duration_input, "Számot kell megadni film hossznak.");
                setErrorClass(duration_input);
                b = false;
            }
        }
        boolean found = false;
        for(int i = 0; i < screening_day_boxes.length; i++) {
            if(screening_day_boxes[i].isSelected()){
                if(screening_day_times[i].getValue() == null){
                    setErrorClass(screening_day_times[i]);
                    t4 = setTooltip(screening_day_times[i], "Ehhez a kiválasztott naphoz, szükséges időpontot választani.");
                }
            }
        }




        return b;
    }
    public void setErrorClass(Node n){
        if(n instanceof TextField && !(n.getStyleClass().contains("input-error"))){
           n.getStyleClass().add("input-error");
        }else if((n instanceof ChoiceBox || n instanceof CheckBox ) && !(n.getStyleClass().contains("choice-error"))){
            n.getStyleClass().add("choice-error");
        }
    }

    public void removeErrorClass(Node n) {
        if(n instanceof TextField && (n.getStyleClass().contains("input-error"))){
            n.getStyleClass().remove("input-error");
        }else if((n instanceof ChoiceBox || n instanceof CheckBox ) && (n.getStyleClass().contains("choice-error"))){
            n.getStyleClass().remove("choice-error");
        }
    }

    public boolean checkScreeningInput() {
        boolean b = true;
        Tooltip t[] = new Tooltip[6];

        for(int i = 0; i < 7; i++) {
            removeErrorClass(screening_day_boxes[i]);
            removeTooltip(screening_day_boxes[i], t[i]);
            
            if(screening_day_boxes[i].isSelected() && screening_day_times[i].getValue() == null){
                setErrorClass(screening_day_times[i]);
                t[i] = setTooltip(screening_day_times[i], "Adott naphoz kötelező megjelőlni kezdődátumot.");
                b = false;
            }else if(screening_day_times[i].getValue() != null && !screening_day_boxes[i].isSelected()) {
                setErrorClass(screening_day_times[i]);
                t[i] = setTooltip(screening_day_times[i], "Adott dátumhoz kötelező kiválasztani a napot is!");
                b = false;
            }
        }

        return b;
    }
    @FXML
    void saveMovie(MouseEvent event) {
        if(new_movie){
            if(checkInputs() && checkScreeningInput()){}
        }else{
            //edited movie save
        }
    }
}

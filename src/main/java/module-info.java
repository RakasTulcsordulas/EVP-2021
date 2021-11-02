module com.app.evp2021 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.app.evp2021 to javafx.fxml;
    exports com.app.evp2021;
    exports com.app.evp2021.controllers;
    opens com.app.evp2021.controllers to javafx.fxml;
    exports com.app.evp2021.views;
    opens com.app.evp2021.views to javafx.fxml;
}
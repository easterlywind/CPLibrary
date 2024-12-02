module com.example.cplibrary {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.desktop;
    requires java.sql;
    requires org.json;
    requires mysql.connector.java;

    exports com.example.cplibrary;
    opens com.example.cplibrary.model to javafx.fxml, javafx.base;
    opens com.example.cplibrary.controller.staff to javafx.fxml;
    opens com.example.cplibrary.controller.user to javafx.fxml;
    opens com.example.cplibrary.controller.common to javafx.fxml;
}
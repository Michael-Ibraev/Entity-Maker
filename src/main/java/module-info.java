module com.example.entitymaker {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires javafx.swing;
    requires itextpdf;


    opens com.example.entitymaker to javafx.fxml;
    exports com.example.entitymaker;
}
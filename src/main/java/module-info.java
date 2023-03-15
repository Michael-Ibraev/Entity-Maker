module com.example.entitymaker {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.entitymaker to javafx.fxml;
    exports com.example.entitymaker;
}
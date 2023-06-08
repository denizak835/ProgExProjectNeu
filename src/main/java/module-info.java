module com.example.progexprojectneu {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.progexprojectneu to javafx.fxml;
    exports com.example.progexprojectneu;
}
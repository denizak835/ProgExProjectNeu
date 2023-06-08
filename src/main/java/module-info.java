module com.example.progexprojectneu {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.example.progexprojectneu to javafx.fxml;
    exports com.example.progexprojectneu;
}
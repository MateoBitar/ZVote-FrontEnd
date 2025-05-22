module com.example.zvotefrontend {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.zvotefrontend to javafx.fxml;
    exports com.example.zvotefrontend;
}
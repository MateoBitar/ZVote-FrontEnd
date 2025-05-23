module com.example.zvotefrontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;


    opens com.example.zvotefrontend to javafx.fxml;
    exports com.example.zvotefrontend;
}
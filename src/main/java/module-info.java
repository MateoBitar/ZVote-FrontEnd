module com.example.zvotefrontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires java.sql;
    requires org.json;


    opens com.example.zvotefrontend to javafx.fxml;
    exports com.example.zvotefrontend;
}
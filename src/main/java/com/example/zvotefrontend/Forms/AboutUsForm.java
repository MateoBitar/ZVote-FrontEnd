package com.example.zvotefrontend.Forms;

import com.example.zvotefrontend.Controllers.AboutUsController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AboutUsForm {

    private final AboutUsController controller = new AboutUsController();  // Connect to backend

    public Scene createAboutUsScene(Stage primaryStage) {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Label title = new Label("About Us");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        TextArea aboutText = new TextArea();
        aboutText.setEditable(false);
        aboutText.setWrapText(true);
        aboutText.setPrefHeight(200);

        // Fetch about info from controller
        try {
            aboutText.setText(controller.fetchAboutInfo());
        } catch (Exception e) {
            aboutText.setText("Failed to load About Us information.");
        }

        layout.getChildren().addAll(title, aboutText);
        return new Scene(layout, 600, 400);
    }
}
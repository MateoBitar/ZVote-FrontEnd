package com.example.zvotefrontend.Forms;

import com.example.zvotefrontend.Controllers.ContactUsController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ContactUsForm {

    private final ContactUsController controller = new ContactUsController();  // Connect to backend

    public Scene createContactUsScene(Stage primaryStage) {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Label title = new Label("Contact Us");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        TextArea contactText = new TextArea();
        contactText.setEditable(false);
        contactText.setWrapText(true);
        contactText.setPrefHeight(200);

        // Fetch contact info from controller
        try {
            contactText.setText(controller.fetchContactInfo());
        } catch (Exception e) {
            contactText.setText("Failed to load Contact Us information.");
        }

        layout.getChildren().addAll(title, contactText);
        return new Scene(layout, 600, 400);
    }
}
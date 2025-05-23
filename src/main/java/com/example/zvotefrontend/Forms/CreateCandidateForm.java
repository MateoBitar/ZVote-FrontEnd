package com.example.zvotefrontend.Forms;

import com.example.zvotefrontend.Controllers.CreateCandidateController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONObject;
import java.io.File;

public class CreateCandidateForm {
    private Stage primaryStage;
    private CreateCandidateController controller;
    private TextField nameField, bioField;
    private File selectedPhoto;

    public void showCreateCandidateForm(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.controller = new CreateCandidateController();

        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: transparent;");

        setupTopBar(layout);
        setupForm(layout);

        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupTopBar(BorderPane layout) {
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(10, 10, 10, 40));
        topBar.setStyle("-fx-background-color: #C8F0FF;");
        topBar.setAlignment(Pos.CENTER_LEFT);

        DropShadow shadow = new DropShadow();
        shadow.setRadius(5);
        shadow.setOffsetY(2);
        shadow.setColor(Color.LIGHTGRAY);
        topBar.setEffect(shadow);

        Label logo = new Label("ZVote");
        logo.setFont(Font.font("Onyx", FontWeight.BOLD, 60));

        Button backButton = new Button("Back");
        backButton.setOnAction(event -> primaryStage.close());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBar.getChildren().addAll(logo, spacer, backButton);
        layout.setTop(topBar);
    }

    private void setupForm(BorderPane layout) {
        GridPane createCandidateForm = new GridPane();
        createCandidateForm.setPadding(new Insets(20));
        createCandidateForm.setAlignment(Pos.TOP_CENTER);
        createCandidateForm.setHgap(20);
        createCandidateForm.setVgap(20);

        Label title = new Label("Create Candidate");
        title.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");
        createCandidateForm.add(title, 0, 0, 2, 1);

        nameField = new TextField();
        nameField.setPromptText("Candidate Name");
        createCandidateForm.add(new Label("Name:"), 0, 1);
        createCandidateForm.add(nameField, 1, 1);

        bioField = new TextField();
        bioField.setPromptText("Candidate Bio");
        createCandidateForm.add(new Label("Bio:"), 0, 2);
        createCandidateForm.add(bioField, 1, 2);

        FileChooser fileChooser = new FileChooser();
        Button uploadPhotoButton = new Button("Upload Candidate Photo");
        uploadPhotoButton.setOnAction(event -> {
            selectedPhoto = fileChooser.showOpenDialog(primaryStage);
            uploadPhotoButton.setText(selectedPhoto != null ? "Photo Uploaded" : "Upload Candidate Photo");
        });

        createCandidateForm.add(uploadPhotoButton, 0, 3, 2, 1);

        Button submitButton = new Button("Create Candidate");
        submitButton.setOnAction(event -> createCandidate());
        createCandidateForm.add(submitButton, 0, 4, 2, 1);

        layout.setCenter(createCandidateForm);
    }

    private void createCandidate() {
        JSONObject candidateData = new JSONObject();
        candidateData.put("name", nameField.getText());
        candidateData.put("bio", bioField.getText());

        boolean success = controller.createCandidate(candidateData, selectedPhoto);
        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Success");
            alert.setContentText("Candidate created successfully!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Failed to create candidate.");
            alert.showAndWait();
        }
    }
}
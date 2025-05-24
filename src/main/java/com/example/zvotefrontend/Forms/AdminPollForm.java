package com.example.zvotefrontend.Forms;

import com.example.zvotefrontend.Controllers.AdminPollController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.util.Map;

public class AdminPollForm {
    private Stage primaryStage;
    private GridPane pollForm;
    private TextField pollTitleField, pollDescriptionField;
    private DatePicker startDatePicker, endDatePicker;
    private AdminPollController controller;

    public void showCreatePoll(Stage primaryStage, Map<String, String> usersession) {
        this.primaryStage = primaryStage;
        this.controller = new AdminPollController();

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
        pollForm = new GridPane();
        pollForm.setPadding(new Insets(20));
        pollForm.setAlignment(Pos.TOP_CENTER);
        pollForm.setHgap(20);
        pollForm.setVgap(20);

        Label title = new Label("Create Poll");
        title.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");
        pollForm.add(title, 0, 0, 2, 1);

        pollTitleField = new TextField();
        pollTitleField.setPromptText("Poll Title");
        pollForm.add(new Label("Poll Title:"), 0, 1);
        pollForm.add(pollTitleField, 1, 1);

        pollDescriptionField = new TextField();
        pollDescriptionField.setPromptText("Poll Description");
        pollForm.add(new Label("Poll Description:"), 0, 2);
        pollForm.add(pollDescriptionField, 1, 2);

        startDatePicker = new DatePicker();
        pollForm.add(new Label("Start Date:"), 0, 3);
        pollForm.add(startDatePicker, 1, 3);

        endDatePicker = new DatePicker();
        pollForm.add(new Label("End Date:"), 0, 4);
        pollForm.add(endDatePicker, 1, 4);

        Button submitButton = new Button("Create Poll");
        submitButton.setOnAction(event -> createPoll());
        pollForm.add(submitButton, 0, 5, 2, 1);

        layout.setCenter(pollForm);
    }

    private void createPoll() {
        JSONObject pollData = new JSONObject();
        pollData.put("title", pollTitleField.getText());
        pollData.put("description", pollDescriptionField.getText());
        pollData.put("start_date", startDatePicker.getValue().toString());
        pollData.put("end_date", endDatePicker.getValue().toString());

        boolean success = controller.createPoll(pollData);
        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Success");
            alert.setContentText("Poll created successfully!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Failed to create poll.");
            alert.showAndWait();
        }
    }
}
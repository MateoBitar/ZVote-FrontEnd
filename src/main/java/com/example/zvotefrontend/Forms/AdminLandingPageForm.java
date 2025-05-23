package com.example.zvotefrontend.Forms;

import com.example.zvotefrontend.Controllers.AdminLandingPageController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONObject;

public class AdminLandingPageForm {
    private Stage primaryStage;
    private JSONObject pollsData;

    public AdminLandingPageForm(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.pollsData = AdminLandingPageController.fetchPolls();
    }

    public void showLandingPage() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Label title = new Label("Admin Dashboard");
        title.setStyle("-fx-font-size: 50px; -fx-font-weight: bold;");

        Button addPollButton = new Button("Add Poll");
        Button deletePollButton = new Button("Delete Poll");
        addPollButton.setOnAction(e -> showCreatePollForm());
        deletePollButton.setOnAction(e -> showDeletePollForm());

        layout.getChildren().addAll(title, addPollButton, deletePollButton);

        Scene scene = new Scene(layout, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showDeletePollForm() {
        VBox deletePollLayout = new VBox(20);
        deletePollLayout.setPadding(new Insets(20));
        deletePollLayout.setAlignment(Pos.CENTER);

        Label title = new Label("Delete Poll");
        title.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

        ComboBox<String> pollDropdown = new ComboBox<>();
        for (Object pollObj : pollsData.getJSONArray("polls")) {
            JSONObject poll = (JSONObject) pollObj;
            pollDropdown.getItems().add(poll.getString("title"));
        }

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
            int selectedPollId = pollsData.getJSONArray("polls")
                    .getJSONObject(pollDropdown.getSelectionModel().getSelectedIndex())
                    .getInt("id");
            AdminLandingPageController.deletePoll(selectedPollId);
            showLandingPage();
        });

        deletePollLayout.getChildren().addAll(title, pollDropdown, deleteButton);

        Scene deleteScene = new Scene(deletePollLayout, 500, 500);
        primaryStage.setScene(deleteScene);
    }

    public void showCreatePollForm() {
        VBox createPollLayout = new VBox(20);
        createPollLayout.setPadding(new Insets(20));
        createPollLayout.setAlignment(Pos.CENTER);

        Label title = new Label("Create New Poll");
        title.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

        TextField pollTitleField = new TextField();
        pollTitleField.setPromptText("Poll Title");

        TextField pollDescriptionField = new TextField();
        pollDescriptionField.setPromptText("Poll Description");

        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Start Date");

        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setPromptText("End Date");

        Button createButton = new Button("Create Poll");
        createButton.setOnAction(e -> {
            JSONObject pollData = new JSONObject();
            pollData.put("title", pollTitleField.getText());
            pollData.put("description", pollDescriptionField.getText());
            pollData.put("startDate", startDatePicker.getValue().toString());
            pollData.put("endDate", endDatePicker.getValue().toString());

            AdminLandingPageController.addPoll(pollData);
            showLandingPage();
        });

        createPollLayout.getChildren().addAll(title, pollTitleField, pollDescriptionField, startDatePicker, endDatePicker, createButton);

        Scene createScene = new Scene(createPollLayout, 500, 500);
        primaryStage.setScene(createScene);
    }
}
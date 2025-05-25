package com.example.zvotefrontend.Forms;

import com.example.zvotefrontend.Controllers.CandidateController;
import com.example.zvotefrontend.Controllers.CandidatesController;
import com.example.zvotefrontend.Controllers.PollController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.util.*;


public class CandidateForm {

    private final CandidateController controller = new CandidateController();
    public static Map<String, String> userSession = new HashMap<>();

    // Method to display a list of candidates for a specific poll
    public void displayCandidates(Stage stage, int pollId, Map<String,String> userSession) throws Exception {
        this.userSession = userSession;

        // Top bar configuration
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(10, 10, 10, 40));
        topBar.setStyle("-fx-background-color: #C8F0FF;");
        topBar.setAlignment(Pos.CENTER_LEFT);

        // Add shadow effect
        DropShadow shadow = new DropShadow();
        shadow.setRadius(5);
        shadow.setOffsetY(2);
        shadow.setColor(Color.LIGHTGRAY);
        topBar.setEffect(shadow);

        // Logo
        Label logo = new Label("ZVote");
        logo.setFont(Font.font("Onyx", FontWeight.BOLD, 60));
        topBar.getChildren().add(logo);


        // Instructional Label
        Label label = new Label("Choose the candidates for the poll created:");
        label.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: #333333;");


        // Fetch all candidates from CandidateService
        ObservableList<JSONObject> candidates = FXCollections.observableArrayList(
                new CandidatesController().getAllCandidates()
        );


        // TableView to display candidates
        TableView<JSONObject> tableView = new TableView<>();
        tableView.setStyle("-fx-background-color: #C8F0FF; -fx-font-size: 16px;");
        tableView.setItems(candidates);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPrefHeight(600);


        // Map to store CheckBoxes associated with each candidate
        Map<JSONObject, CheckBox> checkBoxMap = new HashMap<>();


        // Candidate Name Column
        TableColumn<JSONObject, String> nameColumn = new TableColumn<>("Candidate Name");
        nameColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().optString("name")));
        nameColumn.setStyle("-fx-alignment: CENTER; -fx-text-fill: black; -fx-font-weight: bold;");


        // CheckBox Column for selection
        TableColumn<JSONObject, CheckBox> checkBoxColumn = new TableColumn<>("Select");
        checkBoxColumn.setCellValueFactory(data -> {
            CheckBox checkBox = new CheckBox();
            checkBoxMap.put(data.getValue(), checkBox);  // Associate CheckBox with CandidateModel
            return new javafx.beans.property.SimpleObjectProperty<>(checkBox);
        });
        checkBoxColumn.setStyle("-fx-alignment: CENTER; -fx-text-fill: black; -fx-font-weight: bold;");


        // Add columns to the TableView
        tableView.getColumns().addAll(nameColumn, checkBoxColumn);


        // Submit button to save selected candidates
        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-background-color: #C8F0FF; -fx-font-weight: bold; -fx-border-radius: 10px; -fx-font-size: 22px;" +
                "-fx-cursor: hand");

        submitButton.setOnAction(event -> {
            for (JSONObject candidate : candidates) {
                CheckBox checkBox = checkBoxMap.get(candidate);
                if (checkBox != null && checkBox.isSelected()) {
                    try {
                        JSONObject resultData = new JSONObject();
                        resultData.put("registration_date", java.time.LocalDate.now().toString());
                        resultData.put("votes_casted", 0);
                        resultData.put("withdrawal_date", JSONObject.NULL);

                        // Wrap poll_ID in a nested object
                        JSONObject pollObject = new JSONObject();
                        pollObject.put("poll_ID", pollId);
                        resultData.put("poll", pollObject);

                        // Wrap candidate_ID in a nested object
                        JSONObject candidateObject = new JSONObject();
                        candidateObject.put("candidate_ID", candidate.optInt("candidate_ID"));
                        resultData.put("candidate", candidateObject);

                        CandidateController.addResult(resultData);
                    } catch (Exception e) {
                        System.out.println("Error submitting result: " + e.getMessage());
                    }
                }
            }

            // Confirmation alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Candidates linked to the poll successfully!");
            alert.showAndWait();

            try {
                new AdminLandingPageForm().showAdminLandingPage(stage, userSession);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });



        // Layout configuration
        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.getChildren().addAll(topBar, label, tableView, submitButton);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setStyle("-fx-background-color: white;");


        // Scene setup
        Scene scene = new Scene(vBox, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight() - 80);
        stage.setScene(scene);
        stage.show();
    }
}

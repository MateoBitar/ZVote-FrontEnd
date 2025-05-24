package com.example.zvotefrontend.Forms;

import com.example.zvotefrontend.Controllers.AdminPollDetailsController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.json.JSONObject;

public class AdminPollDetailsForm {
    private Stage primaryStage;
    private AdminPollDetailsController controller;
    private Label pollTitleLabel, pollDescriptionLabel, statusLabel;
    private PieChart pieChart;

    public void showAdminPollDetails(Stage primaryStage, int pollId) {
        this.primaryStage = primaryStage;
        this.controller = new AdminPollDetailsController();

        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: transparent;");

        setupTopBar(layout);
        setupPollInfo(layout, pollId);

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

    private void setupPollInfo(BorderPane layout, String pollId) {
        VBox pollInfoSection = new VBox(15);
        pollInfoSection.setPadding(new Insets(40));
        pollInfoSection.setAlignment(Pos.TOP_CENTER);
        pollInfoSection.setPrefWidth(300);

        pollTitleLabel = new Label();
        pollTitleLabel.setStyle("-fx-font-size: 50px; -fx-font-weight: bold;");

        pollDescriptionLabel = new Label();
        pollDescriptionLabel.setStyle("-fx-font-size: 25px;");

        statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        pieChart = new PieChart();
        pieChart.setLegendVisible(true);
        pieChart.setLabelsVisible(true);
        pieChart.setPrefWidth(400);
        pieChart.setPrefHeight(400);

        pollInfoSection.getChildren().addAll(pollTitleLabel, pollDescriptionLabel, statusLabel, pieChart);
        layout.setCenter(pollInfoSection);

        loadPollDetails(pollId);
    }

    private void loadPollDetails(String pollId) {
        JSONObject pollData = controller.getPollDetails(pollId);
        pollTitleLabel.setText("Poll Title: " + pollData.getString("title"));
        pollDescriptionLabel.setText("Description: " + pollData.getString("description"));
        statusLabel.setText("Status: " + pollData.getString("status"));

        for (Object candidateObj : pollData.getJSONArray("candidates")) {
            JSONObject candidate = (JSONObject) candidateObj;
            PieChart.Data slice = new PieChart.Data(candidate.getString("name"), candidate.getDouble("votePercentage"));
            pieChart.getData().add(slice);
        }
    }
}
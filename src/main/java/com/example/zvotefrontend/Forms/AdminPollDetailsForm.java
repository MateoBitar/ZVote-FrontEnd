package com.example.zvotefrontend.Forms;

import com.example.zvotefrontend.Controllers.AdminPollDetailsController;
import com.example.zvotefrontend.Controllers.LandingPageController;
import com.example.zvotefrontend.Controllers.PollController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdminPollDetailsForm {

    private final AdminPollDetailsController controller = new AdminPollDetailsController();
    public static Map<String, String> userSession = new HashMap<>();

    // Method to display poll details for admins
    public void showAdminPollDetails(Stage primaryStage, int poll_ID, Map<String, String> userSession) throws Exception {
        this.userSession = userSession;

        // Main layout
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: transparent;");


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

        // Back button
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #C8F0FF; -fx-font-weight: bold; -fx-border-radius: 10px; -fx-font-size: 22px;" +
                "-fx-cursor: hand");
        backButton.setOnAction(event -> {
            try {
                AdminLandingPageForm adminLandingPageController = new AdminLandingPageForm();
                adminLandingPageController.showAdminLandingPage(primaryStage, userSession);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // Align logo to the left and back button to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        topBar.getChildren().addAll(logo, spacer, backButton);
        layout.setTop(topBar);


        // Poll Info Section
        VBox pollInfoSection = new VBox(15);
        pollInfoSection.setPadding(new Insets(40));
        pollInfoSection.setAlignment(Pos.TOP_CENTER);
        pollInfoSection.setPrefWidth(300);

        Label pollTitleLabel = new Label("Poll Title: " + PollController.getPollByPoll_ID(poll_ID).optString("title"));
        pollTitleLabel.setStyle("-fx-font-size: 50px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        Label pollDescriptionLabel = new Label("Description: " + PollController.getPollByPoll_ID(poll_ID).optString("description"));
        pollDescriptionLabel.setStyle("-fx-font-size: 25px; -fx-text-fill: #555555;");


        // Poll status
        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #000000;");

        String startDateStr = PollController.getPollByPoll_ID(poll_ID).optString("start_date", null);
        String endDateStr = PollController.getPollByPoll_ID(poll_ID).optString("end_date", null);

        LocalDate startLocalDate = startDateStr != null ? LocalDate.parse(startDateStr) : null;
        LocalDate endLocalDate = endDateStr != null ? LocalDate.parse(endDateStr) : null;

        LocalDate today = LocalDate.now();
        long daysLeft = ChronoUnit.DAYS.between(today, endLocalDate);

        // Set status text based on time
        if (today.isBefore(startLocalDate)) {
            statusLabel.setText("Status: Inactive");
            statusLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: Gray;");
        } else if (daysLeft > 0) {
            statusLabel.setText("Status: Active • " + daysLeft + " day(s) left");
            statusLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: Green;");
        } else if (daysLeft == 0) {
            statusLabel.setText("Status: Last day to vote!");
            statusLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: Orange;");
        } else {
            statusLabel.setText("Status: Completed");
            statusLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: Red;");
        }


        // Pie Chart for Candidate Votes
        Label chartTitle = new Label("Candidate Votes Breakdown:");
        chartTitle.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        // Smaller space between chart title and the Pie Chart
        Region space = new Region();
        space.setPrefHeight(2); // Reduced height for smaller gap

        LandingPageController landingPageController = new LandingPageController();
        List<JSONObject> candidates = landingPageController.getCandidatesWithVotesByPollID(poll_ID);

        // Calculate total votes
        int totalVotes = candidates.stream()
                .mapToInt(c -> c.optInt("voteCount", 0))
                .sum();

        PieChart pieChart = new PieChart();
        for (JSONObject candidate : candidates) {
            int votes = candidate.optInt("voteCount", 0);
            PieChart.Data slice = new PieChart.Data(candidate.optString("name"), (double) votes * 100 / totalVotes);
            pieChart.getData().add(slice);
        }
        pieChart.setLegendVisible(true);
        pieChart.setLabelsVisible(true);
        pieChart.setPrefWidth(400);
        pieChart.setPrefHeight(400);


        if(totalVotes != 0) {
            // Winner Display if Poll is Completed
            Label winnerLabel = null;
            if (daysLeft < 0) {
                winnerLabel = new Label("Winner: " + new PollController().getWinnerByPoll_ID(poll_ID).optString("name"));
                winnerLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: Black;");
                pollInfoSection.getChildren().add(winnerLabel);
            }
        } else {
            Label noWinnerLabel = new Label("Winner: No winner");
            if (daysLeft < 0) {
                noWinnerLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: Black;");
                pollInfoSection.getChildren().add(noWinnerLabel);
            }
        }

        pollInfoSection.getChildren().addAll(pollTitleLabel, pollDescriptionLabel, statusLabel, chartTitle, space, pieChart);
        layout.setCenter(pollInfoSection);


        // Scene setup
        Scene scene = new Scene(layout, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight() - 80);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
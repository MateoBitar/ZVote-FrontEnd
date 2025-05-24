package com.example.zvotefrontend.Forms;

import com.example.zvotefrontend.Controllers.LandingPageController;
import com.example.zvotefrontend.Controllers.PollController;
import com.example.zvotefrontend.Controllers.UserController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.zvotefrontend.Forms.SignInForm.showAlert;


public class PollForm {

    private final PollController controller = new PollController();
    public static Map<String, String> userSession = new HashMap<>();

    // Method to display poll details
    public void showPollDetails(Stage primaryStage, int poll_ID, Map<String, String> userSession) throws Exception {
        JSONObject poll = PollController.getPollByPoll_ID(poll_ID);
        this.userSession = userSession;

        // Main layout
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: transparent;");


        // Top Bar Configuration
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

        // Back Button
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #C8F0FF; -fx-font-weight: bold; -fx-border-radius: 10px; -fx-font-size: 22px;" +
                " -fx-cursor: hand");
        backButton.setOnAction(event -> {
            try {
                LandingPageForm landingPageForm = new LandingPageForm();
                landingPageForm.showLandingPage(primaryStage, userSession);
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

        Label pollTitleLabel = new Label("Poll Title: " + poll.optString("title"));
        pollTitleLabel.setStyle("-fx-font-size: 50px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        Label pollDescriptionLabel = new Label("Description: " + poll.optString("description"));
        pollDescriptionLabel.setStyle("-fx-font-size: 25px; -fx-text-fill: #555555;");


        // Poll Status Label
        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #000000;");

        String startDateStr = poll.optString("start_date", null);
        String endDateStr = poll.optString("end_date", null);

        LocalDate startLocalDate = startDateStr != null ? LocalDate.parse(startDateStr) : null;
        LocalDate endLocalDate = endDateStr != null ? LocalDate.parse(endDateStr) : null;
        LocalDate today = LocalDate.now();
        long daysLeft = ChronoUnit.DAYS.between(today, endLocalDate);

        // Set status text based on the current date
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
                .mapToInt(c -> c.optInt("votes", 0))
                .sum();

        PieChart pieChart = new PieChart();
        for (JSONObject candidate : candidates) {
            int votes = candidate.optInt("votes", 0);
            PieChart.Data slice = new PieChart.Data(candidate.optString("name"), controller.getVotePercentage(votes, totalVotes));
            pieChart.getData().add(slice);
        }
        pieChart.setLegendVisible(true);
        pieChart.setLabelsVisible(true);
        pieChart.setPrefWidth(400);
        pieChart.setPrefHeight(400);


        // Display Winner if Poll is Completed
        Label winnerLabel = null;
        if (daysLeft < 0) {
            winnerLabel = new Label("Winner: " + controller.getWinnerByPoll_ID(poll_ID).optString("name"));
            winnerLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: Black;");
        }


        // Vote Button for Active Polls
        Button voteButton = new Button("Vote");
        voteButton.setStyle("-fx-background-color: #C8F0FF; -fx-font-weight: bold; -fx-border-radius: 10px; -fx-font-size: 18px;" +
                "-fx-cursor: hand");
        voteButton.setOnAction(e -> {
            try {
                showVotingSection(primaryStage, poll, userSession);
            } catch (Exception exe) {
                throw new RuntimeException(exe);
            }
        });

        pollInfoSection.getChildren().addAll(pollTitleLabel, pollDescriptionLabel, statusLabel, chartTitle, space, pieChart);

        boolean hasVoted = controller.hasUserVoted(UserController.getUserByUsername(userSession.get("username")).optInt("user_ID"), poll_ID);

        if (today.isBefore(startLocalDate)) {
            Label inactiveLabel = new Label("Voting Starts On • " + startLocalDate);
            inactiveLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: Black;");
            pollInfoSection.getChildren().add(inactiveLabel);
        } else if (daysLeft >= 0) {
            if (!hasVoted) {
                pollInfoSection.getChildren().add(voteButton);
            } else {
                Label votedLabel = new Label("You Voted Already!");
                votedLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: Black;");
                pollInfoSection.getChildren().add(votedLabel);
            }
        } else {
            pollInfoSection.getChildren().add(winnerLabel);
        }

        layout.setCenter(pollInfoSection);


        // Scene Setup
        Scene scene = new Scene(layout, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight() - 80);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    private void showVotingSection(Stage primaryStage, JSONObject poll, Map<String,String> userSession) throws Exception {
        // Main voting layout
        VBox votingLayout = new VBox(30);  // Vertical layout with spacing of 30px
        votingLayout.setAlignment(Pos.TOP_CENTER);  // Align items at the top and center


        // Top Bar Configuration
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(10, 10, 10, 40));
        topBar.setStyle("-fx-background-color: #C8F0FF;");
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setMaxWidth(Double.MAX_VALUE);  // Ensure the top bar spans the full width


        // Add shadow effect to the top bar
        DropShadow shadow = new DropShadow();
        shadow.setRadius(5);
        shadow.setOffsetY(2);
        shadow.setColor(Color.LIGHTGRAY);
        topBar.setEffect(shadow);


        // Logo
        Label logo = new Label("ZVote");
        logo.setFont(Font.font("Onyx", FontWeight.BOLD, 60));


        // Back Button for returning to poll details
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #C8F0FF; -fx-font-weight: bold; -fx-border-radius: 10px; -fx-font-size: 22px;" +
                " -fx-cursor: hand");
        backButton.setOnAction(event -> {
            try {
                showPollDetails(primaryStage, poll.optInt("poll_ID"), userSession);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });


        // Align logo to the left and back button to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        topBar.getChildren().addAll(logo, spacer, backButton);


        // Candidate Label
        Label candidateLabel = new Label("Vote for your Candidate");
        candidateLabel.setStyle("-fx-font-size: 50px; -fx-font-weight: bold;");


        // Candidates Section
        LandingPageController landingPageController = new LandingPageController();
        List<JSONObject> candidates = landingPageController.getCandidatesWithVotesByPollID(poll.optInt("poll_ID"));

        ToggleGroup candidatesGroup = new ToggleGroup();  // Group for RadioButtons to ensure only one is selected


        VBox candidatesSection = new VBox(10);  // Vertical layout with spacing of 10px
        candidatesSection.setAlignment(Pos.CENTER);
        for (JSONObject candidate : candidates) {
            RadioButton candidateButton = new RadioButton(candidate.optString("name"));
            candidateButton.setToggleGroup(candidatesGroup);
            candidateButton.setUserData(candidate.optInt("candidate_ID"));  // Associate candidate ID with button
            candidateButton.setStyle("-fx-font-size: 20px; -fx-cursor: hand");
            candidatesSection.getChildren().add(candidateButton);
        }


        // Submit Button
        Button submitButton = new Button("Submit Vote");
        submitButton.setStyle("-fx-background-color: #C8F0FF; -fx-font-weight: bold; -fx-border-radius: 10px; -fx-font-size: 22px;" +
                " -fx-cursor: hand");
        submitButton.setOnAction(event -> {
            RadioButton selectedCandidate = (RadioButton) candidatesGroup.getSelectedToggle();

            if (selectedCandidate == null) {  // No candidate selected, submit as abstention
                try {
                    JSONObject abstainVote = new JSONObject();

                    abstainVote.put("user_ID", UserController.getUserByUsername(userSession.get("user_ID")).optInt("user_ID"));
                    abstainVote.put("poll_ID", poll.optInt("poll_ID"));

                    // Set current timestamp
                    abstainVote.put("timestamp", Instant.now().toString());

                    // Set candidate_ID to JSONObject.NULL to serialize it as null in JSON
                    abstainVote.put("candidate_ID", JSONObject.NULL);

                    PollController.addVote(abstainVote);

                    // Update poll info
                    poll.put("nbOfVotes", poll.optInt("nbOfVotes") + 1);
                    poll.put("nbOfAbstentions", poll.optInt("nbOfAbstentions") + 1);
                    controller.updatePoll(poll);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Vote Submitted");
                    alert.setContentText("Your abstention vote has been successfully recorded.");
                    alert.showAndWait();

                    new LandingPageForm().showLandingPage(primaryStage, userSession);

                } catch (Exception e) {
                    handleVoteError(e);
                }
            } else {  // Candidate selected, submit vote for candidate
                try {
                    int pollId = poll.optInt("poll_ID");
                    int candidateId = (int) selectedCandidate.getUserData();
                    int voterId = UserController.getUserByUsername(userSession.get("username")).optInt("user_ID");

                    JSONObject Vote = new JSONObject();
                    Vote.put("user_ID", voterId);
                    Vote.put("poll_ID", pollId);

                    // Set current timestamp
                    Vote.put("timestamp", Instant.now().toString());

                    Vote.put("candidate_ID", candidateId);

                    PollController.addVote(Vote);

                    JSONObject result = controller.getResultByPollAndCandidate(pollId, candidateId);

                    if (result == null) {
                        showAlert(Alert.AlertType.ERROR, "Failed to submit vote", "No result object found for this poll and candidate!");
                    } else {
                        result.put("votes_casted", result.optInt("votes_casted") + 1);
                        controller.updateResult(result);

                        poll.put("nbOfVotes", poll.optInt("nbOfVotes") + 1);
                        controller.updatePoll(poll);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setContentText("Your vote has been submitted successfully!");
                        alert.showAndWait();

                        new LandingPageForm().showLandingPage(primaryStage, userSession);
                    }
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while submitting your vote. Please try again.");
                }
            }
        });


        // Add components to the voting layout
        votingLayout.getChildren().addAll(topBar, candidateLabel, candidatesSection, submitButton);


        // Background Image
        ImageView backgroundImageView = new ImageView(new Image(getClass().getResource("/images/VotePic.png").toExternalForm()));
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setFitWidth(Screen.getPrimary().getBounds().getWidth());
        backgroundImageView.setFitHeight(Screen.getPrimary().getBounds().getHeight() - 80);


        // Root Pane Setup
        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundImageView, votingLayout);


        // Scene Setup
        Scene scene = new Scene(root, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight() - 80);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    // Helper Method to Handle Vote Errors
    private void handleVoteError(Exception e) {
        if (e.getMessage().contains("User has already voted")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Duplicate Vote");
            alert.setContentText("You have already voted in this poll. Multiple votes are not allowed.");
            alert.showAndWait();
        } else {
            throw new RuntimeException(e);  // Handle other unexpected exceptions
        }
    }
}

package com.example.zvotefrontend.Forms;

import com.example.zvotefrontend.Controllers.PollController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONObject;

public class PollForm {
    private Stage primaryStage;
    private JSONObject currentPoll;

    public PollForm(Stage primaryStage, int pollId) {
        this.primaryStage = primaryStage;
        this.currentPoll = PollController.getPollByPoll_ID(pollId);
    }

    public void showPollDetails() {
        VBox pollInfoSection = new VBox(20);
        pollInfoSection.setPadding(new Insets(20));
        pollInfoSection.setAlignment(Pos.CENTER);

        Label title = new Label("Poll Title: " + currentPoll.getString("title"));
        Label description = new Label("Description: " + currentPoll.getString("description"));
        Label status = new Label("Status: " + currentPoll.getString("status"));

        Button voteButton = new Button("Vote");
        voteButton.setOnAction(e -> showVotingForm());

        pollInfoSection.getChildren().addAll(title, description, status, voteButton);

        Scene scene = new Scene(pollInfoSection, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showVotingForm() {
        VBox votingSection = new VBox(20);
        votingSection.setPadding(new Insets(20));
        votingSection.setAlignment(Pos.CENTER);

        Label voteTitle = new Label("Vote for a Candidate");
        ToggleGroup candidateGroup = new ToggleGroup();

        for (Object candidateObj : currentPoll.getJSONArray("candidates")) {
            JSONObject candidate = (JSONObject) candidateObj;
            RadioButton candidateButton = new RadioButton(candidate.getString("name"));
            candidateButton.setToggleGroup(candidateGroup);
            candidateButton.setUserData(candidate.getInt("id"));
            votingSection.getChildren().add(candidateButton);
        }

        Button submitButton = new Button("Submit Vote");
        submitButton.setOnAction(e -> {
            RadioButton selectedCandidate = (RadioButton) candidateGroup.getSelectedToggle();
            if (selectedCandidate != null) {
                JSONObject voteData = new JSONObject();
                voteData.put("pollId", currentPoll.getInt("id"));
                voteData.put("candidateId", (int) selectedCandidate.getUserData());
                PollController.addVote(voteData);
            }
            showPollDetails();
        });

        votingSection.getChildren().addAll(voteTitle, submitButton);

        Scene voteScene = new Scene(votingSection, 500, 500);
        primaryStage.setScene(voteScene);
    }
}
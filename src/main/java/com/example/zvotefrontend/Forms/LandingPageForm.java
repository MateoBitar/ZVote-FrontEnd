package com.example.zvotefrontend.Forms;

import com.example.zvotefrontend.Controllers.LandingPageController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import org.json.JSONObject;
import java.util.List;

public class LandingPageForm {

    private final LandingPageController controller = new LandingPageController();  // API Connection

    public Scene createLandingScene(Stage primaryStage) {
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #FFFFFF");

        // Top Bar
        VBox topBar = new VBox(10);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: #C8F0FF;");
        topBar.setAlignment(Pos.CENTER);

        Label logo = new Label("ZVote");
        logo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        TextField searchBar = new TextField();
        searchBar.setPromptText("Search...");
        searchBar.setPrefWidth(300);

        MenuButton profileMenu = new MenuButton("\uD83D\uDC64");
        profileMenu.setStyle("-fx-font-size: 20px; -fx-background-color: #C8F0FF; -fx-text-fill: black;");

        topBar.getChildren().addAll(logo, searchBar, profileMenu);
        layout.setTop(topBar);

        // Create Poll Grid & Fetch Polls
        GridPane pollGrid = createPollGrid(searchBar);
        layout.setCenter(pollGrid);

        return new Scene(layout, 800, 600);
    }

    private GridPane createPollGrid(TextField searchBar) {
        GridPane pollGrid = new GridPane();
        pollGrid.setHgap(30);
        pollGrid.setVgap(30);
        pollGrid.setAlignment(Pos.CENTER);

        updatePollGrid(pollGrid, "");  // Load polls at startup

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> updatePollGrid(pollGrid, newValue));

        return pollGrid;
    }

    private void updatePollGrid(GridPane pollGrid, String query) {
        List<JSONObject> polls = controller.fetchPolls();  // Fetch API data

        pollGrid.getChildren().clear();
        int col = 0, row = 0;
        for (JSONObject poll : polls) {
            if (poll.getString("title").toLowerCase().contains(query.toLowerCase()) ||
                    poll.getString("description").toLowerCase().contains(query.toLowerCase())) {
                pollGrid.add(createPollCard(poll), col, row);
                col++;
                if (col == 4) { col = 0; row++; }
            }
        }
    }

    // PollCard UI inside LandingPage
    private VBox createPollCard(JSONObject poll) {
        VBox pollCard = new VBox();
        pollCard.setAlignment(Pos.TOP_CENTER);
        pollCard.setPadding(new Insets(10));
        pollCard.setStyle("-fx-background-color: #C8F0FF; -fx-border-radius: 10px; -fx-border-width: 3px; -fx-border-color: #000000;");
        pollCard.setPrefSize(300, 400);

        // Poll Title
        Label pollLabel = new Label(poll.getString("title"));
        pollLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: black;");
        pollLabel.setWrapText(true);

        // Poll Description
        Label descriptionLabel = new Label(poll.getString("description"));
        descriptionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");
        descriptionLabel.setWrapText(true);

        // Vote Progress Bar (Mocked)
        ProgressBar voteBar = new ProgressBar(Math.random());

        pollCard.getChildren().addAll(pollLabel, descriptionLabel, voteBar);
        return pollCard;
    }
}
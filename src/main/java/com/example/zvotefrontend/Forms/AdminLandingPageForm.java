package com.example.zvotefrontend.Forms;

import com.example.zvotefrontend.Controllers.AdminLandingPageController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

public class AdminLandingPageForm {
    private TabPane tabPane;
    private Stage primaryStage;
    private TextField searchBar;
    private GridPane pollGrid;

    public void showAdminLandingPage(Stage primaryStage) {
        this.primaryStage = primaryStage;

        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #FFFFFF");

        setupTopBar(layout);
        setupTabs(layout);

        Scene scene = new Scene(layout, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight() - 80);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        // Call the controller to fetch polls
        AdminLandingPageController controller = new AdminLandingPageController();
        JSONArray polls = controller.getPolls();

        // Populate UI with polls received from API
        populatePollGrid(polls);
    }

    private void setupTopBar(BorderPane layout) {
        searchBar = new TextField();
        searchBar.setPromptText("Search...");
        searchBar.setPrefWidth(300);

        Label logo = new Label("ZVote");
        logo.setFont(Font.font("Onyx", FontWeight.BOLD, 60));

        HBox topBar = new HBox(20, logo, searchBar);
        topBar.setPadding(new Insets(10, 10, 10, 40));
        topBar.setStyle("-fx-background-color: #C8F0FF;");
        topBar.setEffect(new DropShadow(5, Color.LIGHTGRAY));

        layout.setTop(topBar);
    }

    private void setupTabs(BorderPane layout) {
        Tab add = new Tab("Add");
        Tab delete = new Tab("Delete");

        tabPane = new TabPane(add, delete);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setStyle("-fx-font-weight: bold;");

        pollGrid = new GridPane(); // Grid where polls will be populated
        layout.setCenter(tabPane);
    }

    private void populatePollGrid(JSONArray polls) {
        pollGrid.getChildren().clear();

        for (int i = 0; i < polls.length(); i++) {
            JSONObject poll = polls.getJSONObject(i);
            VBox pollCard = createPollCard(poll);
            pollGrid.add(pollCard, (i % 4), (i / 4));
        }
    }

    private VBox createPollCard(JSONObject poll) {
        VBox pollCard = new VBox();
        pollCard.setStyle("-fx-background-color: #C8F0FF; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        Label titleLabel = new Label(poll.getString("title"));
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label statusLabel = new Label(poll.getString("status"));
        statusLabel.setStyle("-fx-font-size: 16px;");

        pollCard.getChildren().addAll(titleLabel, statusLabel);
        return pollCard;
    }
}
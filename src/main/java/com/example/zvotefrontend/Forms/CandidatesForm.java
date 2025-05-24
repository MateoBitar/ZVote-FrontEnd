package com.example.zvotefrontend.Forms;

import com.example.zvotefrontend.Controllers.CandidatesController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class CandidatesForm {

    private final CandidatesController controller = new CandidatesController();
    private TabPane tabPane;
    private TextField searchBar;
    private GridPane addCandidateGrid;
    private GridPane deleteCandidateGrid;

    public void showCandidatesPage(Stage primaryStage, Map<String, String> usersession) {
        // Main layout
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #FFFFFF;");

        // Search Bar
        searchBar = new TextField();
        searchBar.setPromptText("Search...");
        searchBar.setPrefWidth(300);
        searchBar.setTranslateX(Screen.getPrimary().getVisualBounds().getWidth() / 2 - 300);
        searchBar.setStyle("-fx-background-color: #FFFFFF; -fx-font-size: 14px; -fx-border-color: #C8F0FF; -fx-border-radius: 30px; -fx-background-radius: 30px;");
        searchBar.setFocusTraversable(true);

        // Logo
        Label logo = new Label("ZVote");
        logo.setFont(Font.font("Onyx", FontWeight.BOLD, 60));

        // Polls Button
        Button pollIcon = createStyledButton("\uD83D\uDCCB", 70, 30);

        // Profile Menu Button
        MenuButton profileMenu = createStyledMenuButton("\uD83D\uDC64", 75, 30);

        HBox topBar = new HBox(20, logo, searchBar);
        topBar.setPadding(new Insets(10, 10, 10, 40));
        topBar.setStyle("-fx-background-color: #C8F0FF;");
        topBar.setAlignment(Pos.CENTER);
        topBar.setEffect(new DropShadow(5, 2, 2, Color.LIGHTGRAY));

        layout.setTop(topBar);

        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setStyle("-fx-font-weight: bold;");
        Tab add = new Tab("Add");
        Tab delete = new Tab("Delete");
        tabPane.getTabs().addAll(add, delete);

        addCandidateGrid = new GridPane();
        deleteCandidateGrid = new GridPane();

        populateCandidateGrid(addCandidateGrid, controller.getAllCandidates());
        populateCandidateGrid(deleteCandidateGrid, controller.getAllCandidates());

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            populateCandidateGrid(addCandidateGrid, controller.filterCandidates(newValue));
            populateCandidateGrid(deleteCandidateGrid, controller.filterCandidates(newValue));
        });

        add.setContent(new ScrollPane(addCandidateGrid));
        delete.setContent(new ScrollPane(deleteCandidateGrid));

        layout.setCenter(tabPane);

        Scene scene = new Scene(layout, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight() - 80);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void populateCandidateGrid(GridPane candidateGrid, List<JSONObject> candidates) {
        candidateGrid.getChildren().clear();
        int row = 0, col = 0;

        for (JSONObject candidate : candidates) {
            VBox candidateCard = new VBox();
            candidateCard.setAlignment(Pos.TOP_CENTER);
            candidateCard.setPadding(new Insets(10));
            candidateCard.setStyle("-fx-background-color: #C8F0FF; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-border-width: 3px; -fx-border-color: #000000;");
            candidateCard.setPrefSize(300, 400);

            Label nameLabel = new Label(candidate.getString("name"));
            nameLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

            Label bioLabel = new Label(candidate.getString("bio"));
            bioLabel.setStyle("-fx-font-size: 16px;");

            candidateCard.getChildren().addAll(nameLabel, bioLabel);
            candidateGrid.add(candidateCard, col, row);

            col++;
            if (col >= 4) {
                col = 0;
                row++;
            }
        }
    }

    // UI helper methods for styled buttons
    private Button createStyledButton(String text, int width, int height) {
        Button button = new Button(text);
        button.setPrefSize(width, height);
        button.setStyle("-fx-font-family: Onyx; -fx-font-size: 25; -fx-background-color: #C8F0FF; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand;");
        return button;
    }

    private MenuButton createStyledMenuButton(String text, int width, int height) {
        MenuButton menuButton = new MenuButton(text);
        menuButton.setPrefSize(width, height);
        menuButton.setStyle("-fx-font-family: Arial; -fx-font-size: 20; -fx-background-color: #C8F0FF; -fx-text-fill: black; -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand;");
        return menuButton;
    }

}
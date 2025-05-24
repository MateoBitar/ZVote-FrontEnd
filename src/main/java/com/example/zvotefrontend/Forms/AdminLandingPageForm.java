package com.example.zvotefrontend.Forms;

import com.example.zvotefrontend.Controllers.AdminLandingPageController;
import com.example.zvotefrontend.Controllers.LandingPageController;
import com.example.zvotefrontend.Main;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;


public class AdminLandingPageForm {

    private AdminLandingPageController controller = new AdminLandingPageController();  // API Connection

    TabPane tabPane;  // Tab pane for navigating between add and delete operations
    Stage primaryStage;  // Primary stage for the application
    Map<String, String> userSession;  // Holds session details for the logged-in user


    // Method to display the admin landing page
    public void showAdminLandingPage(Stage primaryStage, Map<String, String> userSession) throws Exception {
        this.primaryStage = primaryStage;
        this.userSession = userSession;

        // Main layout
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #FFFFFF");


        // Search Bar Configuration
        TextField searchBar = new TextField();
        searchBar.setPromptText("Search...");
        searchBar.setPrefWidth(300);
        searchBar.setTranslateX(Screen.getPrimary().getVisualBounds().getWidth() / 2 - 300);
        searchBar.setStyle("-fx-background-color: #FFFFFF; -fx-font-size: 14px; -fx-border-color: #C8F0FF; -fx-border-radius: 30px;" +
                "-fx-background-radius: 30px;");
        searchBar.setFocusTraversable(true);  // Ensure the search bar can always regain focus


        // Logo Configuration
        Label logo = new Label("ZVote");
        logo.setFont(Font.font("Onyx", FontWeight.BOLD, 60));


        // Polls Button Configuration
        Button pollIcon = new Button("\uD83D\uDCCB");  // Unicode for clipboard icon
        pollIcon.setStyle("-fx-font-family: Onyx; -fx-font-size: 25; -fx-background-color: #C8F0FF; -fx-text-fill: white;" +
                " -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand;");
        pollIcon.setPrefSize(70, 30);
        pollIcon.setTranslateX(150);


        // Profile Menu Configuration
        MenuButton profileMenu = new MenuButton("\uD83D\uDC64");  // Unicode for user icon
        profileMenu.setStyle("-fx-font-family: Arial; -fx-font-size: 20; -fx-background-color: #C8F0FF; -fx-text-fill: black;" +
                " -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand;");
        profileMenu.setPrefSize(75, 30);
        profileMenu.setTranslateX(200);

        // Add hover effects for the Profile Menu
        profileMenu.setOnMouseEntered(e -> profileMenu.setStyle(profileMenu.getStyle().replace("-fx-text-fill: black;", "-fx-text-fill: white;")));
        profileMenu.setOnMouseExited(e -> profileMenu.setStyle(profileMenu.getStyle().replace("-fx-text-fill: white;", "-fx-text-fill: black;")));


        // Menu Items for Profile Menu
        MenuItem candidatesItem = new MenuItem("Candidates");
        candidatesItem.setOnAction(e -> {
            try {
                new CandidatesForm().showCandidatesPage(primaryStage, userSession.get("username"));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        MenuItem userInfoItem = new MenuItem("User Info");
        userInfoItem.setOnAction(e -> {
            UserForm userForm = new UserForm();
            userForm.showUserProfile(primaryStage, userSession.get("username"));
        });

        MenuItem logoutItem = new MenuItem("Log Out");
        logoutItem.setOnAction(e -> {
            primaryStage.close();
            new Main().start(new Stage());
        });

        MenuItem logoffItem = new MenuItem("Close Application");
        logoffItem.setOnAction(e -> primaryStage.close());

        profileMenu.getItems().addAll(candidatesItem, userInfoItem, logoutItem, logoffItem);


        pollIcon.setFocusTraversable(false);
        profileMenu.setFocusTraversable(false);


        // Menu Icon for Animations
        Label menuIcon = new Label("\u283F");  // Unicode for menu icon
        menuIcon.setStyle("-fx-font-size: 24px; -fx-padding: 10px; -fx-cursor: hand;");
        menuIcon.setOnMouseClicked(e -> animateMenu(pollIcon, profileMenu));


        // Menu Layout
        HBox menu = new HBox(-10);
        menu.getChildren().addAll(pollIcon, profileMenu, menuIcon);
        menu.setAlignment(Pos.CENTER_RIGHT);


        // Top Bar Configuration
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(10, 10, 10, 40));
        topBar.setStyle("-fx-background-color: #C8F0FF;");
        topBar.setAlignment(Pos.CENTER);

        // Add shadow effect to the top bar
        DropShadow shadow = new DropShadow();
        shadow.setRadius(5);
        shadow.setOffsetY(2);
        shadow.setColor(Color.LIGHTGRAY);
        topBar.setEffect(shadow);

        // Add components to the top bar
        HBox.setHgrow(menu, Priority.ALWAYS);
        topBar.getChildren().addAll(logo, searchBar, menu);
        layout.setTop(topBar);


        // Tabs Configuration
        Tab add = new Tab("Add");
        Tab delete = new Tab("Delete");

        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setStyle("-fx-font-weight: bold;");
        tabPane.getTabs().addAll(add, delete);


        // Add Tab Content
        VBox addcontent = new VBox(10);
        addcontent.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());
        addcontent.setAlignment(Pos.CENTER);
        addcontent.setPadding(new Insets(5, 0, 0, 0));
        addcontent.setStyle("-fx-background-color: #FFFFFF;");


        // Delete Tab Content
        VBox deletecontent = new VBox(10);
        deletecontent.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());
        deletecontent.setAlignment(Pos.CENTER);
        deletecontent.setPadding(new Insets(5, 0, 0, 0));
        deletecontent.setStyle("-fx-background-color: #FFFFFF;");


        // Add Poll Button with Image
        ImageView addImageViewButton = new ImageView(new Image(getClass().getResource("/images/Plus Sign.png").toExternalForm()));
        addImageViewButton.setFitHeight(30);
        addImageViewButton.setFitWidth(30);

        Button addPollButton = new Button("Add Poll");
        addPollButton.setGraphic(addImageViewButton);
        addPollButton.setStyle("-fx-background-color: transparent; -fx-border-color: #C8F0FF; -fx-background-radius: 20px; -fx-border-radius: 20px;" +
                "-fx-border-width: 3px; -fx-border-style: dashed; -fx-cursor: hand;");
        addPollButton.setOnAction(e -> {
            try {
                new AdminPollForm().showCreatePoll(primaryStage, userSession.get("username"));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });


        // Wrap the pollGrid in a ScrollPane for Add tab
        ScrollPane addScrollPane = new ScrollPane();
        addScrollPane.setContent(addcontent);  // Set the content of the ScrollPane to addContent
        addScrollPane.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight() - (topBar.getHeight() + addPollButton.getHeight()));  // Adjust height based on screen bounds
        addScrollPane.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth() - 50);  // Adjust width based on screen bounds
        addScrollPane.setStyle("-fx-background-color: transparent;");  // Transparent background for the ScrollPane
        addScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);  // Show vertical scroll bar when needed
        addScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);  // Hide horizontal scroll bar


        // Wrap the pollGrid in a ScrollPane for Delete tab
        ScrollPane deleteScrollPane = new ScrollPane();
        deleteScrollPane.setContent(deletecontent);  // Set the content of the ScrollPane to deleteContent
        deleteScrollPane.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight() - (topBar.getHeight() + addPollButton.getHeight()));  // Adjust height based on screen bounds
        deleteScrollPane.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth() - 50);  // Adjust width based on screen bounds
        deleteScrollPane.setStyle("-fx-background-color: transparent;");  // Transparent background for the ScrollPane
        deleteScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);  // Show vertical scroll bar when needed
        deleteScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);  // Hide horizontal scroll bar


        // Grid for Add Tab
        GridPane addPollGrid = new GridPane();
        addPollGrid.setHgap(30);  // Horizontal spacing between grid items
        addPollGrid.setVgap(30);  // Vertical spacing between grid items
        addPollGrid.setAlignment(Pos.CENTER);  // Center align grid content
        addPollGrid.setPadding(new Insets(10, 0, 0, 0));  // Add padding to the grid
        addPollGrid.setFocusTraversable(false);  // Disable focus traversal for the grid
        addPollGrid.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> searchBar.requestFocus());  // Refocus search bar when grid is clicked


        // Grid for Delete Tab
        GridPane deletePollGrid = new GridPane();
        deletePollGrid.setHgap(30);  // Horizontal spacing between grid items
        deletePollGrid.setVgap(30);  // Vertical spacing between grid items
        deletePollGrid.setAlignment(Pos.CENTER);  // Center align grid content
        deletePollGrid.setPadding(new Insets(10, 0, 0, 0));  // Add padding to the grid
        deletePollGrid.setFocusTraversable(false);  // Disable focus traversal for the grid
        deletePollGrid.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> searchBar.requestFocus());  // Refocus search bar when grid is clicked


        // Fetch all polls from the PollService
        List<JSONObject> allPolls = controller.getAllPolls();

        populatePollGrid(addPollGrid, allPolls);  // Populate Add tab grid with polls
        populatePollGrid(deletePollGrid, allPolls);  // Populate Delete tab grid with polls


        // Add listener to search bar to filter polls dynamically based on input
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                List<JSONObject> allPollsForFilter = controller.getAllPolls();

                // Filter polls using the query entered in the search bar
                List<JSONObject> filteredPolls = filterPolls(allPollsForFilter, newValue);

                // Determine the active tab and update the corresponding grid
                Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
                if (selectedTab.getText().equals("Add")) {
                    populatePollGrid(addPollGrid, filteredPolls);  // Update Add tab grid
                } else if (selectedTab.getText().equals("Delete")) {
                    populatePollGrid(deletePollGrid, filteredPolls);  // Update Delete tab grid
                }
            } catch (Exception e) {
                throw new RuntimeException(e);  // Handle exceptions appropriately
            }
        });


        // Add components to Add and Delete tabs
        addcontent.getChildren().addAll(addPollButton, addPollGrid);  // Add Button and Grid to Add tab content
        deletecontent.getChildren().addAll(deletePollGrid);  // Add Grid to Delete tab content


        // Set ScrollPane content to respective tabs
        add.setContent(addScrollPane);  // Assign ScrollPane to Add tab
        delete.setContent(deleteScrollPane);  // Assign ScrollPane to Delete tab


        // Add the TabPane to the center of the layout
        layout.setCenter(tabPane);


        // Scene and Stage setup
        Scene scene = new Scene(layout, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight() - 80);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);  // Disable resizing of the stage
        primaryStage.show();  // Display the admin landing page
    }


    // Tracks the menu state (open or closed)
    private static boolean isMenuOpen = false;


    // Animate menu items (Poll and Profile buttons)
    static void animateMenu(Button poll, MenuButton profile) {
        isMenuOpen = !isMenuOpen;  // Toggle state
        double targetX = isMenuOpen ? 0 : 150;  // Calculate target position based on state
        animateItem(poll, targetX);  // Animate poll button
        animateItem(profile, targetX);  // Animate profile menu button
    }


    // Animation for Button elements
    private static void animateItem(Button item, double targetX) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), item);  // Configure transition
        transition.setToX(targetX);  // Set target X position
        transition.play();  // Execute animation
    }


    // Animation for MenuButton elements
    private static void animateItem(MenuButton item, double targetX) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), item);  // Configure transition
        transition.setToX(targetX);  // Set target X position
        transition.play();  // Execute animation
    }


    // Method to filter polls based on a search query
    private List<JSONObject> filterPolls(List<JSONObject> allPolls, String query) {
        String lowerQuery = query.toLowerCase();

        return allPolls.stream()
                .filter(poll -> {
                    String title = poll.optString("title", null).toLowerCase();
                    String description = poll.optString("description", null).toLowerCase();

                    return title.contains(lowerQuery) || description.contains(lowerQuery);
                })
                .toList();
    }


    // Method to populate the poll grid with polls
    private void populatePollGrid(GridPane pollGrid, List<JSONObject> polls) throws Exception {
        pollGrid.getChildren().clear();  // Clear existing content in the grid

        // Loop through the polls and add poll cards to the grid
        for (JSONObject poll : polls) {
            VBox pollCard = createPollCard(poll);  // Create a poll card using the reusable method
            pollGrid.add(
                    pollCard,
                    (polls.indexOf(poll) % 4),  // Column index
                    (polls.indexOf(poll) / 4)   // Row index
            );

            pollCard.setFocusTraversable(false);  // Disable focus traversal for poll cards
        }
    }


    // Create a reusable poll card component
    private VBox createPollCard(JSONObject poll) throws Exception {
        VBox pollCard = new VBox();
        pollCard.setAlignment(Pos.TOP_CENTER);  // Align card content to the top-center
        pollCard.setPadding(new Insets(10));  // Add padding to card
        pollCard.setStyle("-fx-background-color: #C8F0FF; -fx-border-radius: 10px; -fx-background-radius: 10px;" +
                "-fx-border-width: 3px; -fx-border-color: #000000; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 6, 0.0, 3, 3);");
        pollCard.setPrefSize(300, 400);  // Set preferred size for poll card

        // Configure the poll label (title)
        Label pollLabel = new Label(poll.optString("title", null));
        pollLabel.setStyle("-fx-background-color: #FFFFFF; -fx-border-radius: 10px; -fx-background-radius: 10px;" +
                "-fx-border-width: 3px; -fx-border-color: #000000; -fx-font-size: 30px; -fx-font-weight: bold;");
        pollLabel.setAlignment(Pos.CENTER);  // Center align the label
        pollLabel.setWrapText(true);  // Enable text wrapping
        pollLabel.setMaxWidth(250);  // Set maximum width for the label
        pollLabel.setMinHeight(100);  // Set minimum height for the label
        pollLabel.setPadding(new Insets(0, 0, 0, 10));  // Add padding to label


        // Poll Status
        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #000000;");

        String startDateStr = poll.optString("start_date", null);
        String endDateStr = poll.optString("end_date", null);

        LocalDate startLocalDate = startDateStr != null ? LocalDate.parse(startDateStr) : null;
        LocalDate endLocalDate = endDateStr != null ? LocalDate.parse(endDateStr) : null;

        LocalDate today = LocalDate.now();
        long daysLeft = ChronoUnit.DAYS.between(today, endLocalDate);  // Calculate days remaining until the poll ends

        // Set status text and style based on time
        if (today.isBefore(startLocalDate)) {
            statusLabel.setText("Status: Inactive");
            statusLabel.setStyle("-fx-font-size:20px; -fx-font-weight: bold; -fx-text-fill: Gray;");
        } else if (daysLeft > 0) {
            statusLabel.setText("Status: Active • " + daysLeft + " day(s) left");
            statusLabel.setStyle("-fx-font-size:20px; -fx-font-weight: bold; -fx-text-fill: Green;");
        } else if (daysLeft == 0) {
            statusLabel.setText("Status: Last day to vote!");
            statusLabel.setStyle("-fx-font-size:20px; -fx-font-weight: bold; -fx-text-fill: Orange;");
        } else {
            statusLabel.setText("Status: Completed");
            statusLabel.setStyle("-fx-font-size:20px; -fx-font-weight: bold; -fx-text-fill: Red;");
        }

        // Candidates Section
        Label candidatesLabel = new Label("Candidates:");
        candidatesLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");

        VBox candidatesBox = new VBox(5);
        candidatesBox.setAlignment(Pos.TOP_LEFT);
        candidatesBox.setPadding(new Insets(20, 0, 0, 0));
        candidatesBox.getChildren().add(candidatesLabel);

        LandingPageController landingPageController = new LandingPageController();
        List<JSONObject> candidates = landingPageController.getCandidatesWithVotesByPollID(poll.optInt("poll_id"));


        for (JSONObject candidate : candidates) {
            String name = candidate.optString("name", "Unknown");
            double votePercentage = candidate.optDouble("votePercentage", 0.0);

            HBox candidateBox = new HBox(10);
            candidateBox.setAlignment(Pos.CENTER_LEFT);

            Label nameLabel = new Label(name);
            nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: normal;");

            Label percentageLabel = new Label(String.format("%.1f%%", votePercentage * 100));
            percentageLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: normal;");

            ProgressBar voteBar = new ProgressBar(votePercentage);
            voteBar.setPrefWidth(80);
            voteBar.progressProperty().addListener((observable, oldValue, newValue) -> {
                percentageLabel.setText(String.format("%.1f%%", newValue.doubleValue() * 100));
            });

            candidateBox.getChildren().addAll(nameLabel, voteBar, percentageLabel);
            candidatesBox.getChildren().add(candidateBox);
        }

        // Add spacing element for visual separation
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // View Poll Button configuration
        Button viewPollButton = new Button("View Poll");
        viewPollButton.setStyle(
                "-fx-background-color: #C8F0FF; " +
                        "-fx-text-fill: black; " +
                        "-fx-border-radius: 5px;" +
                        "-fx-border-color: #000000;" +
                        "-fx-border-width: 2px; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-cursor: hand; " +
                        "-fx-background-radius: 20;"
        );
        viewPollButton.setPrefHeight(30);

        viewPollButton.setOnAction(event -> {
            try {
                new AdminPollDetailsForm().showAdminPollDetails(primaryStage, poll.optInt("poll_ID"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });


        // Delete Poll Button with Image
        ImageView deleteImageViewButton = new ImageView(new Image(getClass().getResource("/images/Minus Sign.png").toExternalForm()));
        deleteImageViewButton.setFitHeight(20);
        deleteImageViewButton.setFitWidth(20);

        Button deletePollButton = new Button("Delete Poll");
        deletePollButton.setGraphic(deleteImageViewButton);
        deletePollButton.setStyle(
                "-fx-background-color: red; " +
                        "-fx-text-fill: black; " +
                        "-fx-border-radius: 5px;" +
                        "-fx-border-color: #000000;" +
                        "-fx-border-width: 2px; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-cursor: hand; " +
                        "-fx-background-radius: 5;"
        );
        deletePollButton.setPrefHeight(30);

        deletePollButton.setOnAction(event -> {
            try {
                // Fetch and delete all results associated with the poll
                List<JSONObject> results = controller.getPollResults(poll.optString("poll_ID"));
                if (results != null && !results.isEmpty()) {
                    for (JSONObject result : results) {
                        controller.deleteResult((String) result.opt("result_ID"));
                    }
                }

                // Fetch and delete all votes associated with the poll
                List<JSONObject> votes = controller.getPollVotes(poll.optString("poll_ID"));
                if (votes != null && !votes.isEmpty()) {
                    controller.deleteVotesByPoll(poll.optString("poll_ID"));
                }

                // Delete the poll itself
                controller.deletePoll(poll.optString("poll_ID"));

                // Reload the admin landing page
                showAdminLandingPage(primaryStage, userSession);

                // Explicitly reselect the "Delete" tab after the page reload
                Platform.runLater(() -> {
                    tabPane.getSelectionModel().select(tabPane.getTabs().stream()
                            .filter(tab -> tab.getText().equals("Delete"))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Delete Tab not found")));
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });


        // Add buttons and layout adjustments to the poll card
        pollCard.getChildren().addAll(pollLabel, statusLabel, candidatesBox, spacer, viewPollButton, deletePollButton);


        // Configure dynamic button behavior based on selected tab
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        if (selectedTab.getText().equals("Add")) {
            pollCard.getChildren().remove(deletePollButton);
        } else if (selectedTab.getText().equals("Delete")) {
            pollCard.getChildren().remove(viewPollButton);
        }

        // Add dynamic tab change listener for button visibility
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab.getText().equals("Add")) {
                pollCard.getChildren().remove(deletePollButton);
                pollCard.getChildren().add(viewPollButton);
            } else if (newTab.getText().equals("Delete")) {
                pollCard.getChildren().remove(viewPollButton);
                pollCard.getChildren().add(deletePollButton);
            }
        });

        // Hover effects for poll cards
        pollCard.setOnMouseEntered(e -> {
            TranslateTransition hoverIn = new TranslateTransition(Duration.millis(150), pollCard);
            hoverIn.setToY(-10);
            hoverIn.play();
        });

        pollCard.setOnMouseExited(e -> {
            TranslateTransition hoverOut = new TranslateTransition(Duration.millis(150), pollCard);
            hoverOut.setToY(0);
            hoverOut.play();
        });

        return pollCard;
    }
}

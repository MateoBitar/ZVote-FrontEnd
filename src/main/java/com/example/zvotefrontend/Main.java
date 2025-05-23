package com.example.zvotefrontend;

import com.example.zvotefrontend.Controllers.AboutUsController;
import com.example.zvotefrontend.Controllers.ContactUsController;
import com.example.zvotefrontend.Controllers.SignInController;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Top Bar
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(10, 10, 10, 40));
        topBar.setStyle("-fx-background-color: #C8F0FF;");

        DropShadow shadow = new DropShadow();
        shadow.setRadius(5);
        shadow.setOffsetY(2);
        shadow.setColor(Color.LIGHTGRAY);
        topBar.setEffect(shadow);

        Label logo = new Label("ZVote");
        logo.setFont(Font.font("Onyx", FontWeight.BOLD, 60));

        HBox menu = new HBox(-10);

        Button about = createStyledButton("About");
        Button contact = createStyledButton("Contact");
        Button profileIcon = createStyledButton("\uD83D\uDC64"); // Unicode for user icon

        menu.getChildren().addAll(about, contact, profileIcon);
        menu.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(menu, Priority.ALWAYS);
        topBar.getChildren().addAll(logo, menu);

        // Background Image
        StackPane content = new StackPane();
        content.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        ImageView votingImage = new ImageView(new Image(getClass().getResource("/images/Main Page.png").toExternalForm()));
        votingImage.setPreserveRatio(false);
        votingImage.fitWidthProperty().bind(content.widthProperty());
        votingImage.fitHeightProperty().bind(content.heightProperty());

        // Text Section
        VBox textSection = new VBox(20);
        textSection.setPadding(new Insets(50, 50, 250, 1100));

        Label title = new Label("ZVote");
        title.setFont(Font.font("Onyx", FontWeight.EXTRA_BOLD, 150));

        Label subtitle = new Label("Online Voting System");
        subtitle.setFont(Font.font("Onyx", FontWeight.BOLD, 60));

        Label tagline = new Label("Democracy At Your Fingertips");
        tagline.setFont(Font.font("Onyx", FontWeight.BOLD, 40));
        tagline.setStyle("-fx-opacity: 0.6;");

        Button voteNow = createStyledButton("Vote Now");

        textSection.getChildren().addAll(title, subtitle, tagline, voteNow);
        textSection.setAlignment(Pos.CENTER);

        content.getChildren().addAll(votingImage, textSection);
        StackPane.setAlignment(textSection, Pos.CENTER_RIGHT);
        VBox.setVgrow(content, Priority.ALWAYS);

        // Main Layout
        VBox mainLayout = new VBox(2);
        mainLayout.getChildren().addAll(topBar, content);
        Scene scene = new Scene(mainLayout, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight() - 80);

        primaryStage.setScene(scene);
        primaryStage.setTitle("ZVote - Online Voting System");
        primaryStage.setResizable(false);
        primaryStage.show();

        // Updated button actions → Controllers handle interactions
        about.setOnAction(e -> {
            AboutUsController aboutUsController = new AboutUsController();
            try {
                String aboutInfo = aboutUsController.fetchAboutInfo();
                System.out.println(aboutInfo); // Replace with actual display logic
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        contact.setOnAction(e -> {
            ContactUsController contactUsController = new ContactUsController();
            try {
                String contactInfo = contactUsController.fetchContactInfo();
                System.out.println(contactInfo); // Replace with actual display logic
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        profileIcon.setOnAction(e -> {
            SignInController signInController = new SignInController();
            try {
                String loginResponse = signInController.login("testUser", "testPassword"); // Replace with actual user input
                System.out.println(loginResponse); // Replace with actual handling
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        voteNow.setOnAction(e -> {
            SignInController signInController = new SignInController();
            try {
                String loginResponse = signInController.login("testUser", "testPassword"); // Replace with actual user input
                System.out.println(loginResponse); // Replace with actual handling
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-font-family: Onyx; -fx-font-size: 30; -fx-background-color: #C8F0FF; -fx-text-fill: black;" +
                " -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand");
        button.setPrefHeight(30);
        button.setPrefWidth(100);

        button.setOnMouseEntered(e -> button.setStyle(button.getStyle().replace("-fx-text-fill: black;", "-fx-text-fill: white;")));
        button.setOnMouseExited(e -> button.setStyle(button.getStyle().replace("-fx-text-fill: white;", "-fx-text-fill: black;")));
        return button;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
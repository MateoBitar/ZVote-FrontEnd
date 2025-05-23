package com.example.zvotefrontend.Forms;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class SignIn {

    public Scene createSignInScene(Stage primaryStage) {
        Stage signInStage = new Stage();
        signInStage.setTitle("Sign Up/Log In - ZVote");
        signInStage.setResizable(false);

        BorderPane layout = new BorderPane();
        HBox form = new HBox();
        form.setAlignment(Pos.CENTER);

        // Sign-Up Section
        VBox signInLayout = new VBox(15);
        signInLayout.setPadding(new Insets(20));
        signInLayout.setAlignment(Pos.TOP_LEFT);
        signInLayout.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #C8F0FF; -fx-border-width: 3px;");

        Label STitle = new Label("Sign Up");
        STitle.setStyle("-fx-font-size: 40px; -fx-font-weight: bold;");

        Label SUsernameLabel = new Label("Username:");
        SUsernameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        TextField SUsernameField = new TextField();
        SUsernameField.setPromptText("Enter your username");
        SUsernameField.setPrefWidth(250);

        Label SEmailLabel = new Label("Email:");
        SEmailLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        TextField SEmailField = new TextField();
        SEmailField.setPromptText("Enter your email");
        SEmailField.setPrefWidth(250);

        Label SPasswordLabel = new Label("Password:");
        SPasswordLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        PasswordField SPasswordField = new PasswordField();
        SPasswordField.setPromptText("Enter your password");
        SPasswordField.setPrefWidth(250);

        Label SPhoneLabel = new Label("Phone Number:");
        SPhoneLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        HBox SPhoneBox = new HBox(10);
        SPhoneBox.setAlignment(Pos.CENTER_LEFT);

        ComboBox<String> countryCodeDropdown = new ComboBox<>();
        countryCodeDropdown.getItems().addAll("+1 (USA)", "+961 (Lebanon)", "+81 (Japan)");
        countryCodeDropdown.setValue("+961");
        countryCodeDropdown.setPrefWidth(120);

        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number");
        phoneField.setPrefWidth(250);

        SPhoneBox.getChildren().addAll(countryCodeDropdown, phoneField);

        Button uploadPhotoButton = new Button("Upload Photo ID");
        Button SSubmitButton = new Button("Submit");

        signInLayout.getChildren().addAll(
                STitle, SUsernameLabel, SUsernameField,
                SEmailLabel, SEmailField,
                SPasswordLabel, SPasswordField,
                SPhoneLabel, SPhoneBox,
                uploadPhotoButton, SSubmitButton
        );

        // Log-In Section
        VBox loginLayout = new VBox(15);
        loginLayout.setPadding(new Insets(20));
        loginLayout.setAlignment(Pos.TOP_LEFT);
        loginLayout.setStyle("-fx-background-color: #C8F0FF;");

        Label LTitle = new Label("Log In");
        LTitle.setStyle("-fx-font-size: 40px; -fx-font-weight: bold;");

        Label LUsernameLabel = new Label("Username:");
        LUsernameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        TextField LUsernameField = new TextField();
        LUsernameField.setPromptText("Enter your username");
        LUsernameField.setPrefWidth(250);

        Label LPasswordLabel = new Label("Password:");
        LPasswordLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        PasswordField LPasswordField = new PasswordField();
        LPasswordField.setPromptText("Enter your password");
        LPasswordField.setPrefWidth(250);

        Button LSubmitButton = new Button("Submit");

        ImageView ballotImage = new ImageView(new Image(getClass().getResource("/images/Ballot Image.png").toExternalForm()));
        ballotImage.setFitHeight(250);
        ballotImage.setFitWidth(300);

        HBox ballotWrapper = new HBox();
        ballotWrapper.setAlignment(Pos.CENTER);
        ballotWrapper.getChildren().add(ballotImage);

        loginLayout.getChildren().addAll(LTitle, LUsernameLabel, LUsernameField, LPasswordLabel, LPasswordField, LSubmitButton, ballotWrapper);

        form.getChildren().addAll(signInLayout, loginLayout);
        layout.setCenter(form);

        Scene scene = new Scene(layout, 800, 635);
        return scene;
    }
}
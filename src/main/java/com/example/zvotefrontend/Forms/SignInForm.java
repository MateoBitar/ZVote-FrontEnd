package com.example.zvotefrontend.Forms;

import com.example.zvotefrontend.Controllers.SignInController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SignInForm {

    private final SignInController controller = new SignInController();  // Connect to backend

    public Scene createSignInScene(Stage primaryStage) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Label title = new Label("Sign In");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #C8F0FF; -fx-font-weight: bold;");
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            try {
                String response = controller.login(username, password);
                System.out.println("Login Response: " + response);  // Debugging log
            } catch (Exception ex) {
                System.out.println("Login failed: " + ex.getMessage());
            }
        });

        layout.getChildren().addAll(title, usernameField, passwordField, loginButton);
        return new Scene(layout, 400, 300);
    }
}
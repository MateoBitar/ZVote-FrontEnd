package com.example.zvotefrontend.Forms;

import com.example.zvotefrontend.Controllers.UserController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONObject;

public class UserForm {
    private Stage primaryStage;
    private JSONObject currentUser;

    public UserForm(Stage primaryStage, String username) {
        this.primaryStage = primaryStage;
        this.currentUser = UserController.getUserByUsername(username);
    }

    public void showUserProfile() {
        VBox userInfoSection = new VBox(20);
        userInfoSection.setPadding(new Insets(20));
        userInfoSection.setAlignment(Pos.CENTER);

        Label title = new Label("User Profile");
        title.setStyle("-fx-font-size: 50px; -fx-font-weight: bold;");

        Label usernameLabel = new Label("Username: " + currentUser.getString("username"));
        Label emailLabel = new Label("Email: " + currentUser.getString("email"));
        Label phoneLabel = new Label("Phone Number: " + currentUser.getString("phone"));
        Label roleLabel = new Label("Role: " + currentUser.getString("role"));

        Button updateButton = new Button("Update Info");
        updateButton.setOnAction(e -> showUpdateForm());

        userInfoSection.getChildren().addAll(title, usernameLabel, emailLabel, phoneLabel, roleLabel, updateButton);

        Scene scene = new Scene(userInfoSection, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showUpdateForm() {
        VBox updateForm = new VBox(20);
        updateForm.setPadding(new Insets(20));
        updateForm.setAlignment(Pos.CENTER);

        Label updateTitle = new Label("Update Info");

        TextField emailField = new TextField(currentUser.getString("email"));
        emailField.setPromptText("Enter new email.");

        TextField phoneField = new TextField(currentUser.getString("phone"));
        phoneField.setPromptText("Enter new phone number.");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter new password.");

        Button submitButton = new Button("Save Changes");
        submitButton.setOnAction(e -> {
            JSONObject updatedData = new JSONObject();
            updatedData.put("email", emailField.getText());
            updatedData.put("phone", phoneField.getText());
            updatedData.put("password", passwordField.getText());
            UserController.updateUser(updatedData);
            showUserProfile();
        });

        updateForm.getChildren().addAll(updateTitle, emailField, phoneField, passwordField, submitButton);

        Scene updateScene = new Scene(updateForm, 400, 400);
        primaryStage.setScene(updateScene);
    }
}
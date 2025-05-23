package com.example.zvotefrontend.Forms;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import com.example.zvotefrontend.Controllers.CandidateController;

import java.util.HashMap;
import java.util.Map;

public class CandidateForm {

    private final CandidateController candidateController = new CandidateController(); // Linking Controller

    public void displayCandidates(Stage stage) {
        ObservableList<String> candidateNames = candidateController.getCandidateNames(); // Fetching data from Controller

        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(10, 10, 10, 40));
        topBar.setStyle("-fx-background-color: #C8F0FF;");
        topBar.setAlignment(Pos.CENTER_LEFT);

        DropShadow shadow = new DropShadow();
        shadow.setRadius(5);
        shadow.setOffsetY(2);
        shadow.setColor(Color.LIGHTGRAY);
        topBar.setEffect(shadow);

        Label logo = new Label("ZVote");
        logo.setFont(Font.font("Onyx", FontWeight.BOLD, 60));
        topBar.getChildren().add(logo);

        Label label = new Label("Choose the candidates for the poll:");
        label.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        TableView<String> tableView = new TableView<>();
        tableView.setStyle("-fx-background-color: #C8F0FF; -fx-font-size: 16px;");
        tableView.setItems(candidateNames);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPrefHeight(600);

        Map<String, CheckBox> checkBoxMap = new HashMap<>();

        TableColumn<String, CheckBox> checkBoxColumn = new TableColumn<>("Select");
        checkBoxColumn.setCellValueFactory(data -> {
            CheckBox checkBox = new CheckBox();
            checkBoxMap.put(data.getValue(), checkBox);
            return new javafx.beans.property.SimpleObjectProperty<>(checkBox);
        });

        tableView.getColumns().add(checkBoxColumn);

        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.getChildren().addAll(topBar, label, tableView);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setStyle("-fx-background-color: white;");

        Scene scene = new Scene(vBox, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight() - 80);
        stage.setScene(scene);
        stage.show();
    }
}
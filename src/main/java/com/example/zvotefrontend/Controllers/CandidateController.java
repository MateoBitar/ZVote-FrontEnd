package com.example.zvotefrontend.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public class CandidateController {

    private static final String BACKEND_URL = "https://your-backend-domain.com/api/candidates"; // Adjust backend endpoint

    public ObservableList<String> getCandidateNames() {
        try {
            URL url = new URL(BACKEND_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                ObservableList<String> candidateNames = FXCollections.observableArrayList(reader.lines().collect(Collectors.toList()));
                reader.close();
                return candidateNames;
            } else {
                throw new RuntimeException("Failed to fetch candidates. HTTP response code: " + responseCode);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error connecting to API: " + e.getMessage());
        }
    }
}
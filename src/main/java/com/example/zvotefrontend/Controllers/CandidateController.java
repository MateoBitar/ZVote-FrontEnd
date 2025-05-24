package com.example.zvotefrontend.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public class CandidateController {
    private static final String BASE_URL = "http://192.168.1.10:8080/zvote";

    public ObservableList<String> getCandidateNames() {
        try {
            URL url = new URL(BASE_URL + "/candidates");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
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

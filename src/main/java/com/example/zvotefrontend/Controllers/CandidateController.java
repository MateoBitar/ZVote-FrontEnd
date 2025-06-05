package com.example.zvotefrontend.Controllers;

import io.github.cdimascio.dotenv.Dotenv;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Collectors;

public class CandidateController {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String BASE_URL = dotenv.get("ZVOTE_BASE_URL");

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

    public static void addResult(JSONObject resultData) {
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/addresult"))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(resultData.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.out.println("Failed to add result. Server responded with: " + response.body());
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Error adding result: " + e.getMessage());
        }
    }


}

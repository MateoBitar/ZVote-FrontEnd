package com.example.zvotefrontend.Controllers;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONArray;

public class AdminLandingPageController {
    private static final String BASE_URL = "http://192.168.1.10:8080/zvote";

    public JSONArray getPolls() {
        try {
            URL url = new URL(BASE_URL + "/polls"); // Backend API endpoint
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed to fetch polls: HTTP Error " + conn.getResponseCode());
            }

            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder jsonResponse = new StringBuilder();
            while (scanner.hasNext()) {
                jsonResponse.append(scanner.nextLine());
            }
            scanner.close();
            conn.disconnect();

            return new JSONArray(jsonResponse.toString()); // Convert response to JSON array
        } catch (Exception e) {
            throw new RuntimeException("Error fetching polls: " + e.getMessage());
        }
    }

    public boolean deletePoll(String pollId) {
        try {
            URL url = new URL(BASE_URL + "/polls/" + pollId); // Backend API endpoint for deleting a poll
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == 200) {
                return true; // Poll successfully deleted
            } else {
                throw new RuntimeException("Failed to delete poll: HTTP Error " + conn.getResponseCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deleting poll: " + e.getMessage());
        }
    }
}

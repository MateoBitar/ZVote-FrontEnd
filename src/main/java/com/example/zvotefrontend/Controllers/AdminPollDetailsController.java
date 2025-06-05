package com.example.zvotefrontend.Controllers;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONObject;

public class AdminPollDetailsController {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String BASE_URL = dotenv.get("ZVOTE_BASE_URL");

    public JSONObject getPollDetails(String pollId) {
        try {
            URL url = new URL(BASE_URL + "/polls/" + pollId); // Backend API endpoint
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed to fetch poll details: HTTP Error " + conn.getResponseCode());
            }

            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder jsonResponse = new StringBuilder();
            while (scanner.hasNext()) {
                jsonResponse.append(scanner.nextLine());
            }
            scanner.close();
            conn.disconnect();

            return new JSONObject(jsonResponse.toString()); // Convert response to JSON object
        } catch (Exception e) {
            throw new RuntimeException("Error fetching poll details: " + e.getMessage());
        }
    }
}

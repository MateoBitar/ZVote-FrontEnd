package com.example.zvotefrontend.Controllers;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class SignInController {

    private static final String BASE_URL = "http://192.168.1.4:8080/zvote";

    // POST /zvote/login
    public boolean login(String username, String password) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        String loginUrl = BASE_URL + "/login?username=" + username + "&password=" + password;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(loginUrl))
                .header("Accept", "text/plain")
                .POST(HttpRequest.BodyPublishers.noBody()) // No body needed, params are in URL
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Check response status code (e.g., 200 means OK/success)
        if (response.statusCode() == 200 && response.body().equalsIgnoreCase("Login successful")) {
            return true;
        } else {
            return false;
        }
    }

    // POST /zvote/users
    public String signUp(String username, String email, String password, byte[] photoID, String phone)
            throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

        // Encode photoID to Base64 string (because you can't send raw bytes in JSON)
        String encodedPhoto = Base64.getEncoder().encodeToString(photoID);

        JSONObject requestJson = new JSONObject();
        requestJson.put("username", username);
        requestJson.put("user_email", email);
        requestJson.put("user_pass", password);
        requestJson.put("user_photoID", encodedPhoto);
        requestJson.put("phoneNb", phone);
        requestJson.put("role", "voter");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/users"))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestJson.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int statusCode = response.statusCode();
        String responseBody = response.body();

        if (statusCode == 200) {
            return "SUCCESS: " + responseBody;
        } else if (statusCode == 400) {
            return "ERROR: Username already exists";
        } else {
            return "ERROR: Server error occurred (" + statusCode + ")";
        }
    }
}

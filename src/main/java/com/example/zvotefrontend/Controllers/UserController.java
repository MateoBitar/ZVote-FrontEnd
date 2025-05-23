package com.example.zvotefrontend.Controllers;

import org.json.JSONObject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class UserController {
    private static final String BASE_URL = "http://localhost:8080/api/users";

    public static JSONObject fetchUserDetails(String username) {
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + username))
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new JSONObject(response.body());
        } catch (IOException | InterruptedException e) {
            System.out.println("Error fetching user details: " + e.getMessage());
            return new JSONObject();
        }
    }

    public static void updateUserInfo(JSONObject updatedUserData) {
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/update"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(updatedUserData.toString()))
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Error updating user info: " + e.getMessage());
        }
    }
}
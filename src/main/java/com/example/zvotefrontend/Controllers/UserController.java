package com.example.zvotefrontend.Controllers;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class UserController {
    private static final String BASE_URL = "http://192.168.1.10:8080/zvote";

    // GET /zvote/users/{username}
    public static JSONObject getUserByUsername(String username) {
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/users/" + username))
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

    // PUT /zvote/users
    public static void updateUser(JSONObject updatedUserData) {
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/users"))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(updatedUserData.toString()))
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Error updating user info: " + e.getMessage());
        }
    }

    // GET /zvote/users/role/{username}
    public String getRoleByUsername(String username) throws IOException {
        String urlStr = BASE_URL + "/role/" + username;
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "text/plain");

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String role = in.readLine(); // should just be "admin" or "user"
            in.close();
            return role;
        } else if (responseCode == 404) {
            return null; // or handle user not found explicitly
        } else {
            throw new IOException("HTTP error code: " + responseCode);
        }
    }
}

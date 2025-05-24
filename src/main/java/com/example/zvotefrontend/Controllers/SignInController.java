package com.example.zvotefrontend.Controllers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SignInController {

    private static final String BASE_URL = "http://192.168.1.10:8080/zvote";

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
    public String signUp(String username, String email, String password, byte[] photoID, String phone) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String requestBody = String.format(
                "{\"username\": \"%s\", \"email\": \"%s\", \"password\": \"%s\", \"photoID\": \" \", \"phone\": \"%s\"}",
                username, email, password, photoID, phone
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/users"))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}

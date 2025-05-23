package com.example.zvotefrontend.Controllers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SignInController {

    private static String buildUrl(String endpoint) {
        final String BASE_URL = "http://localhost:8080/api/auth/";
        return BASE_URL + endpoint;
    }

    public String login(String username, String password) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String requestBody = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(buildUrl("login")))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String signUp(String username, String email, String password, String phone) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String requestBody = "{\"username\": \"" + username + "\", \"email\": \"" + email + "\", \"password\": \"" + password + "\", \"phone\": \"" + phone + "\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(buildUrl("signup")))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
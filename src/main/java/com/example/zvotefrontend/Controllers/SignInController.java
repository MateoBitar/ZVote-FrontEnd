package com.example.zvotefrontend.Controllers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SignInController {

    private static final String BASE_URL = "http://192.168.1.10:8080/zvote";

    // POST /zvote/login
    public String login(String username, String password) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        String loginUrl = BASE_URL + "/login?username=" + username + "&password=" + password;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(loginUrl))
                .header("Accept", "text/plain")
                .POST(HttpRequest.BodyPublishers.noBody()) // No body needed, params are in URL
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    // POST /zvote/addUser
    public String signUp(String username, String email, String password, String phone) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String requestBody = String.format(
                "{\"username\": \"%s\", \"email\": \"%s\", \"password\": \"%s\", \"phone\": \"%s\"}",
                username, email, password, phone
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/adduser"))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}

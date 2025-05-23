package com.example.zvotefrontend.Controllers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AboutUsController {

    private static String buildUrl(String endpoint) {
        final String BASE_URL = "http://localhost:8080/api/";
        return BASE_URL + endpoint;
    }

    public String fetchAboutInfo() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(buildUrl("about")))
                .GET()
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();  // Returns the About Us text from backend
    }
}
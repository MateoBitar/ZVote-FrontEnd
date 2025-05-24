package com.example.zvotefrontend.Controllers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class LandingPageController {
    private static final String BASE_URL = "http://192.168.1.10:8080/zvote";

    // GET /zvote/getallpolls
    public List<JSONObject> getAllPolls() {
        List<JSONObject> pollsList = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/polls/"))
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONArray pollsArray = new JSONArray(response.body());
            for (int i = 0; i < pollsArray.length(); i++) {
                pollsList.add(pollsArray.getJSONObject(i));
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Error fetching polls: " + e.getMessage());
        }

        return pollsList;
    }
}

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

    // GET /zvote/polls
    public List<JSONObject> getAllPolls() {
        List<JSONObject> pollsList = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/polls")) // Removed trailing slash just in case
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            if (response.statusCode() == 200 && responseBody.trim().startsWith("[")) {
                JSONArray pollsArray = new JSONArray(responseBody);
                for (int i = 0; i < pollsArray.length(); i++) {
                    pollsList.add(pollsArray.getJSONObject(i));
                }
            } else {
                System.out.println("Unexpected response format or status: " + responseBody);
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Error fetching polls: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error parsing response: " + e.getMessage());
        }

        return pollsList;
    }

    // Synchronous call to get candidates for a poll as JSONObjects
    public List<JSONObject> getCandidatesWithVotesByPollID(int pollID) {
        List<JSONObject> candidatesList = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/candidateswithvotes/" + pollID))
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            if (response.statusCode() == 200 && responseBody.trim().startsWith("[")) {
                JSONArray candidatesArray = new JSONArray(responseBody);
                for (int i = 0; i < candidatesArray.length(); i++) {
                    candidatesList.add(candidatesArray.getJSONObject(i));
                }
            } else {
                System.out.println("Unexpected response: " + responseBody);
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Error fetching candidates: " + e.getMessage());
        }

        return candidatesList;
    }
}

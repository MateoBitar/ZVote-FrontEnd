package com.example.zvotefrontend.Controllers;

import org.json.JSONObject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PollController {
    private static final String BASE_URL = "http://192.168.1.10:8080/zvote";

    // GET /zvote/getpoll/{poll_ID}
    public static JSONObject getPollByPoll_ID(int pollId) {
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/getpoll/" + pollId))
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new JSONObject(response.body());
        } catch (IOException | InterruptedException e) {
            System.out.println("Error fetching poll details: " + e.getMessage());
            return new JSONObject();
        }
    }

    // POST /zvote/addvote
    public static void addVote(JSONObject voteData) {
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/addvote"))
                    .header("Content-Type", "application/json")
                    .header("Accept", "text/plain")
                    .POST(HttpRequest.BodyPublishers.ofString(voteData.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Vote Response: " + response.body());
        } catch (IOException | InterruptedException e) {
            System.out.println("Error submitting vote: " + e.getMessage());
        }
    }
}

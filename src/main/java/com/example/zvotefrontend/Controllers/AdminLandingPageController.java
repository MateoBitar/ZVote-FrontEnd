package com.example.zvotefrontend.Controllers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class AdminLandingPageController {
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

    public boolean deletePoll(String pollId) {
        try {
            URL url = new URL(BASE_URL + "/polls/" + pollId); // Backend API endpoint for deleting a poll
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == 200) {
                return true; // Poll successfully deleted
            } else {
                throw new RuntimeException("Failed to delete poll: HTTP Error " + conn.getResponseCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deleting poll: " + e.getMessage());
        }
    }

    public boolean deleteVotesByPoll(String pollId) {
        try {
            URL url = new URL(BASE_URL + "/deletevotesbypoll/" + pollId); // Backend API endpoint for deleting votes
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == 200) {
                return true; // Vote successfully deleted
            } else {
                throw new RuntimeException("Failed to delete vote: HTTP Error " + conn.getResponseCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deleting vote: " + e.getMessage());
        }
    }

    public List<JSONObject> getPollVotes(String pollId) {
        List<JSONObject> votesList = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/getvotesbypoll/" + pollId)) // Removed trailing slash just in case
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            if (response.statusCode() == 200 && responseBody.trim().startsWith("[")) {
                JSONArray votesArray = new JSONArray(responseBody);
                for (int i = 0; i < votesArray.length(); i++) {
                    votesList.add(votesArray.getJSONObject(i));
                }
            } else {
                System.out.println("Unexpected response format or status: " + responseBody);
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Error fetching votes: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error parsing response: " + e.getMessage());
        }

        return votesList;
    }

    public boolean deleteResult(String resultId) {
        try {
            URL url = new URL(BASE_URL + "/deleteresult/" + resultId); // Backend API endpoint for deleting a result
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == 200) {
                return true; // Result successfully deleted
            } else {
                throw new RuntimeException("Failed to delete vote: HTTP Error " + conn.getResponseCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deleting vote: " + e.getMessage());
        }
    }

    public List<JSONObject> getPollResults(String pollId) {
        List<JSONObject> resultsList = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/getresultsbypoll/" + pollId)) // Removed trailing slash just in case
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            if (response.statusCode() == 200 && responseBody.trim().startsWith("[")) {
                JSONArray resultsArray = new JSONArray(responseBody);
                for (int i = 0; i < resultsArray.length(); i++) {
                    resultsList.add(resultsArray.getJSONObject(i));
                }
            } else {
                System.out.println("Unexpected response format or status: " + responseBody);
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Error fetching results: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error parsing response: " + e.getMessage());
        }

        return resultsList;
    }
}

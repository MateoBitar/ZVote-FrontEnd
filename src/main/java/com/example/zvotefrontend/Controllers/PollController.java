package com.example.zvotefrontend.Controllers;

import org.json.JSONObject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PollController {
    private static final String BASE_URL = "http://192.168.1.4:8080/zvote";

    // GET /zvote/polls/{poll_ID}
    public static JSONObject getPollByPoll_ID(int pollId) {
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/polls/" + pollId))
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

            if (response.statusCode() != 200) {
                System.out.println("Failed to add result. Server responded with: " + response.body());
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Error submitting vote: " + e.getMessage());
        }
    }

    // GET /zvote/getresultbypollcandidate/{pollID}/{candidateID}
    public JSONObject getResultByPollAndCandidate(int pollID, int candidateID) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/getresultbypollcandidate/" + pollID + "/" + candidateID))
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return new JSONObject(response.body());
            } else {
                System.err.println("No result found or error: " + response.body());
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // GET /zvote/winner/{poll_ID}
    public JSONObject getWinnerByPoll_ID(int pollId) {
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/winner/" + pollId))
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new JSONObject(response.body());
        } catch (IOException | InterruptedException e) {
            System.out.println("Error fetching winner details: " + e.getMessage());
            return new JSONObject();
        }
    }

    // GET /zvote/hasuservoted/{userID}/{pollID}
    public boolean hasUserVoted(int userId, int pollId) {
        HttpClient client = HttpClient.newHttpClient();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/hasuservoted/" + userId + "/" + pollId))
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return Boolean.parseBoolean(response.body());

        } catch (Exception e) {
            e.printStackTrace(); // Log the error
            return false; // Default to false if an error occurs
        }
    }

    // GET /zvote/votepercentage/{candidatevotes}/{totalvotes}
    public double getVotePercentage(int candidateVotes, int totalVotes) {
        HttpClient client = HttpClient.newHttpClient();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/votepercentage/" + candidateVotes + "/" + totalVotes))
                    .GET()
                    .header("Accept", "text/plain")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return Double.parseDouble(response.body().trim());
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    // PUT /zvote/polls
    public void updatePoll(JSONObject updatedPollData) {
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/polls"))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(updatedPollData.toString()))
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Error updating poll info: " + e.getMessage());
        }
    }

    // PUT /zvote/updateresult
    public void updateResult(JSONObject updatedResultData) {
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/updateresult"))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(updatedResultData.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.out.println("Failed to update result. Server responded with: " + response.body());
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Error updating result: " + e.getMessage());
        }
    }

}

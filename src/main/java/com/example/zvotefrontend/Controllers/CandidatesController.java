package com.example.zvotefrontend.Controllers;

import org.json.JSONArray;
import org.json.JSONObject;

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

public class CandidatesController {

    private static final String BASE_URL = "http://192.168.1.10:8080/zvote";

    public List<JSONObject> getAllCandidates() {
        return fetchCandidates(BASE_URL + "/candidates");
    }

    public List<JSONObject> filterCandidates(String query) {
        List<JSONObject> allCandidates = getAllCandidates();
        List<JSONObject> filteredCandidates = new ArrayList<>();

        for (JSONObject candidate : allCandidates) {
            if (candidate.getString("name").toLowerCase().contains(query.toLowerCase())
                    || candidate.getString("bio").toLowerCase().contains(query.toLowerCase())) {
                filteredCandidates.add(candidate);
            }
        }
        return filteredCandidates;
    }

    public boolean deleteCandidate(int candidateId) {
        try {
            URL url = new URL(BASE_URL + "/candidates/" + candidateId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");

            return conn.getResponseCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteVotesByCandidate(String candidateId) {
        try {
            URL url = new URL(BASE_URL + "/deletevotesbycandidate/" + candidateId); // Backend API endpoint for deleting votes
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == 200) {
                return true; // Vote successfully deleted
            } else {
                throw new RuntimeException("Failed to delete votes: HTTP Error " + conn.getResponseCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deleting votes: " + e.getMessage());
        }
    }

    public List<JSONObject> getCandidateVotes(String candidateId) {
        List<JSONObject> votesList = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/getvotesbycandidate/" + candidateId)) // Removed trailing slash just in case
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

    public List<JSONObject> getCandidateResults(String candidateId) {
        List<JSONObject> resultsList = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/getresultsbycandidate/" + candidateId)) // Removed trailing slash just in case
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

    private List<JSONObject> fetchCandidates(String apiUrl) {
        List<JSONObject> candidates = new ArrayList<>();
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            Scanner scanner = new Scanner(url.openStream());
            StringBuilder response = new StringBuilder();

            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            scanner.close();

            JSONArray jsonArray = new JSONArray(response.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                candidates.add(jsonArray.getJSONObject(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candidates;
    }
}
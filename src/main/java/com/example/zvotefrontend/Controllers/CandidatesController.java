package com.example.zvotefrontend.Controllers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CandidatesController {

    private static final String BASE_URL = "https://192.168.1.10:8080/zvote";

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
package com.example.zvotefrontend.Controllers;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import org.json.JSONObject;

public class AdminPollController {
    private static final String BASE_URL = "http://192.168.1.4:8080/zvote";

    public boolean createPoll(JSONObject pollData) {
        try {
            URL url = new URL(BASE_URL + "/polls"); // Backend API endpoint
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write(pollData.toString().getBytes());
            os.flush();
            os.close();

            if (conn.getResponseCode() == 201) {
                conn.disconnect();
                return true;
            } else {
                conn.disconnect();
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating poll: " + e.getMessage());
        }
    }
}

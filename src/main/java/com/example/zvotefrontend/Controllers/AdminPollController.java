package com.example.zvotefrontend.Controllers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import org.json.JSONObject;

public class AdminPollController {
    private static final String BASE_URL = "http://192.168.1.4:8080/zvote";

    public JSONObject createPoll(JSONObject pollData) {
        try {
            URL url = new URL(BASE_URL + "/polls"); // Backend API endpoint
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send poll data
            OutputStream os = conn.getOutputStream();
            os.write(pollData.toString().getBytes());
            os.flush();
            os.close();

            // Read response body
            int status = conn.getResponseCode();
            InputStream is = (status < 400) ? conn.getInputStream() : conn.getErrorStream();

            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
            conn.disconnect();

            // Parse response body as JSON
            return new JSONObject(response.toString());

        } catch (Exception e) {
            throw new RuntimeException("Error creating poll: " + e.getMessage());
        }
    }

}

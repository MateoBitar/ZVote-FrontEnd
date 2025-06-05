package com.example.zvotefrontend.Controllers;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.nio.file.Files;
import java.io.File;
import java.util.Base64;

import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONObject;

public class CreateCandidateController {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String BASE_URL = dotenv.get("ZVOTE_BASE_URL");

    public boolean createCandidate(JSONObject candidateData) {
        try {
            URL url = new URL(BASE_URL + "/candidates"); // Backend API endpoint
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write(candidateData.toString().getBytes());
            os.flush();
            os.close();

            if (conn.getResponseCode() == 200) {
                conn.disconnect();
                return true;
            } else {
                conn.disconnect();
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating candidate: " + e.getMessage());
        }
    }
}

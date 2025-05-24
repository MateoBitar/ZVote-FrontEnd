package com.example.zvotefrontend.Controllers;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.nio.file.Files;
import java.io.File;
import java.util.Base64;

import org.json.JSONObject;

public class CreateCandidateController {
    private static final String BASE_URL = "http://192.168.1.10:8080/zvote";

    public boolean createCandidate(JSONObject candidateData, File photoFile) {
        try {
            URL url = new URL(BASE_URL + "/candidates"); // Backend API endpoint
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Convert photo file to byte array and add to JSON
            byte[] photoBytes = photoFile != null ? Files.readAllBytes(photoFile.toPath()) : new byte[0];
            candidateData.put("photo", Base64.getEncoder().encodeToString(photoBytes));

            OutputStream os = conn.getOutputStream();
            os.write(candidateData.toString().getBytes());
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
            throw new RuntimeException("Error creating candidate: " + e.getMessage());
        }
    }
}

package com.callcenter.ui;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080";

    public static String get(String endpoint) throws Exception {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        return readResponse(conn);
    }

    public static String post(String endpoint, String json) throws Exception {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        if (json != null && !json.isEmpty()) {
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes());
            }
        }

        return readResponse(conn);
    }

    private static String readResponse(HttpURLConnection conn) throws Exception {
        int responseCode = conn.getResponseCode();

        InputStream stream = (responseCode >= 200 && responseCode < 300)
                ? conn.getInputStream()
                : conn.getErrorStream();

        BufferedReader in = new BufferedReader(new InputStreamReader(stream));

        StringBuilder response = new StringBuilder();
        String line;

        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        return response.toString();
    }
}
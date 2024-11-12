package com.example.cplibrary.infrastructure;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import org.json.JSONArray;
import org.json.JSONObject;

public class GoogleBooksAPI {
    private static final String API_KEY = "AIzaSyBIvKi4gikZqtiHyEEt2kXhD0QUZ8gXyek";
    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    // Phương thức để lấy URL ảnh bìa
    public static String fetchBookImageURL(String query) {
        String imageUrl = null;
        try {
            // Tạo URL request đến Google Books API
            String requestUrl = BASE_URL + query + "&key=" + API_KEY;
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Đọc dữ liệu trả về từ API
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            // Parse dữ liệu JSON từ API
            JSONObject jsonResponse = new JSONObject(content.toString());
            JSONArray items = jsonResponse.getJSONArray("items");

            if (items.length() > 0) {
                JSONObject volumeInfo = items.getJSONObject(0).getJSONObject("volumeInfo");
                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                imageUrl = imageLinks.getString("thumbnail");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return imageUrl;
    }
}

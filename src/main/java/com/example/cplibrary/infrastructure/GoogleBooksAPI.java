package com.example.cplibrary.infrastructure;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

public class GoogleBooksAPI {
    private static final String API_KEY = "AIzaSyBIvKi4gikZqtiHyEEt2kXhD0QUZ8gXyek";
    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=isbn:";

    // Phương thức lấy ảnh sách theo ISBN (đã có)
    public static CompletableFuture<String> fetchBookImageURLAsync(String isbn) {
        return CompletableFuture.supplyAsync(() -> {
            String imageUrl = null;
            try {
                String requestUrl = BASE_URL + isbn + "&key=" + API_KEY;
                URL url = new URL(requestUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder content = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                JSONObject jsonResponse = new JSONObject(content.toString());
                JSONArray items = jsonResponse.optJSONArray("items");

                if (items != null && items.length() > 0) {
                    JSONObject volumeInfo = items.getJSONObject(0).getJSONObject("volumeInfo");
                    JSONObject imageLinks = volumeInfo.optJSONObject("imageLinks");
                    if (imageLinks != null) {
                        imageUrl = imageLinks.optString("thumbnail");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return imageUrl;
        });
    }

    // Phương thức lấy tên sách theo ISBN (mới thêm)
    public static CompletableFuture<String> fetchBookTitleAsync(String isbn) {
        return CompletableFuture.supplyAsync(() -> {
            String title = null;
            try {
                String requestUrl = BASE_URL + isbn + "&key=" + API_KEY;
                URL url = new URL(requestUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder content = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                JSONObject jsonResponse = new JSONObject(content.toString());
                JSONArray items = jsonResponse.optJSONArray("items");

                if (items != null && items.length() > 0) {
                    JSONObject volumeInfo = items.getJSONObject(0).getJSONObject("volumeInfo");
                    title = volumeInfo.optString("title");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return title;
        });
    }

    // Phương thức lấy danh sách URL ảnh từ danh sách ISBN
    public static List<String> fetchBookImageURLs(List<String> isbns) {
        List<CompletableFuture<String>> futures = isbns.stream()
                .map(GoogleBooksAPI::fetchBookImageURLAsync)
                .collect(Collectors.toList());

        // Chờ tất cả các tác vụ hoàn thành
        return futures.stream()
                .map(CompletableFuture::join) // Lấy kết quả
                .collect(Collectors.toList());
    }

    // Phương thức lấy danh sách tên sách từ danh sách ISBN
    public static List<String> fetchBookTitles(List<String> isbns) {
        List<CompletableFuture<String>> futures = isbns.stream()
                .map(GoogleBooksAPI::fetchBookTitleAsync)
                .collect(Collectors.toList());

        // Chờ tất cả các tác vụ hoàn thành
        return futures.stream()
                .map(CompletableFuture::join) // Lấy kết quả
                .collect(Collectors.toList());
    }
}

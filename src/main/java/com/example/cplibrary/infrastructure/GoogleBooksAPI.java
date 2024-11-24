package com.example.cplibrary.infrastructure;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GoogleBooksAPI {
    private static final String API_KEY = "AIzaSyBIvKi4gikZqtiHyEEt2kXhD0QUZ8gXyek";
    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=isbn:";

    /**
     * Lấy thông tin sách từ API Google Books dựa trên ISBN.
     *
     * @param isbn ISBN của sách.
     * @return Một mảng chứa tất cả thông tin của sách. Nếu không tìm thấy, trả về mảng với các giá trị null.
     */
    public static String[] fetchBookDetails(String isbn) {
        String title = null;
        String author = null;
        String subject = null;
        String publisher = null;
        String description = null;
        String imageUrl = null;

        try {
            String requestUrl = BASE_URL + isbn + "&key=" + API_KEY;
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Đọc dữ liệu từ API
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            // Phân tích JSON
            JSONObject jsonResponse = new JSONObject(content.toString());
            JSONArray items = jsonResponse.optJSONArray("items");

            if (items != null && items.length() > 0) {
                JSONObject volumeInfo = items.getJSONObject(0).getJSONObject("volumeInfo");

                title = volumeInfo.optString("title", null);

                // Lấy danh sách authors
                JSONArray authorsArray = volumeInfo.optJSONArray("authors");
                if (authorsArray != null) {
                    author = String.join(", ", authorsArray.toList().stream()
                            .map(Object::toString).toArray(String[]::new));
                }

                // Lấy danh sách categories (subject)
                JSONArray categoriesArray = volumeInfo.optJSONArray("categories");
                if (categoriesArray != null) {
                    subject = String.join(", ", categoriesArray.toList().stream()
                            .map(Object::toString).toArray(String[]::new));
                }

                publisher = volumeInfo.optString("publisher", null);
                description = volumeInfo.optString("description", null);

                JSONObject imageLinks = volumeInfo.optJSONObject("imageLinks");
                if (imageLinks != null) {
                    imageUrl = imageLinks.optString("thumbnail", null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new String[]{title, author, subject, publisher, description, imageUrl};
    }
}

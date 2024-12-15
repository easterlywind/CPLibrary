package com.example.cplibrary.infrastructure;

import com.example.cplibrary.model.Book;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ApiClient {

    private static final String API_URL = "http://localhost:5000/recommend"; // URL của API

    public static List<Book> getRecommendedBooks(int userId) {
        List<Book> recommendedBooks = new ArrayList<>();
        try {

            String requestUrl = API_URL + "?userId=" + userId;
            URL url = new URL(requestUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new Exception("Error: " + connection.getResponseCode());
            }

            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

//                System.out.println("Response Body: " + response);

                JSONObject responseJson = new JSONObject(response.toString());

                if (responseJson.has("recommendations")) {
                    JSONArray recommendationsArray = responseJson.getJSONArray("recommendations");

                    for (int i = 0; i < recommendationsArray.length(); i++) {
                        JSONObject jsonBook = recommendationsArray.getJSONObject(i);

                        int book_id = jsonBook.getInt("book_id");
                        int quantity = jsonBook.getInt("quantity");
                        String title = jsonBook.getString("title");
                        String author = jsonBook.getString("author");
                        String isbn = jsonBook.getString("isbn");
                        String subject = jsonBook.getString("subject");
                        String publisher = jsonBook.getString("publisher");
                        String shelfLocation = jsonBook.getString("shelf_location");
                        String review = jsonBook.getString("review");
                        String imageUrl = jsonBook.getString("imageUrl");

                        Book book = new Book(book_id, quantity, isbn, title, author, subject, publisher, shelfLocation, review, imageUrl);
                        recommendedBooks.add(book);
                    }
                } else {
                    System.out.println("No recommendations found in the response");
                }
            }

            // Đóng kết nối
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return recommendedBooks;
    }
}

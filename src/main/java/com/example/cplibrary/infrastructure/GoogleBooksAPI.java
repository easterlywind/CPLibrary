package com.example.cplibrary.infrastructure;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class GoogleBooksAPI {

    private static final String API_KEY = "AIzaSyBIvKi4gikZqtiHyEEt2kXhD0QUZ8gXyek";

    // Method to search for books by title
    public static void searchBookByTitle(String title) throws IOException {
        String query = title.replace(" ", "%20");
        String urlString = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&key=" + API_KEY;

        // Create a URL object
        URL url = new URL(urlString);

        // Open a connection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        // Check if the request was successful
        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            // Read the response
            InputStreamReader reader = new InputStreamReader(conn.getInputStream());
            StringBuilder response = new StringBuilder();
            Scanner scanner = new Scanner(reader);

            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            scanner.close();

            // Parse the response JSON
            parseBookResponse(response.toString());
        } else {
            System.out.println("Error: " + responseCode);
        }
    }

    // Method to parse the response and print book information
    private static void parseBookResponse(String jsonResponse) {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray items = jsonObject.getJSONArray("items");

        // Loop through each book and print details
        for (int i = 0; i < items.length(); i++) {
            JSONObject book = items.getJSONObject(i).getJSONObject("volumeInfo");
            String title = book.getString("title");
            String authors = book.has("authors") ? book.getJSONArray("authors").join(", ") : "Unknown";

            System.out.println("Title: " + title);
            System.out.println("Authors: " + authors);
            System.out.println("----------------------------");
        }
    }
}

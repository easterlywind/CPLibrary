package com.example.cplibrary.infrastructure;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class GoogleBooksAPI {
    private static final String API_KEY = "AIzaSyBIvKi4gikZqtiHyEEt2kXhD0QUZ8gXyek";
    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    private static void saveJsonToFile(String jsonString, String fileName) {
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(jsonString);
            System.out.println("JSON data has been saved to " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Lấy thông tin sách từ API Google Books dựa trên ISBN hoặc từ khóa.
     *
     * @param query     ISBN hoặc từ khóa tìm kiếm.
     * @param isKeyword True nếu là từ khóa, False nếu là ISBN.
     * @return Một mảng chứa tất cả thông tin của sách. Nếu không tìm thấy, trả về mảng với các giá trị null.
     */
    public static List<String[]> fetchBookDetails(String query, boolean isKeyword) {
        List<String[]> books = new ArrayList<>();

        if (isKeyword) {
            query = query.replace(' ', '+'); // Thay dấu cách bằng dấu cộng
        }

        try {
            String requestUrl = BASE_URL + (isKeyword ? query : "isbn:" + query) + "&key=" + API_KEY ;
            System.out.println("Requesting URL: " + requestUrl);

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

            String jsonResponse = content.toString();
            saveJsonToFile(jsonResponse, "response.json");

            JSONObject responseJson = new JSONObject(jsonResponse);
            JSONArray items = responseJson.optJSONArray("items");

            if (items != null) {
                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    JSONObject volumeInfo = item.getJSONObject("volumeInfo");

                    String isbn = null;
                    JSONArray industryIdentifiers = volumeInfo.optJSONArray("industryIdentifiers");
                    if (industryIdentifiers != null) {
                        for (int j = 0; j < industryIdentifiers.length(); j++) {
                            JSONObject identifier = industryIdentifiers.getJSONObject(j);
                            if ("ISBN_13".equals(identifier.optString("type"))) {
                                isbn = identifier.optString("identifier");
                                break;
                            }
                        }
                    }

                    String title = volumeInfo.optString("title", "Unknown Title");
                    String author = volumeInfo.optJSONArray("authors") != null
                            ? String.join(", ", volumeInfo.getJSONArray("authors").toList().stream()
                            .map(Object::toString).toArray(String[]::new))
                            : "Unknown Author";
                    String subject = volumeInfo.optJSONArray("categories") != null
                            ? String.join(", ", volumeInfo.getJSONArray("categories").toList().stream()
                            .map(Object::toString).toArray(String[]::new))
                            : "Unknown Subject";
                    String publisher = volumeInfo.optString("publisher", "Unknown Publisher");
                    String description = volumeInfo.optString("description", "No Description");
                    String imageUrl = volumeInfo.optJSONObject("imageLinks") != null
                            ? volumeInfo.getJSONObject("imageLinks").optString("thumbnail", null)
                            : null;

                    // Lưu thông tin sách vào danh sách
                    books.add(new String[]{isbn, title, author, subject, publisher, description, imageUrl});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return books;
    }
}

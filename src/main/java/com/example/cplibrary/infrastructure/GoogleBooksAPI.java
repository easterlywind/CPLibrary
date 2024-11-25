package com.example.cplibrary.infrastructure;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



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
    public static String[] fetchBookDetails(String query, boolean isKeyword) {
        String title = null;
        String author = null;
        String subject = null;
        String publisher = null;
        String description = null;
        String imageUrl = null;
        String isbn = null;

        if (isKeyword) {
            char[] queryArray = query.toCharArray();
            for (int i = 0; i < queryArray.length; i++) {
                if (queryArray[i] == ' ') {
                    queryArray[i] = '+';  // Thay dấu cách bằng dấu cộng
                }
            }
            String modifiedQuery = new String(queryArray);  // Chuyển mảng char thành chuỗi mới
            query = modifiedQuery;
        }


        try {
            String requestUrl = BASE_URL + (isKeyword ? query : "isbn:" + query) + "&key=" + API_KEY;
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

            saveJsonToFile(content.toString(), "response.json");
            JSONObject jsonResponse = new JSONObject(content.toString());
            JSONArray items = jsonResponse.optJSONArray("items");

            if (items != null && items.length() > 0) {
                JSONObject volumeInfo = items.getJSONObject(0).getJSONObject("volumeInfo");

                JSONArray industryIdentifiers = volumeInfo.optJSONArray("industryIdentifiers");
                if (industryIdentifiers != null ) {
                    for (int i = 0; i < industryIdentifiers.length(); i++) {
                        JSONObject identifier = industryIdentifiers.getJSONObject(i);
                        if ("ISBN_13".equals(identifier.optString("type"))) {
                            isbn = identifier.optString("identifier");
                            break;
                        }
                    }
                } else {
                    System.out.println("No ISBN found");
                }

                title = volumeInfo.optString("title", null);

                JSONArray authorsArray = volumeInfo.optJSONArray("authors");
                if (authorsArray != null) {
                    author = String.join(", ", authorsArray.toList().stream()
                            .map(Object::toString).toArray(String[]::new));
                }

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

        return new String[]{isbn, title, author, subject, publisher, description, imageUrl};
    }
}

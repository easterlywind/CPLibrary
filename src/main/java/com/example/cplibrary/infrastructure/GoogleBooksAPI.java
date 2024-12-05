package com.example.cplibrary.infrastructure;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GoogleBooksAPI {
    private static final String API_KEY = "AIzaSyBIvKi4gikZqtiHyEEt2kXhD0QUZ8gXyek";
    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes";

    /**
     * Gửi yêu cầu HTTP GET và nhận phản hồi dưới dạng chuỗi JSON.
     *
     * @param requestUrl URL của yêu cầu.
     * @return Chuỗi JSON phản hồi từ API.
     * @throws Exception Nếu xảy ra lỗi kết nối hoặc đọc dữ liệu.
     */
    private static String sendGetRequest(String requestUrl) throws Exception {
        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new Exception("HTTP GET Request failed with error code: " + responseCode);
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                content.append(line);
            }
            return content.toString();
        }
    }

    /**
     * Phân tích phản hồi JSON từ API để lấy thông tin sách.
     *
     * @param jsonResponse Chuỗi JSON từ API.
     * @return Danh sách thông tin sách.
     */
    private static List<String[]> parseBooksFromJson(String jsonResponse) {
        List<String[]> books = new ArrayList<>();
        JSONObject responseJson = new JSONObject(jsonResponse);
        JSONArray items = responseJson.optJSONArray("items");

        if (items == null) {
            return books;
        }

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

            books.add(new String[]{isbn, title, author, subject, publisher, description, imageUrl});
        }

        return books;
    }

    /**
     * Lấy thông tin sách từ API Google Books.
     *
     * @param query      Từ khóa tìm kiếm hoặc ISBN.
     * @param isKeyword  True nếu là từ khóa, False nếu là ISBN.
     * @param startIndex Chỉ số bắt đầu để hỗ trợ phân trang (mặc định là 0).
     * @return Danh sách thông tin sách.
     */
    public static List<String[]> fetchBookDetails(String query, boolean isKeyword, int startIndex) {
        List<String[]> books = new ArrayList<>();

        try {
            query = isKeyword ? "isbn:" + query : query.replace(" ", "+");
            String requestUrl = BASE_URL + "?q=" + query + "&startIndex=" + startIndex
                    + "&maxResults=10&key=" + API_KEY;

            System.out.println("Requesting URL: " + requestUrl);

            String jsonResponse = sendGetRequest(requestUrl);
            books = parseBooksFromJson(jsonResponse);

        } catch (Exception e) {
            System.err.println("Error fetching book details: " + e.getMessage());
            e.printStackTrace();
        }

        return books;
    }

    /**
     * Overload phương thức fetchBookDetails để sử dụng mặc định startIndex = 0.
     */
    public static List<String[]> fetchBookDetails(String query, boolean isKeyword) {
        return fetchBookDetails(query, isKeyword, 0);
    }
}

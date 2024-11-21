package com.example.cplibrary.infrastructure;

import com.example.cplibrary.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLReviewRepository {
    private final DatabaseConnection databaseConnection = new DatabaseConnection();

    public List<String[]> getReviewsByBookId(int bookId) {
        List<String[]> reviews = new ArrayList<>();
        String query = "SELECT r.review, r.review_date, u.name FROM Reviews r " +
                "JOIN Users u ON r.user_id = u.user_id " +
                "WHERE r.book_id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reviews.add(new String[]{rs.getString("review"), rs.getDate("review_date").toString()
                            , rs.getString("name")});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    public void addReview(int bookId, int userId, String review) {
        String insertQuery = "INSERT INTO reviews (book_id, user_id, review, review_date) VALUES (?, ?, ?, CURDATE())";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
            stmt.setInt(1, bookId);
            stmt.setInt(2, userId);
            stmt.setString(3, review);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

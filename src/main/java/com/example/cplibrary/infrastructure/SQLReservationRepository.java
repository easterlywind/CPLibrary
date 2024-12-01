package com.example.cplibrary.infrastructure;

import com.example.cplibrary.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class SQLReservationRepository {
    private final DatabaseConnection databaseConnection = new DatabaseConnection();

    public ObservableList<Object[]> fetchReservationData(int userId) {
        ObservableList<Object[]> data = FXCollections.observableArrayList();

        String query = """
            SELECT b.title, r.reservation_date, r.status
            FROM books b
            JOIN reservations r on b.book_id = r.book_id
            WHERE r.user_id = ?;
        """;

        try (Connection connection = databaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1,userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String title = rs.getString("title");
                LocalDate reservationDate = rs.getDate("reservation_date").toLocalDate();
                String status = rs.getString("status");
                data.add(new Object[]{title,reservationDate,status});
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return data;
    }
}

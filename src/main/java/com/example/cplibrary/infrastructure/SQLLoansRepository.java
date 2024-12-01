package com.example.cplibrary.infrastructure;

import com.example.cplibrary.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class SQLLoansRepository {
    private DatabaseConnection databaseConnection = new DatabaseConnection();

    public ObservableList<Object[]> fetchLoanData(int userId) {
        ObservableList<Object[]> data = FXCollections.observableArrayList();

        String query = """
            SELECT b.title, l.borrow_date, l.due_date
            FROM loans l
            JOIN Books b ON l.book_id = b.book_id
            WHERE l.user_id = ?;
        """;

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, userId); // Thay userId bằng giá trị cần lọc
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String title = rs.getString("title");
                LocalDate borrowDate = rs.getDate("borrow_date").toLocalDate();
                LocalDate dueDate = rs.getDate("due_date").toLocalDate();
                data.add(new Object[]{title, borrowDate, dueDate});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }
}

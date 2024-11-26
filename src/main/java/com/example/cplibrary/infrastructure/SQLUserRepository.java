package com.example.cplibrary.infrastructure;

import com.example.cplibrary.model.Member;
import com.example.cplibrary.model.User;
import com.example.cplibrary.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLUserRepository {

    private final DatabaseConnection databaseConnection = new DatabaseConnection();

    // Thêm một user mới
    public void addUser(User user) {
        String sqlUser = "INSERT INTO Users (user_id, name, email, password,role, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlUser)) {
            // Thêm user vào bảng Users
            stmt.setInt(1, user.getUserId());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, "member");
            stmt.setString(6, user.getStatus());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(User user) {
        String sql = "UPDATE Users SET name = ?, email = ?, password = ?, status = ? WHERE user_id = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getName()); // name
            stmt.setString(2, user.getEmail()); // email
            stmt.setString(3, user.getPassword()); // password
            stmt.setString(4, user.getStatus()); // status
            stmt.setInt(5, user.getUserId()); // user_id (for WHERE clause)
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Xóa một user
    public void deleteUser(int userId) {
        String sql = "DELETE FROM Users WHERE user_id = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Member getUserByQuery(String query, String parameter) {
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, parameter);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String status = rs.getString("status");
                String password = rs.getString("password");
                return new Member(userId, name, email, password, status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Member getUserById(int userId) {
        return getUserByQuery("SELECT user_id, name, email, status, password FROM Users WHERE user_id = ?", String.valueOf(userId));
    }

    public Member getUserByEmail(String email) {
        return getUserByQuery("SELECT user_id, name, email, status, password FROM Users WHERE email = ?", email);
    }

    // Lấy tất cả users theo status
    public List<Member> getUsersByStatus(String status) {
        String sql = "SELECT user_id, name, email, status, password FROM Users WHERE status = ?";
        List<Member> users = new ArrayList<>();
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int userId = rs.getInt("user_id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                users.add(new Member(userId, name, email, password, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

}

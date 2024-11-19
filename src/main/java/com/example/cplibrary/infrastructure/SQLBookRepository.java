package com.example.cplibrary.infrastructure;

import com.example.cplibrary.model.Book;
import com.example.cplibrary.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLBookRepository {

    private final DatabaseConnection databaseConnection = new DatabaseConnection();

    // Thêm sách vào bảng Books
    public void addBook(Book book) {
        String sql = "INSERT INTO Books (book_id, isbn, title, author, subject, publisher, shelf_location, review) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, book.getBook_id());
            stmt.setString(2, book.getIsbn());
            stmt.setString(3, book.getTitle());
            stmt.setString(4, book.getAuthor());
            stmt.setString(5, book.getSubject());
            stmt.setString(6, book.getPublisher());
            stmt.setString(7, book.getShelfLocation());
            stmt.setString(8, book.getReview());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Cập nhật thông tin sách
    public void updateBook(Book book) {
        String sql = "UPDATE Books SET title = ?, author = ?, subject = ?, publisher = ?, shelf_location = ?, review = ? " +
                "WHERE isbn = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getSubject());
            stmt.setString(4, book.getPublisher());
            stmt.setString(5, book.getShelfLocation());
            stmt.setString(6, book.getReview());
            stmt.setString(7, book.getIsbn());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xóa sách theo ISBN
    public void deleteBook(String isbn) {
        String sql = "DELETE FROM Books WHERE isbn = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy thông tin sách theo ISBN
    public Book getBookByIsbn(String isbn) {
        String sql = "SELECT book_id, isbn, title, author, subject, publisher, shelf_location, review " +
                "FROM Books WHERE isbn = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Book(
                        rs.getInt("book_id"),
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("subject"),
                        rs.getString("publisher"),
                        rs.getString("shelf_location"),
                        rs.getString("review")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Tìm kiếm sách theo tiêu chí người dùng chỉ định
    public List<Book> searchBooks(String title, String author, String subject) {
        StringBuilder sql = new StringBuilder("SELECT book_id, isbn, title, author, subject, publisher, shelf_location, review FROM Books WHERE 1=1");
        List<Book> books = new ArrayList<>();
        List<String> params = new ArrayList<>();

        if (title != null && !title.isEmpty()) {
            sql.append(" AND title LIKE ?");
            params.add("%" + title + "%");
        }
        if (author != null && !author.isEmpty()) {
            sql.append(" AND author LIKE ?");
            params.add("%" + author + "%");
        }
        if (subject != null && !subject.isEmpty()) {
            sql.append(" AND subject LIKE ?");
            params.add("%" + subject + "%");
        }

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setString(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("book_id"),
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("subject"),
                        rs.getString("publisher"),
                        rs.getString("shelf_location"),
                        rs.getString("review")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }
}

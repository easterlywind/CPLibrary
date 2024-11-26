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
        String sql = "INSERT INTO Books (book_id, quantity, isbn, title, author, subject, publisher, shelf_location, review, image_url) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, book.getBook_id());
            stmt.setInt(2, book.getQuantity());
            stmt.setString(3, book.getIsbn());
            stmt.setString(4, book.getTitle());
            stmt.setString(5, book.getAuthor());
            stmt.setString(6, book.getSubject());
            stmt.setString(7, book.getPublisher());
            stmt.setString(8, book.getShelfLocation());
            stmt.setString(9, book.getReview());
            stmt.setString(10, book.getImageUrl()); // Image URL
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Cập nhật thông tin sách (bao gồm cả quantity)
    public void updateBook(Book book) {
        String sql = "UPDATE Books SET title = ?, author = ?, subject = ?, publisher = ?, shelf_location = ?, review = ?, quantity = ?, image_url = ? " +
                "WHERE isbn = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getSubject());
            stmt.setString(4, book.getPublisher());
            stmt.setString(5, book.getShelfLocation());
            stmt.setString(6, book.getReview());
            stmt.setInt(7, book.getQuantity());
            stmt.setString(8, book.getImageUrl()); // Image URL
            stmt.setString(9, book.getIsbn());
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
        String sql = "SELECT book_id, isbn, title, author, subject, publisher, shelf_location, review, quantity, image_url " +
                "FROM Books WHERE isbn = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Book(
                        rs.getInt("book_id"),
                        rs.getInt("quantity"),
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("subject"),
                        rs.getString("publisher"),
                        rs.getString("shelf_location"),
                        rs.getString("review"),
                        rs.getString("image_url") // Image URL
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Tìm kiếm sách theo từ khóa (search word)
    public List<Book> searchBooks(String keyword) {
        String sql = "SELECT book_id, isbn, title, author, subject, publisher, shelf_location, review, quantity, image_url " +
                "FROM Books " +
                "WHERE title LIKE ? OR author LIKE ? OR subject LIKE ? OR isbn LIKE ? OR publisher LIKE ?";
        List<Book> books = new ArrayList<>();

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            for (int i = 1; i <= 5; i++) {
                stmt.setString(i, searchPattern);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("book_id"),
                        rs.getInt("quantity"),
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("subject"),
                        rs.getString("publisher"),
                        rs.getString("shelf_location"),
                        rs.getString("review"),
                        rs.getString("image_url") // Image URL
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }



    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT book_id, isbn, title, author, subject, publisher, shelf_location, review, quantity, image_url FROM Books";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("book_id"),
                        rs.getInt("quantity"),
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("subject"),
                        rs.getString("publisher"),
                        rs.getString("shelf_location"),
                        rs.getString("review"),
                        rs.getString("image_url") // Image URL
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public void updateQuantity(int bookId, int quantityChange) {
        String query = "UPDATE Books SET quantity = quantity + ? WHERE book_id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, quantityChange);
            stmt.setInt(2, bookId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

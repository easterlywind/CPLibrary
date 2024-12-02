package com.example.cplibrary.infrastructure;

import com.example.cplibrary.model.Book;
import com.example.cplibrary.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
            stmt.setString(8, book.getImageUrl());
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
    public Book getBookByIsbn(String title) {
        String sql = "SELECT book_id, isbn, title, author, subject, publisher, shelf_location, review, quantity, image_url " +
                "FROM Books WHERE title = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
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
                        rs.getString("image_url")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public boolean updateQuantity(int bookId, int quantityChange) {
        try (Connection connection = databaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            String updateQuery = "UPDATE Books SET quantity = quantity + ? WHERE book_id = ?";
            try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                updateStmt.setInt(1, quantityChange);
                updateStmt.setInt(2, bookId);
                updateStmt.executeUpdate();
            }

            String selectReservationQuery = "SELECT user_id FROM Reservations WHERE book_id = ?";
            try (PreparedStatement selectStmt = connection.prepareStatement(selectReservationQuery)) {
                selectStmt.setInt(1, bookId);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    while (rs.next()) {
                        int userId = rs.getInt("user_id");

                        String insertLoanQuery = "INSERT INTO Loans (book_id, user_id, borrow_date, due_date) VALUES (?, ?, NOW(), DATE_ADD(NOW(), INTERVAL 14 DAY))";
                        try (PreparedStatement insertLoanStmt = connection.prepareStatement(insertLoanQuery)) {
                            insertLoanStmt.setInt(1, bookId);
                            insertLoanStmt.setInt(2, userId);
                            insertLoanStmt.executeUpdate();
                        }

                        String decrementQuery = "UPDATE Books SET quantity = quantity - 1 WHERE book_id = ?";
                        try (PreparedStatement decrementStmt = connection.prepareStatement(decrementQuery)) {
                            decrementStmt.setInt(1, bookId);
                            decrementStmt.executeUpdate();
                        }

                        String deleteReservationQuery = "DELETE FROM Reservations WHERE user_id = ? AND book_id = ?";
                        try (PreparedStatement deleteStmt = connection.prepareStatement(deleteReservationQuery)) {
                            deleteStmt.setInt(1, userId);
                            deleteStmt.setInt(2, bookId);
                            deleteStmt.executeUpdate();
                        }
                    }
                }
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public void addLoan(int bookId, int userId) {
        String query = "INSERT INTO Loans (book_id, user_id, borrow_date, due_date) VALUES (?, ?, ?, ?)";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, bookId);
            stmt.setInt(2, userId);
            stmt.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            stmt.setDate(4, java.sql.Date.valueOf(LocalDate.now().plusDays(14))); // Thời hạn 14 ngày
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean deleteLoan(int userId, int bookId) {
        String query = "DELETE FROM Loans WHERE user_id = ? AND book_id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addReservation(int userId, int bookId) {
        String query = "INSERT INTO Reservations (book_id, user_id, reservation_date) VALUES (?, ?, ?)";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, bookId);
            stmt.setInt(2, userId);
            stmt.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isBookBorrowedByUser(int userId, int bookId) {
        String query = "SELECT COUNT(*) FROM Loans WHERE user_id = ? AND book_id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isBookReservedByUser(int userId, int bookId) {
        String query = "SELECT COUNT(*) FROM Reservations WHERE user_id = ? AND book_id = ? AND status = 'pending'";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}

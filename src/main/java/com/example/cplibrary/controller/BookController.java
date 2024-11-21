package com.example.cplibrary.controller;

import com.example.cplibrary.model.Book;
import com.example.cplibrary.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class BookController {

    @FXML
    private Label titleLabel;

    @FXML
    private Label authorLabel;

    @FXML
    private Label subjectLabel;

    @FXML
    private Label shelfLocationLabel;

    @FXML
    private Label quantityLabel;

    @FXML
    private VBox reviewList;

    @FXML
    private TextArea reviewInput;

    @FXML
    private Button borrowButton;

    @FXML
    ImageView bookImage;

    @FXML
    Label nameLabel;

    @FXML
    Button backButton;

    private Book book;

    public void backButtonOnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/staffLib.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void setBookDetails(Book book) {
        this.book = book;
        nameLabel.setText("admin");
        // Hiển thị thông tin sách
        titleLabel.setText(book.getTitle());
        authorLabel.setText(book.getAuthor());
        subjectLabel.setText(book.getSubject());
        shelfLocationLabel.setText(book.getShelfLocation());
        quantityLabel.setText(String.valueOf(book.getQuantity()));

        Image image = new Image(getClass().getResource("/image/img.png").toExternalForm(),200,300,true,true);
        bookImage.setImage(image);

        // Tải các review từ database
        loadReviews();
    }
    private final DatabaseConnection databaseConnection = new DatabaseConnection();
    @FXML
    private void addReview() {
        String newReview = reviewInput.getText();
        if (newReview.isEmpty()) {
            return;
        }

        try (Connection connection = databaseConnection.getConnection()) {
            String insertQuery = "INSERT INTO reviews (review_id, book_id, user_id, review, review_date) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
                stmt.setInt(1, book.getBook_id());
                stmt.setInt(2, book.getBook_id()); // Giả sử user_id là 1 (cần thay đổi theo người dùng hiện tại)
                stmt.setInt(3, 1);
                stmt.setString(4, newReview);
                stmt.setDate(5, java.sql.Date.valueOf(LocalDate.now()));
                stmt.executeUpdate();
            }
            // Thêm review mới vào UI
            reviewList.getChildren().add(createReviewBox(newReview, LocalDate.now().toString()));

            // Xóa text area
            reviewInput.clear();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadReviews() {
        try (Connection connection = databaseConnection.getConnection()) {
            String query = "SELECT review, review_date FROM Reviews WHERE book_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, book.getBook_id());
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String reviewText = rs.getString("review");
                        String reviewDate = rs.getDate("review_date").toString();
                        reviewList.getChildren().add(createReviewBox(reviewText, reviewDate));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private VBox createReviewBox(String reviewText, String reviewDate) {
        VBox reviewBox = new VBox();
        reviewBox.setSpacing(5);

        Label reviewContent = new Label(reviewText);
        reviewContent.setWrapText(true);

        Label reviewDateLabel = new Label("Date: " + reviewDate);
        reviewDateLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: gray;");

        reviewBox.getChildren().addAll(reviewContent, reviewDateLabel);
        return reviewBox;
    }

    @FXML
    private void handleBorrowAction() {
        try (Connection connection = databaseConnection.getConnection()) {
            if (borrowButton.getText().equals("Borrow")) {
                // Kiểm tra số lượng sách
                if (book.getQuantity() > 0) {
                    // Giảm quantity trong database
                    String updateQuery = "UPDATE Books SET quantity = quantity - 1 WHERE book_id = ?";
                    try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
                        stmt.setInt(1, book.getBook_id());
                        stmt.executeUpdate();
                    }

                    // Cập nhật UI
                    book.setQuantity(book.getQuantity() - 1);
                    quantityLabel.setText(String.valueOf(book.getQuantity()));
                    borrowButton.setText("Return");
                } else {
                    System.out.println("No books available for borrowing!");
                }
            } else if (borrowButton.getText().equals("Return")) {
                // Tăng quantity trong database
                String updateQuery = "UPDATE Books SET quantity = quantity + 1 WHERE book_id = ?";
                try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
                    stmt.setInt(1, book.getBook_id());
                    stmt.executeUpdate();
                }

                // Cập nhật UI
                book.setQuantity(book.getQuantity() + 1);
                quantityLabel.setText(String.valueOf(book.getQuantity()));
                borrowButton.setText("Borrow");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        borrowButton.setOnAction(event -> handleBorrowAction());
    }
}

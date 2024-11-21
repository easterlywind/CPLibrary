package com.example.cplibrary.controller;

import com.example.cplibrary.infrastructure.GoogleBooksAPI;
import com.example.cplibrary.infrastructure.SQLBookRepository;
import com.example.cplibrary.infrastructure.SQLReviewRepository;
import com.example.cplibrary.model.Book;
import com.example.cplibrary.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import java.util.ArrayList;
import java.util.List;

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

    @FXML
    TextField quantityCopyInput;

    private final SQLBookRepository bookRepository = new SQLBookRepository();
    private final SQLReviewRepository reviewRepository = new SQLReviewRepository();

    private Book book;
    private int currentUserId = 1;

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

        List<String> isbnBookList = new ArrayList<>();
        isbnBookList.add(book.getIsbn());

        List<String> imageUrls = GoogleBooksAPI.fetchBookImageURLs(isbnBookList);
        String imageUrl = imageUrls.get(0);
        Image image = imageUrl != null && !imageUrl.isEmpty()
                ? new Image(imageUrl, 200, 300, true, true)
                : new Image(getClass().getResource("/image/img.png").toExternalForm(), 200, 300, true, true);
        bookImage.setImage(image);

        // Tải các review từ database
        loadReviews();
    }
    @FXML
    private void addReview() {
        String newReview = reviewInput.getText();
        if (!newReview.isEmpty()) {
            // Lấy tên người review (giả sử là người dùng hiện tại)
            String reviewerName = "admin"; // Bạn có thể thay bằng tên người dùng thực tế từ session hoặc login
            reviewRepository.addReview(book.getBook_id(), currentUserId, newReview);
            reviewList.getChildren().add(createReviewBox(newReview, LocalDate.now().toString(), reviewerName));
            reviewInput.clear();
        }
    }


    private void loadReviews() {
        reviewList.getChildren().clear();
        List<String[]> reviews = reviewRepository.getReviewsByBookId(book.getBook_id());
        for (String[] reviewData : reviews) {
            String reviewText = reviewData[0];
            String reviewDate = reviewData[1];
            String reviewName = reviewData[2];
            reviewList.getChildren().add(createReviewBox(reviewText, reviewDate, reviewName));
        }
    }

    private VBox createReviewBox(String reviewText, String reviewDate, String reviewerName) {
        VBox reviewBox = new VBox();
        reviewBox.setSpacing(5);

        // Tạo Label cho tên người review
        Label reviewerNameLabel = new Label("Reviewed by: " + reviewerName);
        reviewerNameLabel.setStyle("-fx-font-weight: bold;");

        // Tạo Label cho nội dung review
        Label reviewContent = new Label(reviewText);
        reviewContent.setWrapText(true);

        // Tạo Label cho ngày review
        Label reviewDateLabel = new Label("Date: " + reviewDate);
        reviewDateLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: gray;");

        reviewBox.getChildren().addAll(reviewerNameLabel, reviewContent, reviewDateLabel);
        return reviewBox;
    }



    @FXML
    private void handleBorrowAction() {
        if (borrowButton.getText().equals("Borrow") && book.getQuantity() > 0) {
            bookRepository.updateQuantity(book.getBook_id(), -1);
            book.setQuantity(book.getQuantity() - 1);
            quantityLabel.setText(String.valueOf(book.getQuantity()));
            borrowButton.setText("Return");
        } else if (borrowButton.getText().equals("Return")) {
            bookRepository.updateQuantity(book.getBook_id(), 1);
            book.setQuantity(book.getQuantity() + 1);
            quantityLabel.setText(String.valueOf(book.getQuantity()));
            borrowButton.setText("Borrow");
        }
    }

    public void addQuantity() {
        int countOfCopy = Integer.parseInt(quantityCopyInput.getText());
        bookRepository.updateQuantity(book.getBook_id(), countOfCopy);
        book.setQuantity(book.getQuantity() + countOfCopy);
        quantityLabel.setText(String.valueOf(book.getQuantity()));
    }

    @FXML
    public void initialize() {
//        borrowButton.setOnAction(event -> handleBorrowAction());
    }
}

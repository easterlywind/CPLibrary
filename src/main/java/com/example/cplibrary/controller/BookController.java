package com.example.cplibrary.controller;

import com.example.cplibrary.UserSession;
import com.example.cplibrary.infrastructure.GoogleBooksAPI;
import com.example.cplibrary.infrastructure.SQLBookRepository;
import com.example.cplibrary.infrastructure.SQLReviewRepository;
import com.example.cplibrary.model.Book;
import com.example.cplibrary.DatabaseConnection;
import com.example.cplibrary.model.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class BookController {

    @FXML
    private Label titleLabel, authorLabel, subjectLabel, shelfLocationLabel,
            quantityLabel, nameLabel, publisherLabel, isbnLabel, descriptionLabel;

    @FXML
    private VBox reviewList;

    @FXML
    private TextArea reviewInput;

    @FXML
    private Button borrowButton, backButton, saveButton, editButton, addQuantityButton;

    @FXML
    private TextArea titleInput, authorInput, subjectInput, shelfLocationInput, publisherInput, descriptionInput, quantityInput;

    @FXML
    ImageView bookImage;

    @FXML
    TextField quantityCopyInput;


    private final SQLBookRepository bookRepository = new SQLBookRepository();
    private final SQLReviewRepository reviewRepository = new SQLReviewRepository();
    private final User currentUser = UserSession.getInstance().getCurrentUser();

    private Book book;
    public boolean isEditing = false;

    public void backButtonOnAction(ActionEvent event) {
        NavigationManager.switchScene("/staffLib.fxml");
    }

    public void deleteButtonOnAction(ActionEvent event) {
            bookRepository.deleteBook(book.getIsbn());
            backButtonOnAction(event);
    }


    public void setBookDetails(Book book) {
        this.book = book;
        nameLabel.setText(currentUser.getName());
        // Hiển thị thông tin sách
        titleLabel.setText(book.getTitle());
        authorLabel.setText(book.getAuthor());
        subjectLabel.setText(book.getSubject());
        shelfLocationLabel.setText(book.getShelfLocation());
        quantityLabel.setText(String.valueOf(book.getQuantity()));
        isbnLabel.setText(book.getIsbn());
        publisherLabel.setText(book.getPublisher());
        descriptionLabel.setText(book.getReview());


        String imageUrl = book.getImageUrl();


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
            String reviewerName = currentUser.getName(); // Bạn có thể thay bằng tên người dùng thực tế từ session hoặc login
            reviewRepository.addReview(book.getBook_id(), currentUser.getUserId(), newReview);
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

    public void editButtonOnAction() {
        toggleEditing(true);
    }


    public void saveButtonOnAction() {

        // Lấy dữ liệu từ TextArea
        String newTitle = titleInput.getText();
        String newAuthor = authorInput.getText();
        String newSubject = subjectInput.getText();
        String newShelfLocation = shelfLocationInput.getText();
        String newPublisher = publisherInput.getText();
        String newDescription = descriptionInput.getText();
        String newQuantity = quantityInput.getText();

        if (newTitle.isEmpty() || newAuthor.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Title and Author cannot be empty!");
            alert.show();
            return;
        }

        // Cập nhật đối tượng Book
        book.setTitle(newTitle);
        book.setAuthor(newAuthor);
        book.setSubject(newSubject);
        book.setShelfLocation(newShelfLocation);
        book.setPublisher(newPublisher);
        book.setReview(newDescription);
        book.setQuantity(Integer.parseInt(newQuantity));


        // Lưu vào cơ sở dữ liệu
        bookRepository.updateBook(book);

        // Hiển thị dữ liệu mới trên giao diện
        titleLabel.setText(newTitle);
        authorLabel.setText(newAuthor);
        subjectLabel.setText(newSubject);
        shelfLocationLabel.setText(newShelfLocation);
        publisherLabel.setText(newPublisher);
        descriptionLabel.setText(newDescription);
        quantityLabel.setText(String.valueOf(book.getQuantity()));

        // Tắt chế độ chỉnh sửa
        toggleEditing(false);
    }

    private void toggleEditing(boolean enable) {
        isEditing = enable;

        // Chuyển trạng thái giữa Label và TextArea
        titleLabel.setVisible(!enable);
        authorLabel.setVisible(!enable);
        subjectLabel.setVisible(!enable);
        shelfLocationLabel.setVisible(!enable);
        publisherLabel.setVisible(!enable);
        descriptionLabel.setVisible(!enable);
        quantityLabel.setVisible(!enable);

        titleInput.setVisible(enable);
        authorInput.setVisible(enable);
        subjectInput.setVisible(enable);
        shelfLocationInput.setVisible(enable);
        publisherInput.setVisible(enable);
        descriptionInput.setVisible(enable);
        quantityInput.setVisible(enable);

        // Hiển thị nút phù hợp
        editButton.setVisible(!enable);
        saveButton.setVisible(enable);
        borrowButton.setDisable(enable);
        addQuantityButton.setDisable(enable);
        quantityCopyInput.setDisable(enable);

        if (enable) {
            // Sao chép giá trị từ Label sang TextArea khi bắt đầu chỉnh sửa
            titleInput.setText(titleLabel.getText());
            authorInput.setText(authorLabel.getText());
            subjectInput.setText(subjectLabel.getText());
            shelfLocationInput.setText(shelfLocationLabel.getText());
            publisherInput.setText(publisherLabel.getText());
            descriptionInput.setText(descriptionLabel.getText());
            quantityInput.setText(String.valueOf(book.getQuantity()));
        }
    }
    public void switchSceneLibrary(MouseEvent event) {
        NavigationManager.switchScene("/staffLib.fxml");
    }

    public void switchSceneItems(MouseEvent event) {
        NavigationManager.switchScene("/staffItem.fxml");
    }

    public void switchSceneUser(MouseEvent event) {
        NavigationManager.switchScene("/staffUsers.fxml");
    }

    public void switchSceneLogout(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.setContentText("All unsaved changes will be lost.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Platform.exit();
        }
    }

    @FXML
    public void initialize() {
//        borrowButton.setOnAction(event -> handleBorrowAction());
        toggleEditing(false);
    }
}

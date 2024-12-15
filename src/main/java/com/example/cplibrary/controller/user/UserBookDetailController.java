package com.example.cplibrary.controller.user;

import com.example.cplibrary.UserSession;
import com.example.cplibrary.application.BookLoansService;
import com.example.cplibrary.application.BookReservationService;
import com.example.cplibrary.controller.common.AlertManager;
import com.example.cplibrary.controller.common.NavigationManager;
import com.example.cplibrary.infrastructure.SQLBookRepository;
import com.example.cplibrary.infrastructure.SQLReviewRepository;
import com.example.cplibrary.model.Book;
import com.example.cplibrary.model.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class UserBookDetailController {

    @FXML
    private Label titleLabel, authorLabel, subjectLabel, shelfLocationLabel,
            quantityLabel, nameLabel, publisherLabel, isbnLabel, descriptionLabel;

    @FXML
    private VBox reviewList;

    @FXML
    private TextArea reviewInput;

    @FXML
    private Button borrowButton;

    @FXML
    ImageView bookImage;

    private final SQLBookRepository bookRepository = new SQLBookRepository();
    private final SQLReviewRepository reviewRepository = new SQLReviewRepository();
    private final User currentUser = UserSession.getInstance().getCurrentUser();
    private final BookLoansService bookLoansService = new BookLoansService();
    private final BookReservationService bookReservationService = new BookReservationService();

    private Book book;
    public void backButtonOnAction() {
        NavigationManager.switchScene("/userScene/userLib.fxml");
    }

    public void setBookDetails(Book book) {
        this.book = book;
        nameLabel.setText(currentUser.getName());

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

        if (bookLoansService.isBookBorrowedByUser(currentUser.getUserId(), book.getBook_id())) {
            borrowButton.setText("Return");
        } else {
            borrowButton.setText("Borrow");
        }

        loadReviews();
    }

    @FXML
    private void addReview() {
        String newReview = reviewInput.getText();
        if (!newReview.isEmpty()) {

            String reviewerName = currentUser.getName();
            reviewRepository.addReview(book.getBook_id(), currentUser.getUserId(), newReview);
            reviewList.getChildren().add(createReviewBox(newReview, LocalDate.now().toString(), reviewerName));
            reviewInput.clear();
            AlertManager.showInfoAlert("Review Added", "Success", "Thank you! Your review has been added successfully.");
        }
    }

    private void loadReviews() {
        reviewList.setSpacing(10);
        reviewList.setStyle("-fx-padding: 10; -fx-background-color: #f5f5f5;");

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

        reviewBox.setStyle("-fx-background-color: #fbe7c6; " +
                "-fx-border-color: #f4a261; " +
                "-fx-border-radius: 10; " +
                "-fx-background-radius: 10; " +
                "-fx-padding: 10;");

        Label reviewerNameLabel = new Label("Reviewed by: " + reviewerName);
        reviewerNameLabel.setStyle("-fx-font-weight: bold;");

        Label reviewContent = new Label(reviewText);
        reviewContent.setWrapText(true);

        Label reviewDateLabel = new Label("Date: " + reviewDate);
        reviewDateLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: gray;");

        reviewBox.getChildren().addAll(reviewerNameLabel, reviewContent, reviewDateLabel);
        return reviewBox;
    }

    @FXML
    private void handleBorrowAction() {
        if (borrowButton.getText().equals("Borrow")) {

            if (isBookAlreadyBorrowedOrReserved(false)) {
                return;
            }

            if (book.getQuantity() > 0) {

                bookRepository.updateQuantity(book.getBook_id(), -1);
                book.setQuantity(book.getQuantity() - 1);
                quantityLabel.setText(String.valueOf(book.getQuantity()));

                bookLoansService.addLoan(book.getBook_id(), currentUser.getUserId());

                borrowButton.setText("Return");

                AlertManager.showInfoAlert(
                        "Success",
                        "Book Borrowed Successfully",
                        "You have successfully borrowed the book '" + book.getTitle() + "'."
                );

            } else {
                AlertManager.showInfoAlert(
                        "NOTIFICATION",
                        "Out of Stock",
                        "This book is out of stock. Would you like to reserve it?"
                );
                handleReserveBook();
            }

        } else if (borrowButton.getText().equals("Return")) {

            if (bookLoansService.deleteLoan(currentUser.getUserId(), book.getBook_id())) {

                bookRepository.updateQuantity(book.getBook_id(), 1);
                book.setQuantity(book.getQuantity() + 1);
                quantityLabel.setText(String.valueOf(book.getQuantity()));

                borrowButton.setText("Borrow");

                AlertManager.showInfoAlert(
                        "Success",
                        "Book Returned Successfully",
                        "You have successfully returned the book '" + book.getTitle() + "'."
                );

            } else {
                AlertManager.showErrorAlert(
                        "ERROR",
                        "Return Error",
                        "Unable to return the book. Please try again."
                );
            }
        }
    }

    public void switchSceneLibrary() {
        NavigationManager.switchScene("/userScene/userLib.fxml");
    }

    public void switchSceneLoans() {
        NavigationManager.switchScene("/userScene/userLoans.fxml");
    }

    public void switchSceneReservation() {
        NavigationManager.switchScene("/userScene/userReservation.fxml");
    }

    public void switchSceneRcm(MouseEvent mouseEvent) {
        NavigationManager.switchScene("/userScene/userRcm.fxml");
    }

    public void switchSceneLogout() {
        boolean confirmed = AlertManager.showConfirmationAlert("CONFIRMATION", "Are you sure you want to logout?" ,"All unsaved changes will be lost.");
        if (confirmed) {
            NavigationManager.switchScene("/commonScene/login.fxml");
        }
    }

    private void handleReserveBook() {
        bookReservationService.addReservation(currentUser.getUserId(), book.getBook_id());
        AlertManager.showInfoAlert("NOTIFICATION", "Reservation Success", "You have successfully reserved the book.");

    }

    public boolean isBookAlreadyBorrowedOrReserved(boolean isReturning) {
        boolean isBookAlreadyBorrowed = bookLoansService.isBookBorrowedByUser(currentUser.getUserId(), book.getBook_id());
        boolean isBookAreadyReserved = bookReservationService.isBookReservedByUser(currentUser.getUserId(), book.getBook_id());

        if (!isReturning && isBookAlreadyBorrowed) {
            AlertManager.showWarningAlert("WARNING", "Book Already Borrowded", "You have already borrow this book.");
            return true;
        }

        if (!isReturning && isBookAreadyReserved) {
            AlertManager.showWarningAlert("WARNING", "Book Already Reserved", "You have already reserved this book.");
            return true;
        }

        return false;
    }

    @FXML
    public void initialize() {
//        borrowButton.setOnAction(event -> handleBorrowAction());

    }
}

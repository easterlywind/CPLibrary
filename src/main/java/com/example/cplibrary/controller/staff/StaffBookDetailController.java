package com.example.cplibrary.controller.staff;

import com.example.cplibrary.UserSession;
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

public class StaffBookDetailController {

    @FXML
    private Label titleLabel, authorLabel, subjectLabel, shelfLocationLabel,
            quantityLabel, nameLabel, publisherLabel, isbnLabel, descriptionLabel;

    @FXML
    private VBox reviewList;

    @FXML
    private TextArea reviewInput;

    @FXML
    private Button saveButton, editButton, addQuantityButton;

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
        NavigationManager.switchScene("/staffScene/staffLib.fxml");
    }

    public void deleteButtonOnAction(ActionEvent event) {
            bookRepository.deleteBook(book.getIsbn());
            AlertManager.showInfoAlert("NOTIFICATION","Deleted Succesfully", "User information has been deleted.");
            backButtonOnAction(event);
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

    public void addQuantity() {
        int countOfCopy = Integer.parseInt(quantityCopyInput.getText());
        bookRepository.updateQuantity(book.getBook_id(), countOfCopy);
        book.setQuantity(book.getQuantity() + countOfCopy);
        quantityLabel.setText(String.valueOf(book.getQuantity()));
        AlertManager.showInfoAlert(
                "Quantity Updated",
                "Book Quantity Updated Successfully",
                "You have successfully added " + countOfCopy +
                        " copies. Current total: " + book.getQuantity() + " copies."
        );
    }

    public void editButtonOnAction() {
        toggleEditing(true);
    }


    public void saveButtonOnAction() {

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

        book.setTitle(newTitle);
        book.setAuthor(newAuthor);
        book.setSubject(newSubject);
        book.setShelfLocation(newShelfLocation);
        book.setPublisher(newPublisher);
        book.setReview(newDescription);
        book.setQuantity(Integer.parseInt(newQuantity));

        bookRepository.updateBook(book);

        titleLabel.setText(newTitle);
        authorLabel.setText(newAuthor);
        subjectLabel.setText(newSubject);
        shelfLocationLabel.setText(newShelfLocation);
        publisherLabel.setText(newPublisher);
        descriptionLabel.setText(newDescription);
        quantityLabel.setText(String.valueOf(book.getQuantity()));

        toggleEditing(false);
        AlertManager.showInfoAlert("NOTIFICATION","Updated Successfully","User information has been updated.");
    }

    private void toggleEditing(boolean enable) {
        isEditing = enable;

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

        editButton.setVisible(!enable);
        saveButton.setVisible(enable);
        addQuantityButton.setDisable(enable);
        quantityCopyInput.setDisable(enable);

        if (enable) {

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
        NavigationManager.switchScene("/staffScene/staffLib.fxml");
    }

    public void switchSceneItems(MouseEvent event) {
        NavigationManager.switchScene("/staffScene/staffBook.fxml");
    }

    public void switchSceneUser(MouseEvent event) {
        NavigationManager.switchScene("/staffScene/staffUser.fxml");
    }

    public void switchSceneLogout() {
        boolean confirmed = AlertManager.showConfirmationAlert("CONFIRMATION", "Are you sure you want to logout?" ,"All unsaved changes will be lost.");
        if (confirmed) {
            NavigationManager.switchScene("/commonScene/login.fxml");
        }
    }

    @FXML
    public void initialize() {
        toggleEditing(false);
    }
}

package com.example.cplibrary.controller;

import com.example.cplibrary.infrastructure.GoogleBooksAPI;
import com.example.cplibrary.infrastructure.SQLBookRepository;
import com.example.cplibrary.model.Book;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class StaffItemController {

    @FXML
    private TextField searchField;

    @FXML
    private VBox booksListContainer;

    @FXML
    private ProgressIndicator loadingSpinner;

    @FXML
    private void onSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            booksListContainer.getChildren().clear();
            return;
        }


        loadingSpinner.setVisible(true);


        Task<Book> searchTask = new Task<>() {
            @Override
            protected Book call() {
                updateProgress(0, 1);

                String[] bookDetails = keyword.chars().anyMatch(Character::isDigit)
                        ? GoogleBooksAPI.fetchBookDetails(keyword, false)
                        : GoogleBooksAPI.fetchBookDetails(keyword, true);
                updateProgress(1, 1);

//                System.out.println(bookDetails[0]);

                return new Book(
                        0,
                        0,
                        bookDetails[0],
                        bookDetails[1],
                        bookDetails[2],
                        bookDetails[3],
                        bookDetails[4],
                        "N/A",
                        bookDetails[5]
                );
            }
        };


        loadingSpinner.progressProperty().bind(searchTask.progressProperty());


        searchTask.setOnSucceeded(event -> {
            loadingSpinner.setVisible(false);
            loadingSpinner.progressProperty().unbind();

            Book book = searchTask.getValue();

            booksListContainer.getChildren().clear();
            booksListContainer.getChildren().add(createBookItem(book));
        });

        searchTask.setOnFailed(event -> {
            loadingSpinner.setVisible(false);
            loadingSpinner.progressProperty().unbind();
            System.err.println("Failed to fetch book details: " + searchTask.getException());
        });

        new Thread(searchTask).start();
    }


    private VBox createBookItem(Book book) {
        VBox bookItem = new VBox(10);
        bookItem.setStyle("-fx-padding: 10; -fx-border-color: lightgray; -fx-border-radius: 5;");

        String imageUrl = GoogleBooksAPI.fetchBookDetails(book.getIsbn(), false)[6];
        Image bookImage = imageUrl != null
                ? new Image(imageUrl, 200, 300, true, true)
                : new Image(getClass().getResource("/image/img.png").toExternalForm(), 200, 300, true, true);

        bookItem.getChildren().addAll(
                new ImageView(bookImage),
                new javafx.scene.text.Text("ISBN: " + book.getIsbn()),
                new javafx.scene.text.Text("Title: " + book.getTitle()),
                new javafx.scene.text.Text("Author: " + book.getAuthor()),
                new javafx.scene.text.Text("Subject: " + book.getSubject()),
                new javafx.scene.text.Text("Publisher: " + book.getPublisher()),
                new javafx.scene.text.Text("Review: " + book.getReview())
        );

        return bookItem;
    }

    private Button createDeleteButton(Book book) {
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> {
            booksListContainer.getChildren().removeIf(node -> {
                if (node instanceof HBox) {
                    HBox item = (HBox) node;
                    return item.getUserData() == book;
                }
                return false;
            });
        });
        return deleteButton;
    }

    private Button createViewButton(Book book) {
        Button viewButton = new Button("View");
        viewButton.setOnAction(event -> {
            // Logic to view book details
            System.out.println("Viewing: " + book.getTitle());
        });
        return viewButton;
    }
}

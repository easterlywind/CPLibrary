package com.example.cplibrary.controller;

import com.example.cplibrary.infrastructure.GoogleBooksAPI;
import com.example.cplibrary.infrastructure.SQLBookRepository;
import com.example.cplibrary.model.Book;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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

    private final SQLBookRepository bookRepository = new SQLBookRepository();

    @FXML
    private void onSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            booksListContainer.getChildren().clear();
            return;
        }

//        List<Book> books = bookRepository.searchBooks(keyword);
        List<Book> books = new ArrayList<>();

        booksListContainer.getChildren().clear();
        for (Book book : books) {
            HBox bookItem = createBookItem(book);
            booksListContainer.getChildren().add(bookItem);
        }
    }


    private HBox createBookItem(Book book) {
        HBox bookItem = new HBox(10);
        bookItem.setStyle("-fx-padding: 10; -fx-border-color: lightgray; -fx-border-radius: 5;");

        String imageUrl = GoogleBooksAPI.fetchBookDetails(book.getIsbn())[5];
        Image image = imageUrl != null && !imageUrl.isEmpty()
                ? new Image(imageUrl, 200, 300, true, true)
                : new Image(getClass().getResource("/image/img.png").toExternalForm(), 200, 300, true, true);

        ImageView bookImage = new ImageView(image);

        // Add details
        VBox bookDetails = new VBox(5);
        bookDetails.getChildren().addAll(
                new Text("Title: " + book.getTitle()),
                new Text("Author: " + book.getAuthor()),
                new Text("ISBN: " + book.getIsbn()),
                new Text("Subject: " + book.getSubject()),
                new Text("Publisher: " + book.getPublisher())
        );

        // Add buttons
        Button addButton = new Button("Add Book");
        addButton.setOnAction(event -> {
            bookDetails.getChildren().addAll(
                    createDeleteButton(book),
                    createViewButton(book)
            );
            addButton.setDisable(true); // Disable Add Button
        });

        bookDetails.getChildren().add(addButton);
        bookItem.getChildren().addAll(bookImage, bookDetails);

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

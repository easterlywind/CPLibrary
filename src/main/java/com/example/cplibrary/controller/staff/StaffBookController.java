package com.example.cplibrary.controller.staff;

import com.example.cplibrary.UserSession;
import com.example.cplibrary.controller.common.AlertManager;
import com.example.cplibrary.controller.common.NavigationManager;
import com.example.cplibrary.infrastructure.GoogleBooksAPI;
import com.example.cplibrary.infrastructure.SQLBookRepository;
import com.example.cplibrary.model.Book;
import com.example.cplibrary.model.User;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StaffBookController {

    @FXML
    private TextField searchField;

    @FXML
    private VBox booksListContainer;

    @FXML
    private ProgressIndicator loadingSpinner;

    @FXML
    private Label nameLabel;

    private final SQLBookRepository sqlBookRepository = new SQLBookRepository();
    private boolean flag = false;
    private final User currentUser = UserSession.getInstance().getCurrentUser();

    public void onSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            booksListContainer.getChildren().clear();
            return;
        }
        flag = keyword.matches("\\d{10}|\\d{13}");


        loadingSpinner.setVisible(true);

        Task<List<Book>> searchTask = new Task<>() {
            @Override
            protected List<Book> call() {
                int maxPagesToLoad = 2;
                List<Book> books = new ArrayList<>();

                for (int pageIndex = 0; pageIndex < maxPagesToLoad; pageIndex++) {
                    int startIndex = pageIndex * 10;
                    List<String[]> bookDetailsList = GoogleBooksAPI.fetchBookDetails(keyword, flag, startIndex);

                    for (String[] details : bookDetailsList) {
                        books.add(new Book(
                                0,
                                0,
                                details[0],
                                details[1],
                                details[2],
                                details[3],
                                details[4],
                                "N/A",
                                details[5],
                                details[6]
                        ));
                    }

                    if (bookDetailsList.size() < 10) {
                        break;
                    }
                }

                return books;
            }
        };

        loadingSpinner.progressProperty().bind(searchTask.progressProperty());

        searchTask.setOnSucceeded(event -> {
            List<Book> books = searchTask.getValue();

            booksListContainer.getChildren().clear();

            for (Book book : books) {
                booksListContainer.getChildren().add(createBookItem(book));
            }

            loadingSpinner.setVisible(false);
        });

        searchTask.setOnFailed(event -> {
            loadingSpinner.setVisible(false);
            loadingSpinner.progressProperty().unbind();
            System.err.println("Failed to fetch book details: " + searchTask.getException());
        });

        new Thread(searchTask).start();
    }

    public VBox createBookItem(Book book) {
        VBox bookItem = new VBox(10);
        bookItem.getStyleClass().add("book-item-search");

        ImageView bookImageView = new ImageView();
        bookImageView.setFitWidth(200);
        bookImageView.setFitHeight(300);
        bookImageView.setPreserveRatio(true);

        String imageUrl = book.getImageUrl();
        Image bookImage = imageUrl != null
                ? new Image(imageUrl, 200, 300, true, true, true)
                : new Image(getClass().getResource("/image/img.png").toExternalForm(), 200, 300, true, true);
        bookImageView.setImage(bookImage);


        Text bookTitle = new Text(book.getTitle());
        bookTitle.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        Text bookAuthor = new Text("Author: " + book.getAuthor());
        bookAuthor.setStyle("-fx-font-size: 14; -fx-text-fill: #555555;");

        Text bookPublisher = new Text("Publisher: " + book.getPublisher());
        bookPublisher.setStyle("-fx-font-size: 14; -fx-text-fill: #888888;");

        Text bookIsbn = new Text("ISBN: " + (book.getIsbn() == null || book.getIsbn().isEmpty() ? "UNKNOWN" : book.getIsbn()));
        bookIsbn.setStyle("-fx-font-size: 14; -fx-text-fill: #444444;");

        Label bookReview = new Label("\"" + book.getReview() + "\"");
        bookReview.setStyle("-fx-font-style: italic; -fx-text-fill: #444444;");

        Button addButton = new Button("Add");
        Button viewButton = new Button("View");
        Button deleteButton = new Button("Delete");

        addButton.getStyleClass().add("add-button");
        viewButton.getStyleClass().add("view-button");
        deleteButton.getStyleClass().add("delete-button");


        boolean isBookInDB = sqlBookRepository.getBookByIsbn(book.getTitle()) != null;

        if (isBookInDB) {
            addButton.setVisible(false);
            viewButton.setVisible(true);
            deleteButton.setVisible(true);
        } else {
            addButton.setVisible(true);
            viewButton.setVisible(false);
            deleteButton.setVisible(false);
        }

        addButton.setOnAction(event -> {
            sqlBookRepository.addBook(book);
            addButton.setVisible(false);
            viewButton.setVisible(true);
            deleteButton.setVisible(true);
        });

        deleteButton.setOnAction(event -> {
            sqlBookRepository.deleteBook(book.getIsbn());
            viewButton.setVisible(false);
            deleteButton.setVisible(false);
            addButton.setVisible(true);
        });

        viewButton.setOnAction(event -> {
            NavigationManager.switchSceneWithData("/staffScene/staffBookDetail.fxml",
                    (controller, selectedBook) -> {
                        StaffBookDetailController staffBookDetailController = (StaffBookDetailController) controller;
                        staffBookDetailController.setBookDetails((Book) selectedBook);
                    },
                    book);
        });

        bookItem.getChildren().addAll(
                bookImageView,
                bookTitle,
                bookAuthor,
                bookPublisher,
                bookIsbn,
                bookReview,
                addButton,
                viewButton,
                deleteButton
        );

        bookItem.setOnMousePressed(event -> bookItem.setScaleX(0.97));
        bookItem.setOnMouseReleased(event -> bookItem.setScaleX(1.0));

        return bookItem;
    }



    public void switchSceneLibrary() {
        NavigationManager.switchScene("/staffScene/staffLib.fxml");
    }

    public void switchSceneItems() {
        NavigationManager.switchScene("/staffScene/staffBook.fxml");
    }

    public void switchSceneUser() {
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
        nameLabel.setText(currentUser.getName());
    }
}

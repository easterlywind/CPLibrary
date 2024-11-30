package com.example.cplibrary.controller;

import com.example.cplibrary.UserSession;
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

public class StaffItemController {

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
                int maxPagesToLoad = 5; // Tải trước 3 trang
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
                        break; // Kết thúc nếu không còn kết quả
                    }
                }

                return books;
            }
        };

        // Gắn kết ProgressIndicator với Task
        loadingSpinner.progressProperty().bind(searchTask.progressProperty());

        // Xử lý khi Task hoàn thành
        searchTask.setOnSucceeded(event -> {
            List<Book> books = searchTask.getValue();

            booksListContainer.getChildren().clear(); // Xóa các mục sách cũ trước khi thêm sách mới

            for (Book book : books) {
                booksListContainer.getChildren().add(createBookItem(book));
            }

            loadingSpinner.setVisible(false);
        });

        // Xử lý khi Task thất bại
        searchTask.setOnFailed(event -> {
            loadingSpinner.setVisible(false);
            loadingSpinner.progressProperty().unbind();
            System.err.println("Failed to fetch book details: " + searchTask.getException());
        });

        // Chạy Task trong luồng khác
        new Thread(searchTask).start();
    }

    public VBox createBookItem(Book book) {
        VBox bookItem = new VBox(10);
        bookItem.setStyle("-fx-padding: 10; -fx-border-color: lightgray; -fx-border-radius: 5;");

        String imageUrl = book.getImageUrl();
        Image bookImage = imageUrl != null
                ? new Image(imageUrl, 200, 300, true, true)
                : new Image(getClass().getResource("/image/img.png").toExternalForm(), 200, 300, true, true);

        Text bookISBN = new Text(book.getIsbn());
        Text bookTitle = new Text(book.getTitle());
        Text bookAuthor = new Text(book.getAuthor());
        Text bookSubject = new Text(book.getSubject());
        Text bookPublisher = new Text(book.getPublisher());
        Label bookReview = new Label(book.getReview());

        Button addButton = new Button("Add");
        Button viewButton = new Button("View");
        Button deleteButton = new Button("Delete");

        boolean isBookInDB = sqlBookRepository.getBookByIsbn(book.getIsbn()) != null;

        if (isBookInDB) {
            addButton.setVisible(false);
        } else {
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
            NavigationManager.switchSceneWithData("/staffScene/bookDetails.fxml",
                    (controller, selectedBook) -> {
                        BookController bookController = (BookController) controller;
                        bookController.setBookDetails((Book) selectedBook);
                    },
                    book);
        });

        bookItem.getChildren().addAll(
                new ImageView(bookImage),
                bookISBN,
                bookTitle,
                bookAuthor,
                bookSubject,
                bookPublisher,
                bookReview,
                addButton,
                viewButton,
                deleteButton
        );

        return bookItem;
    }

    public void switchSceneLibrary() {
        NavigationManager.switchScene("/staffScene/staffLib.fxml");
    }

    public void switchSceneItems() {
        NavigationManager.switchScene("/staffScene/staffItem.fxml");
    }

    public void switchSceneUser() {
        NavigationManager.switchScene("/staffScene/staffUsers.fxml");
    }

    public void switchSceneLogout() {
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
        nameLabel.setText(currentUser.getName());
    }
}

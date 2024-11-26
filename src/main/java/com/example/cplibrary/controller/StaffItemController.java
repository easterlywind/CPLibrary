package com.example.cplibrary.controller;

import com.example.cplibrary.infrastructure.GoogleBooksAPI;
import com.example.cplibrary.infrastructure.SQLBookRepository;
import com.example.cplibrary.infrastructure.SQLUserRepository;
import com.example.cplibrary.model.Book;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class StaffItemController {

    @FXML
    private TextField searchField;

    @FXML
    private VBox booksListContainer;

    @FXML
    private ProgressIndicator loadingSpinner;

    private final SQLBookRepository sqlBookRepository = new SQLBookRepository();
    private boolean flag = false;


    public void onSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            booksListContainer.getChildren().clear();
            return;
        }

        loadingSpinner.setVisible(true);

        // Sử dụng Task để thực thi API tìm kiếm trong luồng khác
        Task<List<Book>> searchTask = new Task<>() {
            @Override
            protected List<Book> call() {
                updateProgress(0, 1);

                for (Character c : keyword.toCharArray()) {
                    if (Character.isLetter(c)) {
                        flag = true;
                        break;
                    }
                }
                List<String[]> bookDetailsList = GoogleBooksAPI.fetchBookDetails(keyword, flag);

                // Chuyển đổi sang danh sách đối tượng Book
                List<Book> books = new ArrayList<>();
                for (String[] details : bookDetailsList) {
                    books.add(new Book(
                            0,
                            0,
                            details[0], // ISBN
                            details[1], // Title
                            details[2], // Author
                            details[3], // Subject
                            details[4], // Publisher
                            "N/A",       // Shelf Location
                            details[5],  // Review
                            details[6]
                    ));
                }

                updateProgress(1, 1);
                return books;
            }
        };

        // Gắn kết ProgressIndicator với Task
        loadingSpinner.progressProperty().bind(searchTask.progressProperty());

        // Xử lý khi Task hoàn thành
        searchTask.setOnSucceeded(event -> {
            loadingSpinner.setVisible(false);
            loadingSpinner.progressProperty().unbind();

            List<Book> books = searchTask.getValue();

            // Xóa nội dung cũ và thêm sách mới
            booksListContainer.getChildren().clear();
            for (Book book : books) {
                booksListContainer.getChildren().add(createBookItem(book));
            }
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



    public VBox createBookItem( Book book) {
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

        // Tạo các nút và đặt trạng thái ban đầu
        Button addButton = new Button("Add");
        Button viewButton = new Button("View");
        Button deleteButton = new Button("Delete");

        boolean isBookInDB = sqlBookRepository.getBookByIsbn(book.getIsbn()) != null;

        // Xử lý khi sách đã tồn tại trong CSDL
        if (isBookInDB) {
            addButton.setVisible(false);
        } else {
            viewButton.setVisible(false);
            deleteButton.setVisible(false);
        }

        // Hành động cho nút "Add"
        addButton.setOnAction(event -> {
            sqlBookRepository.addBook(book);
            addButton.setVisible(false);
            viewButton.setVisible(true);
            deleteButton.setVisible(true);
        });

        // Hành động cho nút "Delete"
        deleteButton.setOnAction(event -> {
            sqlBookRepository.deleteBook(book.getIsbn());
            viewButton.setVisible(false);
            deleteButton.setVisible(false);
            addButton.setVisible(true);
        });

        // Hành động cho nút "View"
        viewButton.setOnAction(event -> {
            // Logic để xem chi tiết sách
            NavigationManager.switchSceneWithData("/BookDetails.fxml",
                    (controller,selectedBook) -> {
                        BookController bookController = (BookController) controller;
                        bookController.setBookDetails((Book) selectedBook);
                    },
                    book);
        });

        // Thêm các thành phần vào VBox
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
            System.out.println("view book details" + book.getIsbn());
        });
        return viewButton;
    }

    private Button createAddButton(Book book) {
        Button addButton = new Button("Add");
        addButton.setOnAction(event -> {
            sqlBookRepository.addBook(book);
        });
        return addButton;
    }

    public void switchSceneLibrary(MouseEvent event) {
        NavigationManager.switchScene("/staffLib.fxml");
    }

    public void switchSceneItems(MouseEvent event) {
        NavigationManager.switchScene("/staffItem.fxml");
    }

    public void switchSceneUser(MouseEvent event) {
        NavigationManager.switchScene("/login.fxml");
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
}

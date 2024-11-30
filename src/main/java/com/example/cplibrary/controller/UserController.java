package com.example.cplibrary.controller;

import com.example.cplibrary.UserSession;
import com.example.cplibrary.application.StaffService;
import com.example.cplibrary.model.Book;
import com.example.cplibrary.model.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.concurrent.Task;

import java.util.List;
import java.util.Optional;

public class UserController {

    @FXML
    private GridPane gridPane;

    @FXML
    private Label nameLabel;

    @FXML
    private TextField searchTextField;

    private final StaffService staffService = new StaffService();
    String keyword;
    private final User currentUser = UserSession.getInstance().getCurrentUser();

    private void loadImageAsync(String imageUrl, ImageView imageView) {
        Task<Image> imageTask = new Task<>() {
            @Override
            protected Image call() throws Exception {
                return new Image(imageUrl, 200, 300, true, true);
            }
        };

        imageTask.setOnSucceeded(e -> {
            imageView.setImage(imageTask.getValue());
        });

        imageTask.setOnFailed(e -> {
            imageView.setImage(new Image(getClass().getResource("/image/img.png").toExternalForm(), 200, 300, true, true));
        });

        new Thread(imageTask).start();
    }

    public void onSearchEnter(KeyCode keyCode) {
        if (keyCode == KeyCode.ENTER) {
            initialize();
        }
    }

    @FXML
    public void initialize() {
        searchTextField.setOnKeyPressed(event -> onSearchEnter(event.getCode()));
        keyword = searchTextField.getText();
        boolean flagSearch = !keyword.isEmpty();

        System.out.println(keyword + " " + flagSearch);

        int numCols = 5;
        int numRows = 0;

        nameLabel.setText(currentUser.getName());

        // Xóa ràng buộc cũ nếu có
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();
        gridPane.getChildren().clear();

        gridPane.setVgap(10);
        gridPane.setHgap(10);

        // Thêm ColumnConstraints (5 cột)
        for (int i = 0; i < numCols; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(100.0 / numCols); // Mỗi cột chiếm 1/5 chiều rộng
            gridPane.getColumnConstraints().add(col);
        }

        List<Book> books;

        if (!flagSearch) {
            books = staffService.getAllBooks();
        } else {
            books = staffService.searchBook(keyword);
        }

        // Tính toán số hàng cần thiết
        numRows = (int) Math.ceil(books.size() / (double) numCols);

        // Thêm RowConstraints (số hàng tính toán từ books.size)
        for (int i = 0; i < numRows; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100.0 / numRows); // Mỗi hàng chiếm tỷ lệ chiều cao
            gridPane.getRowConstraints().add(row);
        }

        // Thêm sách vào GridPane
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);

            String imageUrl = book.getImageUrl();
            ImageView imageView = new ImageView();
            loadImageAsync(imageUrl, imageView);

            // Tạo Label cho tên sách
            Label titleLabel = new Label(book.getTitle());
            titleLabel.setAlignment(Pos.CENTER);

            // Tạo VBox để chứa ImageView và Label
            VBox vBox = new VBox(5);  // Khoảng cách giữa Image và Label là 5px
            vBox.setAlignment(Pos.CENTER);
            vBox.getChildren().addAll(imageView, titleLabel);

            imageView.setOnMouseClicked(mouseEvent -> {
                NavigationManager.switchSceneWithData("/bookDetails.fxml",
                        (controller,selectedBook) -> {
                            BookController bookController = (BookController) controller;
                            bookController.setBookDetails((Book) selectedBook);
                        },
                        book
                );
            });


            imageView.setOnMouseEntered(event -> {
                imageView.setScaleX(1.1);
                imageView.setScaleY(1.1);
                imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10, 0, 0, 0);");
            });

            imageView.setOnMouseExited(event -> {
                imageView.setScaleX(1.0);
                imageView.setScaleY(1.0);
                imageView.setStyle("");
            });

            // Tính toán vị trí hàng và cột
            int row = i / numCols; // Hàng
            int col = i % numCols; // Cột

            // Thêm VBox vào GridPane
            gridPane.add(vBox, col, row);
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
}

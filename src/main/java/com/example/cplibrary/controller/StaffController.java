package com.example.cplibrary.controller;

import com.example.cplibrary.UserSession;
import com.example.cplibrary.application.StaffService;
import com.example.cplibrary.infrastructure.GoogleBooksAPI;
import com.example.cplibrary.model.Book;
import com.example.cplibrary.controller.NavigationManager;
import com.example.cplibrary.model.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.concurrent.Task;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class StaffController {

    @FXML
    private GridPane gridPane;

    @FXML
    private Label nameLabel;

    private final StaffService staffService = new StaffService();

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

    @FXML
    public void initialize() {
        // Đặt số cột và hàng cho GridPane
        int numCols = 5; // 5 cột
        int numRows = 0; // Sẽ tính toán số hàng sau

        nameLabel.setText(currentUser.getName());

        // Xóa ràng buộc cũ nếu có
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();

        // Thêm khoảng cách giữa các hàng và cột
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        // Thêm ColumnConstraints (5 cột)
        for (int i = 0; i < numCols; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(100.0 / numCols); // Mỗi cột chiếm 1/5 chiều rộng
            gridPane.getColumnConstraints().add(col);
        }

        // Lấy danh sách tất cả các sách từ cơ sở dữ liệu thông qua StaffService
        List<Book> books = staffService.getAllBooks(); // Lấy tất cả sách

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
            vBox.setAlignment(Pos.CENTER); // Căn giữa ảnh và tên sách
            vBox.getChildren().addAll(imageView, titleLabel);

            imageView.setOnMouseClicked(mouseEvent -> {
                NavigationManager.switchSceneWithData("/BookDetails.fxml",
                        (controller,selectedBook) -> {
                            BookController bookController = (BookController) controller;
                            bookController.setBookDetails((Book) selectedBook);
                        },
                        book
                );
            });


            imageView.setOnMouseEntered(event -> {
                imageView.setScaleX(1.1); // Tăng kích thước theo chiều ngang
                imageView.setScaleY(1.1); // Tăng kích thước theo chiều dọc
                imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10, 0, 0, 0);");
            });

            imageView.setOnMouseExited(event -> {
                imageView.setScaleX(1.0); // Trở lại kích thước ban đầu
                imageView.setScaleY(1.0); // Trở lại kích thước ban đầu
                imageView.setStyle(""); // Xoá hiệu ứng
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

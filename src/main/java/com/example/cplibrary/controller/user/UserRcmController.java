package com.example.cplibrary.controller.user;

import com.example.cplibrary.UserSession;
import com.example.cplibrary.application.StaffService;
import com.example.cplibrary.controller.common.AlertManager;
import com.example.cplibrary.controller.common.NavigationManager;
import com.example.cplibrary.infrastructure.ApiClient;
import com.example.cplibrary.model.Book;
import com.example.cplibrary.model.User;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

import java.util.List;

public class UserRcmController {

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

        imageTask.setOnSucceeded(e -> imageView.setImage(imageTask.getValue()));
        imageTask.setOnFailed(e -> imageView.setImage(new Image(getClass().getResource("/image/img.png").toExternalForm(), 200, 300, true, true)));

        new Thread(imageTask).start();
    }

    @FXML
    public void initialize() {
        nameLabel.setText(currentUser.getName());
        loadRecommendedBooks();
    }

    private void loadRecommendedBooks() {
        Task<List<Book>> task = new Task<>() {
            @Override
            protected List<Book> call() {
                return ApiClient.getRecommendedBooks(currentUser.getUserId());
            }
        };

        task.setOnSucceeded(event -> {
            List<Book> books = task.getValue();
            displayBooks(books);
        });

        task.setOnFailed(event -> AlertManager.showInfoAlert("Error", "Failed to load recommendations.", ""));

        new Thread(task).start();
    }

    private void displayBooks(List<Book> books) {
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();
        gridPane.getChildren().clear();

        int numCols = 3;
        int numRows = (int) Math.ceil(books.size() / (double) numCols);

        gridPane.setVgap(10);
        gridPane.setHgap(10);

        for (int i = 0; i < numCols; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(100.0 / numCols);
            gridPane.getColumnConstraints().add(col);
        }

        for (int i = 0; i < numRows; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100.0 / numRows);
            gridPane.getRowConstraints().add(row);
        }

        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);

            String imageUrl = book.getImageUrl();
            ImageView imageView = new ImageView();
            loadImageAsync(imageUrl, imageView);

            Label titleLabel = new Label(book.getTitle());
            titleLabel.setAlignment(Pos.CENTER);

            VBox vBox = new VBox(5);
            vBox.setAlignment(Pos.CENTER);
            vBox.getChildren().addAll(imageView, titleLabel);

            imageView.setOnMouseClicked(mouseEvent -> {
                NavigationManager.switchScene("/userScene/userBookDetail.fxml",
                        (controller, selectedBook) -> {
                            UserBookDetailController userBookDetailController = (UserBookDetailController) controller;
                            userBookDetailController.setBookDetails((Book) selectedBook);
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

            int row = i / numCols;
            int col = i % numCols;

            gridPane.add(vBox, col, row);
        }
    }

    public void switchSceneLibrary(MouseEvent mouseEvent) {
        NavigationManager.switchScene("/userScene/userLib.fxml");
    }

    public void switchSceneLoans(MouseEvent mouseEvent) {
        NavigationManager.switchScene("/userScene/userLoans.fxml");
    }

    public void switchSceneReservation(MouseEvent mouseEvent) {
        NavigationManager.switchScene("/userScene/userReservation.fxml");
    }

    public void switchSceneRcm(MouseEvent mouseEvent) {
        NavigationManager.switchScene("/userScene/userRcm.fxml");
    }

    public void switchSceneLogout() {
        boolean confirmed = AlertManager.showConfirmationAlert("CONFIRMATION", "Are you sure you want to logout?", "All unsaved changes will be lost.");
        if (confirmed) {
            NavigationManager.switchScene("/commonScene/login.fxml");
        }
    }
}

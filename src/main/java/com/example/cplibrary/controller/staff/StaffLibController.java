package com.example.cplibrary.controller.staff;

import com.example.cplibrary.UserSession;
import com.example.cplibrary.application.StaffService;
import com.example.cplibrary.controller.common.AlertManager;
import com.example.cplibrary.controller.common.NavigationManager;
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

public class StaffLibController {

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

        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();
        gridPane.getChildren().clear();

        gridPane.setVgap(10);
        gridPane.setHgap(10);

        for (int i = 0; i < numCols; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(100.0 / numCols);
            gridPane.getColumnConstraints().add(col);
        }

        List<Book> books;

        if (!flagSearch) {
            books = staffService.getAllBooks();
        } else {
            books = staffService.searchBook(keyword);
        }

        numRows = (int) Math.ceil(books.size() / (double) numCols);

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
                NavigationManager.switchScene("/staffScene/staffBookDetail.fxml",
                        (controller,selectedBook) -> {
                            StaffBookDetailController staffBookDetailController = (StaffBookDetailController) controller;
                            staffBookDetailController.setBookDetails((Book) selectedBook);
//                            System.out.println("Received book title: " + ((Book) selectedBook).getTitle());

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
}

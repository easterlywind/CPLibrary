package com.example.cplibrary.controller;

import com.example.cplibrary.application.StaffService;
import com.example.cplibrary.infrastructure.GoogleBooksAPI;
import com.example.cplibrary.model.Book;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StaffController {

    @FXML
    private GridPane gridPane;
    @FXML
    private Label nameLabel;

    private final StaffService staffService = new StaffService(); // Tạo đối tượng StaffService

    @FXML
    public void initialize() {
        // Đặt số cột và hàng cho GridPane
        int numCols = 5; // 5 cột
        int numRows = 0; // Sẽ tính toán số hàng sau

        nameLabel.setText("Admin");

        // Xóa ràng buộc cũ nếu có
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();

        // Thêm khoảng cách giữa các hàng và cột
        gridPane.setVgap(10); // 10px khoảng cách giữa các hàng
        gridPane.setHgap(10); // 10px khoảng cách giữa các cột

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

        // Danh sách URL ảnh từ Google Books API
        List<String> isbns = new ArrayList<>();
        for (Book book : books) {
            isbns.add(book.getIsbn());
        }
        List<String> imageUrls = GoogleBooksAPI.fetchBookImageURLs(isbns);

        // Thêm sách vào GridPane
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);

            // Lấy ảnh từ Google API
            String imageUrl = imageUrls.get(i);
            Image image = imageUrl != null && !imageUrl.isEmpty()
                    ? new Image(imageUrl, 200, 300, true, true)
                    : new Image(getClass().getResource("/image/img.png").toExternalForm(), 200, 300, true, true);

            ImageView imageView = new ImageView(image);

            // Tạo Label cho tên sách
            Label titleLabel = new Label(book.getTitle());
            titleLabel.setAlignment(Pos.CENTER);

            // Tạo VBox để chứa ImageView và Label
            VBox vBox = new VBox(5);  // Khoảng cách giữa Image và Label là 5px
            vBox.setAlignment(Pos.CENTER); // Căn giữa ảnh và tên sách
            vBox.getChildren().addAll(imageView, titleLabel);

            imageView.setOnMouseClicked(mouseEvent -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/BookDetails.fxml"));
                    Parent root = loader.load();

                    BookController controller = loader.getController();
                    controller.setBookDetails(book);

                    Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
}

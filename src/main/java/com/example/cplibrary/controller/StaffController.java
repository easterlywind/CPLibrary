package com.example.cplibrary.controller;

import com.example.cplibrary.infrastructure.GoogleBooksAPI;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

import java.util.List;

public class StaffController {

    @FXML
    private GridPane gridPane;
    @FXML
    private Label nameLabel;

    // Danh sách mã ISBN để tìm kiếm
    private final List<String> isbnList = List.of(
            "9780201485677", "9780135166307", "9780131103627", "9780307905834", "9780439139601",
            "9780375821977", "9780747532743", "9780525561021", "9780141187761", "9780140247745",
            "9780679783268", "9780525564688", "9781449462711", "9780062457761", "9781484723055",
            "9780307588354", "9780590353427", "9780151072552", "9781400078776", "9780061122415",
            "9780452295260", "9780316769488", "9780743273565", "9780345391803", "9780316195685",
            "9780393350420", "9780380730404", "9781250317793", "9781451673319", "9780060850524",
            "9780374528379", "9780679760802", "9780151010263", "9780060850523", "9780679744758",
            "9780525538085", "9780307464913", "9780143127781", "9781612192132", "9780316666340",
            "9780553382563", "9780316131309", "9781451648547", "9780452295260", "9780395978039",
            "9780061240081", "9780441013593", "9781439153910", "9781439147650", "9780062641541"
    );


    @FXML
    public void initialize() {
        // Đặt số cột và hàng cho GridPane
        int numCols = 5; // 5 cột
        int numRows = 21; // 10 hàng

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

        // Thêm RowConstraints (10 hàng)
        for (int i = 0; i < numRows; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100.0 / numRows); // Mỗi hàng chiếm 1/10 chiều cao
            gridPane.getRowConstraints().add(row);
        }

        // Danh sách URL ảnh từ API
        List<String> imageUrls = GoogleBooksAPI.fetchBookImageURLs(isbnList);

        // Danh sách Title từ API
        List<String> titles = GoogleBooksAPI.fetchBookTitles(isbnList);

        // Thêm ảnh vào GridPane
        for (int i = 0; i < imageUrls.size(); i++) {
            // Tạo một ImageView từ URL
            String imageUrl = imageUrls.get(i);
            String title = titles.get(i);
            Image image = imageUrl != null && !imageUrl.isEmpty()
                    ? new Image(imageUrl, 200, 300, true, true)
                    : new Image(getClass().getResource("/image/img.png").toExternalForm(), 200, 300, true, true);

            ImageView imageView = new ImageView(image);

            // Tạo Label cho tên sách
            Label titleLabel = new Label(title);
            titleLabel.setAlignment(Pos.CENTER);

            // Tạo VBox để chứa ImageView và Label
            VBox vBox = new VBox(5);  // Khoảng cách giữa Image và Label là 5px
            vBox.setAlignment(Pos.CENTER); // Căn giữa ảnh và tên sách
            vBox.getChildren().addAll(imageView, titleLabel);

            imageView.setOnMouseClicked(mouseEvent -> {
                System.out.println("Clicked on book with ISBN: " + "hello");
                System.out.println("Title: " + title);
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

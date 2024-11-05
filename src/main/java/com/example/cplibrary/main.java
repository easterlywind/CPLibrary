package com.example.cplibrary;//package com.example.cplibrary;
//
//import com.example.cplibrary.controller.LoginView;
//import com.example.cplibrary.model.Member;
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//
//
//public class main extends Application {
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
//        Scene scene = new Scene(root,600,400);
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//}

import com.example.cplibrary.infrastructure.GoogleBooksAPI;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.io.IOException;

public class main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Library Management System");

        VBox root = new VBox();
        Label instructionLabel = new Label("Enter book title to search in Google Books API:");
        TextField searchField = new TextField();
        Button searchButton = new Button("Search");
        Label resultLabel = new Label();

        // Set button action
        searchButton.setOnAction(e -> {
            String searchQuery = searchField.getText();
            try {
                // Call the GoogleBooksAPI to search for books
                GoogleBooksAPI.searchBookByTitle(searchQuery);
            } catch (IOException ex) {
                resultLabel.setText("Error fetching data from API.");
                ex.printStackTrace();
            }
        });

        root.getChildren().addAll(instructionLabel, searchField, searchButton, resultLabel);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


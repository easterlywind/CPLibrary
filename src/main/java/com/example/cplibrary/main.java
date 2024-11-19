package com.example.cplibrary;

import com.example.cplibrary.application.StaffService;
import com.example.cplibrary.model.Book;
import com.example.cplibrary.model.Member;
import com.example.cplibrary.model.User;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class main extends Application {

    @Override
    public void start(Stage primaryStage) {
//        try {
//            // Load the FXML file
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
//            Parent root = loader.load();
//
//            primaryStage.setScene(new Scene(root, 600, 400));
//            primaryStage.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        StaffService staffService = new StaffService();
        Book book = new Book(001, "12345", "harry potter", "brandy love", "cucocu"
                , "12/2/122", "A23", "sieu hay pro vip ");
        staffService.deleteBook("12345");
    }

    public static void main(String[] args) {
        launch(args);

    }
}
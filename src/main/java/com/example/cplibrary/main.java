package com.example.cplibrary;

import com.example.cplibrary.application.StaffService;
import com.example.cplibrary.model.Member;
import com.example.cplibrary.model.User;
import javafx.application.Application;
import javafx.stage.Stage;

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
    Member member = new Member(2, "nth", "nth", "110", "nth", "active");
    Member member1 = new Member(4, "hello", "gekki", "210", "chiahi", "banned");
    staffService.addUser(member);
    staffService.addUser(member1);
    Member res = new Member();
    res = staffService.getUserById(4);
        System.out.println(res.getName());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
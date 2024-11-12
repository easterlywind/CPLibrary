package com.example.cplibrary;

import com.example.cplibrary.application.AdminService;
import com.example.cplibrary.model.Admin;
import com.example.cplibrary.model.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

        User user = new User("001", "admin", "admin","admin","0123456789", "true");
        AdminService adminService = new AdminService();
        adminService.addMember(user);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
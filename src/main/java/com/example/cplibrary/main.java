package com.example.cplibrary;

import com.example.cplibrary.application.StaffService;
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

        User user = new User("003", "admin", "admin","admin","0123456789", "true");
        StaffService adminService = new StaffService();
        adminService.addMember(user);
        User meo = new User();
        meo = adminService.viewMemberDetails("002");
        System.out.println(meo.getEmail());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
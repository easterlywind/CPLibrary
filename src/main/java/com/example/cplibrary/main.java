package com.example.cplibrary;

import com.example.cplibrary.controller.LoginView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/adminView.fxml"));
        Scene scene = new Scene(root,1080,720);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

package com.example.cplibrary;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/testapi.fxml"));
            Parent root = loader.load();

            // Set the title and scene
            primaryStage.setTitle("Book Image Viewer");
            primaryStage.setScene(new Scene(root, 600, 500)); // Window size: 600x500
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
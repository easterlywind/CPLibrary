package com.example.cplibrary;

import com.example.cplibrary.infrastructure.SQLReviewRepository;
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/staffLib.fxml"));
            Parent root = loader.load();

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public static void main(String[] args) {
        launch(args);

    }
}



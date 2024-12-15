package com.example.cplibrary.controller.common;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NavigationManager {
    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void switchScene(String fxmlFilePath) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationManager.class.getResource(fxmlFilePath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(NavigationManager.class.getResource("/staffScene/styleStaff.css").toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Không thể tải file FXML: " + fxmlFilePath);
        }
    }

    public static <T> void switchScene(String fxmlFilePath, DataInitializer initializer, Object data) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationManager.class.getResource(fxmlFilePath));
            Parent root = loader.load();

            Object controller = loader.getController();
            initializer.initialize(controller, data);

            primaryStage.setScene(new Scene(root));
            primaryStage.centerOnScreen();
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FunctionalInterface
    public interface DataInitializer {
        void initialize(Object controller, Object data);
    }

}

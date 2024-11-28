package com.example.cplibrary;

import com.example.cplibrary.application.StaffService;
import com.example.cplibrary.controller.NavigationManager;
import com.example.cplibrary.infrastructure.SQLReviewRepository;
import com.example.cplibrary.model.Book;
import com.example.cplibrary.model.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.List;

public class main extends Application {

    @Override
    public void start(Stage primaryStage) {
//
//
        NavigationManager.setPrimaryStage(primaryStage);

        UserSession.getInstance().setCurrentUser(new User());

        NavigationManager.switchScene("/staffLib.fxml");



    }


    public static void main(String[] args) {
        launch(args);

    }
}



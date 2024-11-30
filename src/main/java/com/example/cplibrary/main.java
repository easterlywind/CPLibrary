package com.example.cplibrary;

import com.example.cplibrary.controller.common.NavigationManager;
import com.example.cplibrary.model.User;
import javafx.application.Application;
import javafx.stage.Stage;

public class main extends Application {

    @Override
    public void start(Stage primaryStage) {
//
//
        NavigationManager.setPrimaryStage(primaryStage);

        UserSession.getInstance().setCurrentUser(new User());

        NavigationManager.switchScene("/commonScene/login.fxml");



    }


    public static void main(String[] args) {
        launch(args);

    }
}



package com.example.cplibrary.controller;

import com.example.cplibrary.UserSession;
import com.example.cplibrary.model.User;
import com.example.cplibrary.infrastructure.SQLUserRepository;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.awt.event.ActionEvent;
import java.util.Optional;

public class UserDetailsController {
    @FXML
    private Label nameLabel;

    private final User currentUser = UserSession.getInstance().getCurrentUser();

    public void setUserDetails(User user) {

    }

    public void backButtonOnAction() {
        NavigationManager.switchScene("/staffUsers.fxml");
    }

    public void switchSceneLibrary(MouseEvent event) {
        NavigationManager.switchScene("/staffLib.fxml");
    }

    public void switchSceneItems(MouseEvent event) {
        NavigationManager.switchScene("/staffItem.fxml");
    }

    public void switchSceneUser(MouseEvent event) {
        NavigationManager.switchScene("/staffUsers.fxml");
    }

    public void switchSceneLogout(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.setContentText("All unsaved changes will be lost.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Platform.exit();
        }
    }

    @FXML
    public void initialize() {
        nameLabel.setText(currentUser.getName());
    }
}
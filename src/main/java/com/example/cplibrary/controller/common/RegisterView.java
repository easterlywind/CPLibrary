package com.example.cplibrary.controller.common;

import com.example.cplibrary.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterView {

    @FXML
    private TextField emailTextField, usernameTextField;

    @FXML
    private PasswordField passwordTextField, confirmPasswordTextField;

    @FXML
    private Label registerMessageLabel;

    public void registerButtonOnAction(ActionEvent event) {
        String email = emailTextField.getText().trim();
        String name = usernameTextField.getText().trim();
        String password = passwordTextField.getText();
        String confirmPassword = confirmPasswordTextField.getText();

        if (email.isEmpty() || name.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            registerMessageLabel.setText("Please fill in all fields");
            return;
        }

        if (!password.equals(confirmPassword)) {
            registerMessageLabel.setText("Passwords do not match");
            return;
        }

        String insertUserSQL = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";

        try (Connection connect = new DatabaseConnection().getConnection();
             PreparedStatement stmt = connect.prepareStatement(insertUserSQL)) {

            stmt.setString(1, email);
            stmt.setString(2, name);
            stmt.setString(3, password);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                registerMessageLabel.setText("Register successful");
            } else {
                registerMessageLabel.setText("Register failed");
            }

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // Duplicate entry
                registerMessageLabel.setText("Email or username already exists");
            } else {
                registerMessageLabel.setText("Database error occurred");
            }
            e.printStackTrace();
        }
    }

    public void backToLoginButtonOnAction(ActionEvent event) {
        NavigationManager.switchScene("/commonScene/login.fxml");
    }
}

package com.example.cplibrary.controller.common;

import com.example.cplibrary.DatabaseConnection;
import com.example.cplibrary.UserSession;
import com.example.cplibrary.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.*;


public class LoginView {

    @FXML
    public Label loginMessageLabel;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordTextField;

    public void loginButtonOnAction(ActionEvent event) {
        //loginMessageLabel.setText("You try to login");

        if(emailTextField.getText().isEmpty() || passwordTextField.getText().isEmpty()) {
            loginMessageLabel.setText("Please enter your username and password");
        } else {
            validateLogin(event);
        }

    }

    public void registerButtonOnAction(ActionEvent event)  {

        NavigationManager.switchScene("/commonScene/register.fxml");
    }


    public void validateLogin(ActionEvent event) {
        String email = emailTextField.getText();
        String password = passwordTextField.getText();

        String verifyLoginSQL = "SELECT user_id, name, role FROM Users WHERE email = ? AND password = ?";

        try (Connection connectDB = new DatabaseConnection().getConnection();
             PreparedStatement stmt = connectDB.prepareStatement(verifyLoginSQL)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            try (ResultSet queryResult = stmt.executeQuery()) {
                if (queryResult.next()) {
                    String role = queryResult.getString("role");
                    int userId = queryResult.getInt("user_id");
                    String name = queryResult.getString("name");
                    String status = "active";

                    User userInfo = new User(userId, name, email, password, status);

                    if ("staff".equalsIgnoreCase(role)) {
                        UserSession.getInstance().setCurrentUser(userInfo);
                        NavigationManager.switchScene("/staffScene/staffLib.fxml");
                    } else {
                        loginMessageLabel.setText("Access denied: not a staff account.");
                    }
                } else {
                    loginMessageLabel.setText("Invalid email or password.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            loginMessageLabel.setText("Database error occurred.");
        }
    }

}

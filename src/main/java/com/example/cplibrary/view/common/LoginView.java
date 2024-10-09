package com.example.cplibrary.view.common;

import com.example.cplibrary.util.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class LoginView {
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;
    @FXML
    public Label loginMessageLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;

    public void loginButtonOnAction(ActionEvent event) {
        //loginMessageLabel.setText("You try to login");

        if(usernameTextField.getText().isEmpty() || passwordTextField.getText().isEmpty()) {
            loginMessageLabel.setText("Please enter your username and password");
        } else {
            //loginMessageLabel.setText("Login successful");
            validateLogin();
        }
    }

    public void validateLogin() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String verifyLogin = "select count(1) from login where userName = '" + usernameTextField.getText() + "' AND password = '" + passwordTextField.getText() + "'";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while (queryResult.next()) {
                if(queryResult.getInt(1) == 0) {
                    loginMessageLabel.setText("Invalid username or password");
                } else {
                    loginMessageLabel.setText("Welcome " + usernameTextField.getText());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.example.cplibrary.controller;

import com.example.cplibrary.util.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class RegisterView {
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private PasswordField confirmPasswordTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private Label registerMessageLabel;


    public void registerButtonOnAction(ActionEvent event)  {
        if (emailTextField.getText().isEmpty() || passwordTextField.getText().isEmpty()
                || confirmPasswordTextField.getText().isEmpty() || usernameTextField.getText().isEmpty()) {
            registerMessageLabel.setText("Please fill all the fields");
        } else {
            try {
                validateRegister();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void backToLoginButtonOnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void validateRegister() throws SQLException {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connect = connectNow.getConnection();
        if (connect == null) {
            registerMessageLabel.setText("Please connect to the database");
            return;
        }
        String verifyRegister = "insert into login (userName, password) values ('" + usernameTextField.getText() + "','" + passwordTextField.getText() +  "');";

        Statement statement = connect.createStatement();
        int queryResult = statement.executeUpdate(verifyRegister);

        if (queryResult > 0) {
            registerMessageLabel.setText("Register successful");
        } else {
            registerMessageLabel.setText("Register failed");
        }
    }
}


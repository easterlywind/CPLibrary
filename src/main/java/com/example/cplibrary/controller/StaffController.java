package com.example.cplibrary.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

public class StaffController implements Initializable{
    @FXML
    private ImageView adminImage;

    @FXML
    private Button logOutButton;

    @FXML
    private Label staffNameText;

    @FXML
    private Button bookButton;

    @FXML
    private Button userButton;

    @FXML
    private Button logOutButton1;

    @FXML
    private AnchorPane userPane;

    @FXML
    private Button userSearchButton;

    @FXML
    private TextField userSearchBar;

    @FXML
    private TableView<?> userTable;

    @FXML
    private TableColumn<?, ?> userIDColumn;

    @FXML
    private TableColumn<?, ?> userNameColumn;

    @FXML
    private TableColumn<?, ?> userEmailColumn;

    @FXML
    private TableColumn<?, ?> userPasswordColumn;

    @FXML
    private TextField userIDText;

    @FXML
    private TextField userNameText;

    @FXML
    private TextField userEmailText;

    @FXML
    private Button userDeleteButton;

    @FXML
    private Button userAddButton;

    @FXML
    private Button userUpdateBtton;

    @FXML
    private TextField userIDText1;

    @FXML
    private Button userDeleteButton1;

    @FXML
    private AnchorPane bookPane;

    @FXML
    private Button bookSearchButton;

    @FXML
    private TextField bookSearchBar;

    @FXML
    private TableView<?> bookTable;

    @FXML
    private TableColumn<?, ?> isbnColumn;

    @FXML
    private TableColumn<?, ?> titleColumn;

    @FXML
    private TableColumn<?, ?> authorColumn;

    @FXML
    private TableColumn<?, ?> publishDateColumn;

    @FXML
    private TextField isbnText;

    @FXML
    private TextField titleText;

    @FXML
    private TextField authorText;

    @FXML
    private Button bookDeleteButton;

    @FXML
    private Button bookAddButton;

    @FXML
    private Button bookUpdateBotton;

    @FXML
    private TextField publishDateText;

    @FXML
    private Button bookClearButton;

    public void setAdminNameText() {

    }

    public void searchUser() {

    }

    public void displayUserTable() {

    }

    public void addUser() {

    }

    public void deleteUser() {

    }

    public void updateUser() {

    }

    public void searchBook() {

    }

    public void displayBookTable() {

    }

    public void addBook() {

    }

    public void deleteBook() {

    }

    public void updateBook() {

    }

    public void clearUser() {

    }

    public void clearBook() {

    }

    public void switchForm(ActionEvent event) throws IOException {
        if(event.getSource() == bookButton) {
            bookPane.setVisible(true);
            userPane.setVisible(false);
        }

        if(event.getSource() == userButton) {
            bookPane.setVisible(false);
            userPane.setVisible(true);
        }
    }

    public void switchToUser(ActionEvent event) throws IOException {
        Alert alert;
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information meassage");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure want to switch to user UI?");

        ButtonType buttonY = new ButtonType("Yes");
        ButtonType buttonN = new ButtonType("No");

        alert.getButtonTypes().setAll(buttonN, buttonY);

        Optional<ButtonType> result = alert.showAndWait();


        if (result.isPresent() && result.get() == buttonY) {
            Parent root = FXMLLoader.load(getClass().getResource("/userView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    public void logOut(ActionEvent event) throws IOException {
        Alert alert;
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information meassage");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure want to log out?");

        ButtonType buttonY = new ButtonType("Yes");
        ButtonType buttonN = new ButtonType("No");

        alert.getButtonTypes().setAll(buttonN, buttonY);

        Optional<ButtonType> result = alert.showAndWait();


        if (result.isPresent() && result.get() == buttonY) {
            Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }
}


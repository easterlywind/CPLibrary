package com.example.cplibrary.controller.staff;

import com.example.cplibrary.UserSession;
import com.example.cplibrary.application.BookLoansService;
import com.example.cplibrary.application.BookReservationService;
import com.example.cplibrary.application.StaffService;
import com.example.cplibrary.controller.common.AlertManager;
import com.example.cplibrary.controller.common.NavigationManager;
import com.example.cplibrary.model.User;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;
import java.util.Optional;

public class StaffUserDetailController {
    @FXML
    private Label nameLabel;

    @FXML
    private Label nameInput, emailInput, passwordInput, statusInput;

    @FXML
    private TextField nameField, emailField, passwordField, statusField;

    @FXML
    private Button editButton, saveButton;

    @FXML
    private TableView<Object[]> tableLoansView, tableReservationView;

    @FXML
    private TableColumn<Object[], String> colNameInLoans, colNameInReservation;

    @FXML
    private TableColumn<Object[], LocalDate> colBorrowDateInLoans, colDueDateInLoans,colReservationDateInReservation;

    private final User currentUser = UserSession.getInstance().getCurrentUser();
    private int currenUserId = 0;
    private BookLoansService bookLoansService = new BookLoansService();
    private BookReservationService bookReservationService = new BookReservationService();
    private StaffService staffService = new StaffService();

    public void setUserDetails(User user) {
        if (user != null) {
            this.currenUserId = user.getUserId();
//            System.out.println("Before ememe" + currenUserId);
            nameInput.setText(user.getName());
            emailInput.setText(user.getEmail());
            passwordInput.setText(user.getPassword());
            statusInput.setText(user.getStatus());

            nameField.setVisible(false);
            emailField.setVisible(false);
            passwordField.setVisible(false);
            statusField.setVisible(false);

            nameInput.setVisible(true);
            emailInput.setVisible(true);
            passwordInput.setVisible(true);
            statusInput.setVisible(true);

            saveButton.setVisible(false);
            editButton.setVisible(true);

            loadData();
        } else {
            System.out.println("No user data provided");
        }
    }

    public void editButtonOnAction() {
        nameField.setText(nameInput.getText());
        emailField.setText(emailInput.getText());
        passwordField.setText(passwordInput.getText());
        statusField.setText(statusInput.getText());

        nameInput.setVisible(false);
        emailInput.setVisible(false);
        passwordInput.setVisible(false);
        statusInput.setVisible(false);

        nameField.setVisible(true);
        emailField.setVisible(true);
        passwordField.setVisible(true);
        statusField.setVisible(true);

        // Hiển thị nút Save
        saveButton.setVisible(true);
        editButton.setVisible(false);
    }

    public void saveButtonOnAction() {
        String updatedName = nameField.getText();
        String updatedEmail = emailField.getText();
        String updatedPassword = passwordField.getText();
        String updatedStatus = statusField.getText();

        boolean isUpdated = staffService.updateUser(new User(currenUserId, updatedName, updatedEmail, updatedPassword, updatedStatus));

        if (isUpdated) {
            nameInput.setText(updatedName);
            emailInput.setText(updatedEmail);
            passwordInput.setText(updatedPassword);
            statusInput.setText(updatedStatus);

            nameInput.setVisible(true);
            emailInput.setVisible(true);
            passwordInput.setVisible(true);
            statusInput.setVisible(true);

            nameField.setVisible(false);
            emailField.setVisible(false);
            passwordField.setVisible(false);
            statusField.setVisible(false);

            saveButton.setVisible(false);
            editButton.setVisible(true);
            AlertManager.showInfoAlert(
                    "Update Successful",
                    "User Details Updated",
                    "The details for user '" + updatedName + "' have been successfully updated."
            );
        } else {
            System.out.println("Failed to update user details");
        }
    }

    public void backButtonOnAction() {
        NavigationManager.switchScene("/staffScene/staffUser.fxml");
    }

    public void switchSceneLibrary(MouseEvent event) {
        NavigationManager.switchScene("/staffScene/staffLib.fxml");
    }

    public void switchSceneItems(MouseEvent event) {
        NavigationManager.switchScene("/staffScene/staffBook.fxml");
    }

    public void switchSceneUser(MouseEvent event) {
        NavigationManager.switchScene("/staffScene/staffUser.fxml");
    }

    public void switchSceneLogout() {
        boolean confirmed = AlertManager.showConfirmationAlert("CONFIRMATION", "Are you sure you want to logout?" ,"All unsaved changes will be lost.");
        if (confirmed) {
            NavigationManager.switchScene("/commonScene/login.fxml");
        }
    }

    public void loadData() {
        nameLabel.setText(currentUser.getName());
//        System.out.println("coook" + currenUserId);

        colNameInLoans.setCellValueFactory(data -> {
            Object[] row = data.getValue();
            return new ReadOnlyStringWrapper((String) row[0]);
        });
        colBorrowDateInLoans.setCellValueFactory(data -> {
            Object[] row = data.getValue();
            return new ReadOnlyObjectWrapper<>((LocalDate) row[1]);
        });
        colDueDateInLoans.setCellValueFactory(data -> {
            Object[] row = data.getValue();
            return new ReadOnlyObjectWrapper<>((LocalDate) row[2]);
        });

        tableLoansView.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Object[] row, boolean empty) {
                super.updateItem(row, empty);
                if (row == null || empty) {
                    setStyle("");
                } else {
                    LocalDate dueDate = (LocalDate) row[2];
                    if (dueDate.isBefore(LocalDate.now())) {
                        setStyle("-fx-background-color: lightcoral;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        colNameInReservation.setCellValueFactory(data -> {
            Object[] row = data.getValue();
            return new ReadOnlyStringWrapper((String) row[0]);
        });

        colReservationDateInReservation.setCellValueFactory(data -> {
            Object[] row = data.getValue();
            return new ReadOnlyObjectWrapper<>((LocalDate) row[1]);
        });

        ObservableList<Object[]> loanData = bookLoansService.fetchLoanData(currenUserId);
        tableLoansView.setItems(loanData);

        ObservableList<Object[]> reservationDataData = bookReservationService.fetchReservationData(currenUserId);
        tableReservationView.setItems(reservationDataData);
    }
}

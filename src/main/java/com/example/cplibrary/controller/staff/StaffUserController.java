package com.example.cplibrary.controller.staff;

import com.example.cplibrary.UserSession;
import com.example.cplibrary.controller.common.NavigationManager;
import com.example.cplibrary.model.User;
import com.example.cplibrary.infrastructure.SQLUserRepository;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.util.Optional;

public class StaffUserController {

    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, Integer> colId;

    @FXML
    private TableColumn<User, String> colName;

    @FXML
    private TableColumn<User, String> colEmail;

    @FXML
    private TableColumn<User, String> colPassword;

    @FXML
    private TableColumn<User, String> colStatus;

    @FXML
    private TableColumn<User, Void> colAction;

    @FXML
    private Label nameLabel;

    private SQLUserRepository userRepository;

    private ObservableList<User> userList;

    private final User currentUser = UserSession.getInstance().getCurrentUser();

    public void initialize() {
        userRepository = new SQLUserRepository();
        nameLabel.setText(currentUser.getName());
        // Load data from database
        loadData();

        // Map table columns to User properties
        colId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Add action buttons (Edit/Delete)
        addActionButtons();
    }

    private void loadData() {
        // Fetch data from database and wrap in ObservableList
        userList = FXCollections.observableArrayList(userRepository.getAllUsers());
        userList.forEach(user -> {
            System.out.println(user.getUserId() + " "  + user.getName() + " " + user.getEmail() + " " + user.getPassword() + " " + user.getStatus());
        });
        userTable.setItems(userList);
    }

    private void addActionButtons() {
        Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = param -> new TableCell<>() {
            private final Button btnEdit = new Button("Edit");
            private final Button btnDelete = new Button("Delete");

            {
                btnEdit.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    // Handle edit logic
                    NavigationManager.switchSceneWithData("/staffScene/staffUserDetails.fxml",
                            (controller, data) -> {
                                StaffUserDetailController staffUserDetailController = (StaffUserDetailController) controller;
                                staffUserDetailController.setUserDetails((User) data);
                            },
                            user);
                });

                btnDelete.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    userRepository.deleteUser(user.getUserId());
                    userList.remove(user);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox actionButtons = new HBox(btnEdit, btnDelete);
                    actionButtons.setSpacing(10);
                    setGraphic(actionButtons);
                }
            }
        };
        colAction.setCellFactory(cellFactory);
    }

    public void switchSceneLibrary(MouseEvent event) {
        NavigationManager.switchScene("/staffScene/staffLib.fxml");
    }

    public void switchSceneItems(MouseEvent event) {
        NavigationManager.switchScene("/staffScene/staffItem.fxml");
    }

    public void switchSceneUser(MouseEvent event) {
        NavigationManager.switchScene("/staffScene/staffUser.fxml");
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
}

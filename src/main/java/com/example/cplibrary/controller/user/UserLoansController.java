package com.example.cplibrary.controller.user;

import com.example.cplibrary.UserSession;
import com.example.cplibrary.application.BookLoansService;
import com.example.cplibrary.application.StaffService;
import com.example.cplibrary.controller.common.AlertManager;
import com.example.cplibrary.controller.common.NavigationManager;
import com.example.cplibrary.model.Book;
import com.example.cplibrary.model.User;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.time.LocalDate;
import java.util.Optional;

public class UserLoansController {

    @FXML
    private TableView<Object[]> tableView;

    @FXML
    private TableColumn<Object[], String> colName;

    @FXML
    private TableColumn<Object[], LocalDate> colBorrowDate;

    @FXML
    private TableColumn<Object[], LocalDate> colDueDate;

    @FXML
    private TableColumn<Object[], Void> colAction;

    @FXML
    private Label nameLabel;

    private final BookLoansService bookLoansService = new BookLoansService();
    private final User currentUser = UserSession.getInstance().getCurrentUser();
    private final StaffService staffService = new StaffService();

    @FXML
    public void initialize() {
        nameLabel.setText(currentUser.getName());
        colName.setCellValueFactory(data -> {
            Object[] row = data.getValue();
            return new ReadOnlyStringWrapper((String) row[0]);
        });
        colBorrowDate.setCellValueFactory(data -> {
            Object[] row = data.getValue();
            return new ReadOnlyObjectWrapper<>((LocalDate) row[1]);
        });
        colDueDate.setCellValueFactory(data -> {
            Object[] row = data.getValue();
            return new ReadOnlyObjectWrapper<>((LocalDate) row[2]);
        });

        colAction.setCellFactory(getActionCellFactory());

        tableView.setRowFactory(tv -> new TableRow<>() {
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

        ObservableList<Object[]> loanData = bookLoansService.fetchLoanData(currentUser.getUserId());
        tableView.setItems(loanData);
    }

    private Callback<TableColumn<Object[], Void>, TableCell<Object[], Void>> getActionCellFactory() {
        return param -> new TableCell<>() {
            private final Button viewButton = new Button("View");

            {
                viewButton.setOnAction(event -> {
                    Object[] currentRow = getTableRow().getItem();
                    String bookName = (String) currentRow[0];
                    Book book = staffService.viewBookDetails(bookName);
//                    System.out.println(book.getTitle() );
                    NavigationManager.switchSceneWithData("/userScene/userBookDetail.fxml",
                            (controller, data) -> {
                                UserBookDetailController userBookDetailController = (UserBookDetailController) controller;
                                userBookDetailController.setBookDetails((Book) data);
                            }
                    ,book);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(viewButton));
                }
            }
        };
    }

    public void switchSceneLibrary(MouseEvent mouseEvent) {
        NavigationManager.switchScene("/userScene/userLib.fxml");
    }

    public void switchSceneLoans(MouseEvent mouseEvent) {
        NavigationManager.switchScene("/userScene/userLoans.fxml");
    }

    public void switchSceneReservation(MouseEvent mouseEvent) {
        NavigationManager.switchScene("/userScene/userReservation.fxml");
    }

    public void switchSceneLogout() {
        boolean confirmed = AlertManager.showConfirmationAlert("CONFIRMATION", "Are you sure you want to logout?" ,"All unsaved changes will be lost.");
        if (confirmed) {
            NavigationManager.switchScene("/commonScene/login.fxml");
        }
    }
}

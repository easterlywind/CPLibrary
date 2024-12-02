package com.example.cplibrary.controller.user;

import com.example.cplibrary.UserSession;
import com.example.cplibrary.application.BookLoansService;
import com.example.cplibrary.application.BookReservationService;
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

public class UserReservationController {

    @FXML
    private TableView<Object[]> tableView;

    @FXML
    private TableColumn<Object[], String> colName;

    @FXML
    private TableColumn<Object[], LocalDate> colReservationDate;

    @FXML
    private TableColumn<Object[], String> colStatus;

    private final BookReservationService bookReservationService = new BookReservationService();
    private final User currentUser = UserSession.getInstance().getCurrentUser();

    @FXML
    public void initialize() {
        // Cấu hình các cột
        colName.setCellValueFactory(data -> {
            Object[] row = data.getValue();
            return new ReadOnlyStringWrapper((String) row[0]);
        });
        colReservationDate.setCellValueFactory(data -> {
            Object[] row = data.getValue();
            return new ReadOnlyObjectWrapper<>((LocalDate) row[1]);
        });
        colStatus.setCellValueFactory(data -> {
            Object[] row = data.getValue();
            return new ReadOnlyStringWrapper((String) row[2]);
        });

        // Lấy dữ liệu từ cơ sở dữ liệu
        ObservableList<Object[]> reservationDataData = bookReservationService.fetchReservationData(currentUser.getUserId());
        tableView.setItems(reservationDataData);

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

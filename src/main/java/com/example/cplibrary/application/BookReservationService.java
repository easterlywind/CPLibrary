package com.example.cplibrary.application;

import com.example.cplibrary.infrastructure.SQLBookRepository;
import com.example.cplibrary.infrastructure.SQLLoansRepository;
import com.example.cplibrary.infrastructure.SQLReservationRepository;
import javafx.collections.ObservableList;

public class BookReservationService {
    private SQLReservationRepository sqlReservationRepository;
    private SQLBookRepository sqlBookRepository;

    public BookReservationService() {
        this.sqlBookRepository = new SQLBookRepository();
        this.sqlReservationRepository = new SQLReservationRepository();
    }

    public void addReservation(int userId, int bookId) {
        sqlBookRepository.addReservation(userId, bookId);
    }

    public boolean isBookReservedByUser(int userId, int bookId) {
        return sqlBookRepository.isBookReservedByUser(userId, bookId);
    }

    public ObservableList<Object[]> fetchReservationData(int userId) {
        return sqlReservationRepository.fetchReservationData(userId);
    }
}

package com.example.cplibrary.application;

import com.example.cplibrary.infrastructure.SQLBookRepository;

public class BookReservationService {
    private SQLBookRepository sqlBookRepository;

    public BookReservationService() {
        sqlBookRepository = new SQLBookRepository();
    }

    public void addReservation(int userId, int bookId) {
        sqlBookRepository.addReservation(userId, bookId);
    }

    public boolean isBookReservedByUser(int userId, int bookId) {
        return sqlBookRepository.isBookReservedByUser(userId, bookId);
    }
}

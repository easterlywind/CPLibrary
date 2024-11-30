package com.example.cplibrary.application;

import com.example.cplibrary.infrastructure.SQLBookRepository;

public class BookLoansService {
    private SQLBookRepository sqlBookRepository;

    public BookLoansService() {
        this.sqlBookRepository = new SQLBookRepository();
    }

    public void addLoan(int bookId, int userId) {
        sqlBookRepository.addLoan(bookId, userId);
    }

    public boolean deleteLoan(int userId, int bookId) {
        return sqlBookRepository.deleteLoan(userId,bookId);
    }

    public boolean isBookBorrowedByUser(int userId, int bookId) {
        return sqlBookRepository.isBookBorrowedByUser(userId,bookId);
    }
}

package com.example.cplibrary.application;

import com.example.cplibrary.infrastructure.SQLBookRepository;
import com.example.cplibrary.infrastructure.SQLLoansRepository;
import javafx.collections.ObservableList;

public class BookLoansService {
    private SQLBookRepository sqlBookRepository;
    private SQLLoansRepository sqlLoansRepository;

    public BookLoansService() {
        this.sqlLoansRepository = new SQLLoansRepository();
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

    public ObservableList<Object[]> fetchLoanData (int userId) {
        return sqlLoansRepository.fetchLoanData(userId);
    }
}

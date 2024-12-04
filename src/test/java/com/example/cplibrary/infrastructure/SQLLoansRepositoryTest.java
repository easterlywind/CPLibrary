package com.example.cplibrary.infrastructure;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class SQLLoansRepositoryTest {

    private SQLLoansRepository loansRepository;

    @BeforeEach
    void setUp() {
        loansRepository = new SQLLoansRepository();
    }

    @Test
    void testFetchLoanData() throws SQLException {
        ObservableList<Object[]> loanData = loansRepository.fetchLoanData(1);

        assertTrue(loanData.size() > 0);

        Object[] firstLoan = loanData.get(0);
        assertNotNull(firstLoan[0]);
        assertNotNull(firstLoan[1]);
        assertNotNull(firstLoan[2]);
    }
}

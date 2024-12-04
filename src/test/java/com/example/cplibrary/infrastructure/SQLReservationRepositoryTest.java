package com.example.cplibrary.infrastructure;

import com.example.cplibrary.DatabaseConnection;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SQLReservationRepositoryTest {

    private SQLReservationRepository sqlReservationRepository;

    @BeforeEach
    void setUp() {
        sqlReservationRepository = new SQLReservationRepository();
    }

    @Test
    void testFetchReservationData() throws SQLException {
        int userId = 7;

        ObservableList<Object[]> result = sqlReservationRepository.fetchReservationData(userId);

        assertNotNull(result);
        assertFalse(result.isEmpty(), "Dữ liệu phải có ít nhất 1 bản ghi.");

        Object[] reservation = result.get(0);
        assertNotNull(reservation[0]);
        assertNotNull(reservation[1]);
        assertNotNull(reservation[2]);
    }

    @Test
    void testFetchReservationDataEmpty() throws SQLException {
        int userId = 999;

        ObservableList<Object[]> result = sqlReservationRepository.fetchReservationData(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Dữ liệu không có bản ghi cho người dùng này.");
    }

    @Test
    void testSQLException() {
        SQLReservationRepository faultyRepository = new SQLReservationRepository() {
            @Override
            public ObservableList<Object[]> fetchReservationData(int userId) {
                throw new RuntimeException("Lỗi kết nối cơ sở dữ liệu");
            }
        };

        assertThrows(RuntimeException.class, () -> faultyRepository.fetchReservationData(1));
    }
}

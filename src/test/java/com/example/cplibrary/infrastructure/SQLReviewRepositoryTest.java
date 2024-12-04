package com.example.cplibrary.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SQLReviewRepositoryTest {

    private SQLReviewRepository sqlReviewRepository;

    @BeforeEach
    void setUp() {
        sqlReviewRepository = new SQLReviewRepository();
    }

    @Test
    void testGetReviewsByBookId() {
        // Arrange
        int bookId = 3;

        List<String[]> reviews = sqlReviewRepository.getReviewsByBookId(bookId);

        assertNotNull(reviews, "Reviews should not be null");
        assertFalse(reviews.isEmpty(), "Reviews list should not be empty");

        String[] firstReview = reviews.get(0);
        assertEquals(3, firstReview.length, "Each review should have 3 elements (review, date, user)");
        assertNotNull(firstReview[0], "Review text should not be null");
        assertNotNull(firstReview[1], "Review date should not be null");
        assertNotNull(firstReview[2], "User name should not be null");
    }

    @Test
    void testAddReview() {
        // Arrange
        int bookId = 3;
        int userId = 1;
        String review = "This is a test review.";

        sqlReviewRepository.addReview(bookId, userId, review);

        List<String[]> reviews = sqlReviewRepository.getReviewsByBookId(bookId);

        assertNotNull(reviews, "Reviews should not be null");
        assertTrue(reviews.size() > 0, "At least one review should be present after adding");

        boolean reviewFound = false;
        for (String[] rev : reviews) {
            if (rev[0].equals(review)) {
                reviewFound = true;
                break;
            }
        }

        assertTrue(reviewFound, "The review should have been added successfully");
    }
}

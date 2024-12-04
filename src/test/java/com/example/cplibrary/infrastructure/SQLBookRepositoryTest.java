package com.example.cplibrary.infrastructure;

import com.example.cplibrary.model.Book;
import com.example.cplibrary.DatabaseConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SQLBookRepositoryTest {

    private static SQLBookRepository bookRepository;

    @BeforeAll
    static void setupDatabase() {
        bookRepository = new SQLBookRepository();
        try (Connection connection = new DatabaseConnection().getConnection();
             Statement stmt = connection.createStatement()) {

            stmt.execute("CREATE TABLE IF NOT EXISTS Books (" +
                    "book_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "quantity INT NOT NULL," +
                    "isbn VARCHAR(20) UNIQUE NOT NULL," +
                    "title VARCHAR(255) NOT NULL," +
                    "author VARCHAR(255) NOT NULL," +
                    "subject VARCHAR(100)," +
                    "publisher VARCHAR(100)," +
                    "shelf_location VARCHAR(50)," +
                    "review TEXT," +
                    "image_url TEXT)");

            stmt.execute("INSERT INTO Books (quantity, isbn, title, author, subject, publisher, shelf_location, review, image_url) VALUES " +
                    "(10, '1234567890', 'Test Book', 'John Doe', 'Science', 'Publisher A', 'A1', 'Great book!', 'http://example.com/image.jpg')");

        } catch (Exception e) {
            e.printStackTrace();
            fail("Database setup failed!");
        }
    }

    @AfterAll
    static void teardownDatabase() {

        try (Connection connection = new DatabaseConnection().getConnection();
             Statement stmt = connection.createStatement()) {

            stmt.execute("DROP TABLE IF EXISTS Books");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testAddBook() {
        Book newBook = new Book(0, 5, "0987654321", "New Book", "Jane Doe", "Fiction", "Publisher B", "B2", "Interesting!", "http://example.com/new_image.jpg");
        bookRepository.addBook(newBook);

        Book fetchedBook = bookRepository.getBookByIsbn("New Book");
        assertNotNull(fetchedBook);
        assertEquals("0987654321", fetchedBook.getIsbn());
        assertEquals("New Book", fetchedBook.getTitle());
    }

    @Test
    void testUpdateBook() {
        Book existingBook = bookRepository.getBookByIsbn("Test Book");
        assertNotNull(existingBook);

        existingBook.setTitle("Updated Test Book");
        bookRepository.updateBook(existingBook);

        Book updatedBook = bookRepository.getBookByIsbn("Updated Test Book");
        assertNotNull(updatedBook);
        assertEquals("Updated Test Book", updatedBook.getTitle());
    }

    @Test
    void testDeleteBook() {
        bookRepository.deleteBook("1234567890");
        Book deletedBook = bookRepository.getBookByIsbn("Test Book");
        assertNull(deletedBook);
    }

    @Test
    void testSearchBooks() {
        List<Book> results = bookRepository.searchBooks("Test");
        assertFalse(results.isEmpty());
        assertTrue(results.stream().anyMatch(book -> book.getTitle().contains("Test")));
    }

    @Test
    void testGetAllBooks() {
        List<Book> allBooks = bookRepository.getAllBooks();
        assertFalse(allBooks.isEmpty());
    }
}

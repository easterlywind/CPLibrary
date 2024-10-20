package com.example.cplibrary.application;
import com.example.cplibrary.model.Book;
import com.example.cplibrary.infrastructure.SQLBookRepository;

public class LibrarianService {
    private SQLBookRepository bookRepository;

    public LibrarianService(SQLBookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Nghiệp vụ của thủ thư
    public void addBook(Book book) {
        bookRepository.addBook(book);
    }

    public void deleteBook(String isbn) {
        bookRepository.deleteBook(isbn);
    }

    public Book viewBookDetails(String isbn) {
        return bookRepository.getBookByIsbn(isbn);
    }

    public void updateBook(Book book) {
        bookRepository.updateBook(book);
    }
}

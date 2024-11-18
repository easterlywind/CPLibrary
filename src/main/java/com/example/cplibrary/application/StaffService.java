package com.example.cplibrary.application;
import com.example.cplibrary.infrastructure.SQLUserRepository;
import com.example.cplibrary.model.Book;
import com.example.cplibrary.infrastructure.SQLBookRepository;
import com.example.cplibrary.model.Member;
import com.example.cplibrary.model.User;

import java.util.List;

public class StaffService {
    private SQLBookRepository bookRepository;
    private SQLUserRepository userRepository;

    public StaffService() {
        this.userRepository = new SQLUserRepository();
        this.bookRepository = new SQLBookRepository();
    }

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

    public void addUser(User user) {
        userRepository.addUser(user);
    }

    public void deleteUser(int userId) {
        userRepository.deleteUser(userId);
    }

    public Member getUserById(int userId) {
        return userRepository.getUserById(userId);
    }

    public void updateUser(User user) {
        userRepository.updateUser(user);
    }

    public Member getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    public Member getUserByPhone(String phone) {
        return userRepository.getUserByPhone(phone);
    }

    public List<Member> getUsersByStatus(String status) {
        return userRepository.getUsersByStatus(status);
    }

}

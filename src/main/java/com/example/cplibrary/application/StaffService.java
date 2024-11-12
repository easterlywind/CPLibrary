package com.example.cplibrary.application;
import com.example.cplibrary.infrastructure.SQLUserRepository;
import com.example.cplibrary.model.Book;
import com.example.cplibrary.infrastructure.SQLBookRepository;
import com.example.cplibrary.model.User;

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

    public void addMember(User member) {
        userRepository.addUser(member);
    }

    public void deleteMember(String memberId) {
        userRepository.deleteUser(memberId);
    }

    public User viewMemberDetails(String memberId) {
        return userRepository.getUserById(memberId);
    }

    public void updateMember(User member) {
        userRepository.updateUser(member);
    }
}

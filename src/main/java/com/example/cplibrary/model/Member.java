package com.example.cplibrary.model;

import java.util.ArrayList;
import java.util.List;

public class Member  {
    private String memberId;
    private String libraryCard;
    private List<Book> borrowedBooks = new ArrayList<>();
    private static final int MAX_BOOKS = 10;

    public Member(String memberId , List<Book> borrowedBooks, String libraryCard) {
        this.memberId = memberId;
        this.borrowedBooks = borrowedBooks;
        this.libraryCard = libraryCard;
    }

    public Member() {
        memberId = "";
        borrowedBooks = new ArrayList<Book>();
        libraryCard = "";
    }

    public boolean canBorrow() {
        return this.borrowedBooks.size() >= MAX_BOOKS;
    }

    public void borrowBook(Book book) {
        if (!canBorrow()) {
            borrowedBooks.add(book);
        } else {
            System.out.println("Can't borrow this book");
        }
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getLibraryCard() {
        return libraryCard;
    }

    public void setLibraryCard(String libraryCard) {
        this.libraryCard = libraryCard;
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(List<Book> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }
}

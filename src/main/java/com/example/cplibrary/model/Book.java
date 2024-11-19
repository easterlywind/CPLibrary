package com.example.cplibrary.model;

public class Book {
    private int book_id;
    private String isbn;
    private String title;
    private String author;
    private String subject;
    private String publisher;
    private String shelfLocation;
    private String review;

    // Constructor với đầy đủ thông tin
    public Book(int book_id, String isbn, String title, String author, String subject, String publisher, String shelfLocation, String review) {
        this.book_id = book_id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.subject = subject;
        this.publisher = publisher;
        this.shelfLocation = shelfLocation;
        this.review = review;
    }

    // Getters và Setters cho các thuộc tính

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getShelfLocation() {
        return shelfLocation;
    }

    public void setShelfLocation(String shelfLocation) {
        this.shelfLocation = shelfLocation;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}

package com.example.cplibrary.model;

public class Book {
    private int book_id;
    private int quantity;
    private String isbn;
    private String title;
    private String author;
    private String subject;
    private String publisher;
    private String shelfLocation;
    private String review;
    private String imageUrl;

    public Book() {
    }

    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    public Book(int book_id, int quantity, String isbn, String title, String author, String subject, String publisher, String shelfLocation, String review, String imageUrl) {
        this.book_id = book_id;
        this.quantity = quantity;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.subject = subject;
        this.publisher = publisher;
        this.shelfLocation = shelfLocation;
        this.review = review;
        this.imageUrl = imageUrl;
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

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

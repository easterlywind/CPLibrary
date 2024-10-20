package com.example.cplibrary.model;

public class Book {
    private String isbn;
    private String title;
    private String author;
    private String subject;

    public Book(String isbn, String title, String author, String subject) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.subject = subject;
    }

    // Getter và Setter
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
}

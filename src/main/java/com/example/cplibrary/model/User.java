package com.example.cplibrary.model;

public abstract class User {
    private String userId;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String status;
    private LibraryCard card;

    public User(String userId, String name, String email, String password, String phone) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.status = "Available";
        this.card = new LibraryCard();
    }

    // Getter và Setter
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LibraryCard getCard() {
        return card;
    }

    public String getStatus() {
        return status;
    }

    public void setCard(LibraryCard card) {
        this.card = card;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public abstract void resetPassword();
}


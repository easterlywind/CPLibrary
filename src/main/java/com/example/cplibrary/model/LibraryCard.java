package com.example.cplibrary.model;

public class LibraryCard {
    private String cardNumber;
    private String issued;
    private String active;

    public boolean isActive() {
        return active.equals("Y");
    }
}

package com.example.cplibrary.model;

public class Admin extends User {
    public Admin(String userId, String name, String email, String password, String phone) {
        super(userId, name, email, password, phone);
    }

    @Override
    public void resetPassword() {

    }
}

package com.example.cplibrary.model;

public class Member extends User {

    public Member() {
    }

    public Member(int userId, String name, String email, String phone, String password, String status) {
        super(userId, name, email, phone, password, status);
    }
}

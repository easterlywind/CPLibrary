package com.example.cplibrary.model;

public class Member extends User {

    public Member() {
    }

    public Member(String userId, String name, String email, String password, String phone, String status) {
        super(userId, name, email, password, phone, status);
    }
}

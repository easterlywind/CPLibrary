package com.example.cplibrary.model;

public interface UserManagement {
    void addUser(User user);
    void updateUser(User user);
    void deleteUser(String userId);
    User viewUserDetails(String userId);
}


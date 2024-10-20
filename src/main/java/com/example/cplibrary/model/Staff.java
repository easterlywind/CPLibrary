package com.example.cplibrary.model;

public class Staff extends User implements UserManagement {
    public Staff(String userId, String name, String email, String password, String phone) {
        super(userId, name, email, password, phone);
    }

    @Override
    public void addUser(User user) {
        // Code for adding user
    }

    @Override
    public void updateUser(User user) {
        // Code for updating user
    }

    @Override
    public void deleteUser(String userId) {
        // Code for deleting user
    }

    @Override
    public User viewUserDetails(String userId) {
        // Code for viewing user details
        return null; // Placeholder
    }

}

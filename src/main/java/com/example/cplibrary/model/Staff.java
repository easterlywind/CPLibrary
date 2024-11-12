package com.example.cplibrary.model;

import java.util.HashMap;
import java.util.Map;

public class Staff extends User  {
    // Temporary in-memory storage for demonstration
    private Map<String, User> userDatabase = new HashMap<>();

    public Staff(String userId, String name, String email, String password, String phone) {
        super(userId, name, email, password, phone);
    }

    @Override
    public void resetPassword() {
        // Basic password reset logic for demonstration
        System.out.println("Password reset functionality to be implemented");
    }
}

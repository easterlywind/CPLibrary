package com.example.cplibrary.application;

import com.example.cplibrary.model.User;
import com.example.cplibrary.infrastructure.SQLUserRepository;

public class AdminService {
    private SQLUserRepository userRepository;

    public AdminService() {
        this.userRepository = new SQLUserRepository();
    }

    public void addMember(User member) {
        userRepository.addUser(member);
    }

    public void deleteMember(String memberId) {
        userRepository.deleteUser(memberId);
    }

    public User viewMemberDetails(String memberId) {
        return userRepository.getUserById(memberId);
    }

    public void updateMember(User member) {
        userRepository.updateUser(member);
    }
}

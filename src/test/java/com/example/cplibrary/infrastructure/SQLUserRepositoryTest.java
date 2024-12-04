package com.example.cplibrary.infrastructure;

import com.example.cplibrary.model.Member;
import com.example.cplibrary.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SQLUserRepositoryTest {

    private SQLUserRepository sqlUserRepository;

    @BeforeEach
    void setUp() {
        sqlUserRepository = new SQLUserRepository();
    }

    @AfterEach
    void cleanUp() {
        List<User> users = sqlUserRepository.getAllUsers();
        for (User user : users) {
            if (user.getUserId() > 0) {
                sqlUserRepository.deleteUser(user.getUserId());
            }
        }
    }

    @Test
    void testAddUser() {
        User user = new User(10, "John Doeu", "johnu@example.com", "password123", "active");

        sqlUserRepository.addUser(user);

        Member retrievedUser = sqlUserRepository.getUserById(10);
        assertNotNull(retrievedUser, "User should be added successfully");
        assertEquals("John Doeu", retrievedUser.getName(), "User name should match");
        assertEquals("johnu@example.com", retrievedUser.getEmail(), "User email should match");
    }

    @Test
    void testUpdateUser() {
        User user = new User(1, "John Doe", "john@example.com", "password123", "active");
        sqlUserRepository.addUser(user);

        user.setName("John Updated");
        user.setEmail("john.updated@example.com");

        boolean isUpdated = sqlUserRepository.updateUser(user);

        assertTrue(isUpdated, "User should be updated successfully");
        Member updatedUser = sqlUserRepository.getUserById(1);
        assertEquals("John Updated", updatedUser.getName(), "User name should be updated");
        assertEquals("john.updated@example.com", updatedUser.getEmail(), "User email should be updated");
    }

    @Test
    void testDeleteUser() {
        User user = new User(1, "John Doe", "john@example.com", "password123", "active");
        sqlUserRepository.addUser(user); // Add user to the database

        sqlUserRepository.deleteUser(1);

        Member deletedUser = sqlUserRepository.getUserById(1);
        assertNull(deletedUser, "User should be deleted from the database");
    }

    @Test
    void testGetUserById() {
        User user = new User(1, "John Doe", "john@example.com", "password123", "active");
        sqlUserRepository.addUser(user);

        Member retrievedUser = sqlUserRepository.getUserById(1);

        assertNotNull(retrievedUser, "User should be found");
        assertEquals(1, retrievedUser.getUserId(), "User ID should match");
        assertEquals("John Doe", retrievedUser.getName(), "User name should match");
    }

    @Test
    void testGetUserByEmail() {

        User user = new User(1, "John Doe", "john@example.com", "password123", "active");
        sqlUserRepository.addUser(user);

        Member retrievedUser = sqlUserRepository.getUserByEmail("john@example.com");

        assertNotNull(retrievedUser, "User should be found by email");
        assertEquals(1, retrievedUser.getUserId(), "User ID should match");
        assertEquals("john@example.com", retrievedUser.getEmail(), "User email should match");
    }

    @Test
    void testGetUsersByStatus() {

        User activeUser = new User(1, "John Doe", "john@example.com", "password123", "active");
        User bannedUser = new User(2, "Jane Doe", "jane@example.com", "password456", "banned");
        sqlUserRepository.addUser(activeUser);
        sqlUserRepository.addUser(bannedUser);

        List<Member> activeUsers = sqlUserRepository.getUsersByStatus("active");

        assertEquals(1, activeUsers.size(), "There should be exactly one active user");
        assertEquals("John Doe", activeUsers.get(0).getName(), "Active user's name should match");
    }
}

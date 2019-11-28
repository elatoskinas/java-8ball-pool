package com.sem.pool.database.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserTest {
    private User user;

    @BeforeEach
    void before() {
        this.user = new User(42, "test", "$2a$10$zHvWR86cQ0BYVuV9NL0w1OpAANtJftHMhknR/XlWJ.OkclTHsmRqq");
    }

    @Test
    void newUser() {
        User user = new User("test", "testPassword");
        assertFalse(user.isExisting());
        assertEquals("test", user.getUsername());
        assertNotEquals("testPassword", user.getPassword());
        System.out.println(user.getPassword());
        assertTrue(this.user.checkPassword("testPassword"));
    }

    @Test
    void existingUser() {
        assertTrue(this.user.isExisting());
        assertEquals(42, this.user.getUserID());
        assertEquals("test", this.user.getUsername());
        assertEquals("$2a$10$zHvWR86cQ0BYVuV9NL0w1OpAANtJftHMhknR/XlWJ.OkclTHsmRqq", this.user.getPassword());
        assertTrue(this.user.checkPassword("testPassword"));
    }

    @Test
    void passwordValidation() {
        assertTrue(this.user.checkPassword("testPassword"));
        assertFalse(this.user.checkPassword("wrongPassword"));
    }

    @Test
    void setVariables() {
        this.user.setUserID(666);
        this.user.setUsername("yeet42");
        this.user.setPassword("$2a$10$RVyY7i9s3C6KodXIeNXyUusuXeJnNM52QK0niLWTW399lFKvf7Zc2");
        assertEquals(666, this.user.getUserID());
        assertEquals("yeet42", this.user.getUsername());
        assertEquals("$2a$10$RVyY7i9s3C6KodXIeNXyUusuXeJnNM52QK0niLWTW399lFKvf7Zc2", this.user.getPassword());
        assertTrue(this.user.checkPassword("newPassword"));
    }

    @Test
    void toStringTest() {
        assertEquals("test", this.user.toString());
    }
}

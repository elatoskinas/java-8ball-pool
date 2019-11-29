package com.sem.pool.database.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class UserTest {
    private final transient String defaultPassword = "testPassword";
    private final transient String defaultUsername = "test";

    private transient User user;

    @BeforeEach
    void before() {
        this.user = new User(42, "test",
                "$2a$10$zHvWR86cQ0BYVuV9NL0w1OpAANtJftHMhknR/XlWJ.OkclTHsmRqq");
    }

    @Test
    void newUser() {
        User user = new User(this.defaultUsername, this.defaultPassword);
        assertFalse(user.isExisting());
        assertEquals(this.defaultUsername, user.getUsername());
        assertNotEquals(this.defaultPassword, user.getPassword());
        System.out.println(user.getPassword());
        assertTrue(this.user.checkPassword(this.defaultPassword));
    }

    @Test
    void existingUser() {
        assertTrue(this.user.isExisting());
        assertEquals(42, this.user.getUserID());
        assertEquals(this.defaultUsername, this.user.getUsername());
        assertEquals("$2a$10$zHvWR86cQ0BYVuV9NL0w1OpAANtJftHMhknR/XlWJ.OkclTHsmRqq",
                this.user.getPassword());
        assertTrue(this.user.checkPassword(this.defaultPassword));
    }

    @Test
    void passwordValidation() {
        assertTrue(this.user.checkPassword(this.defaultPassword));
        assertFalse(this.user.checkPassword("wrongPassword"));
    }

    @Test
    void setVariables() {
        this.user.setUserID(666);
        this.user.setUsername("yeet42");
        this.user.setPassword("$2a$10$RVyY7i9s3C6KodXIeNXyUusuXeJnNM52QK0niLWTW399lFKvf7Zc2");
        assertEquals(666, this.user.getUserID());
        assertEquals("yeet42", this.user.getUsername());
        assertEquals("$2a$10$RVyY7i9s3C6KodXIeNXyUusuXeJnNM52QK0niLWTW399lFKvf7Zc2",
                this.user.getPassword());
        assertTrue(this.user.checkPassword("newPassword"));
    }

    @Test
    void toStringTest() {
        assertEquals(this.defaultUsername, this.user.toString());
    }
}

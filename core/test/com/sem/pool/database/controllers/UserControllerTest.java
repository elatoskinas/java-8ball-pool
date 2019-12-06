package com.sem.pool.database.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.sem.pool.database.Database;
import com.sem.pool.database.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserControllerTest {
    @BeforeEach
    public void setup() {
        Database.setTestMode();
    }

    @Test
    public void login() {
        User testUser = new User("real", "foobar");
        UserController.register("real", "foobar");
        assertEquals(testUser, UserController.login("real", "foobar"));
    }

    @Test
    public void loginWrongPW() {
        UserController.register("real", "foobar");
        assertNull(UserController.login("real", "barfoo"));
    }

    @Test
    public void loginNonExisting() {
        assertNull(UserController.login("notreal", "foobar"));
    }

    @Test
    public void register() {
        User testUser = new User("other", "foobar");
        assertEquals(testUser, UserController.register("other", "foobar"));
    }

    @Test
    public void registerExists() {
        UserController.register("other", "foobar");
        assertNull(UserController.register("other", "foobar"));
    }

    @Test
    public void nonExistingUser() {
        assertFalse(UserController.exists("notreal"));
    }

    @Test
    public void existingUser() {
        UserController.register("real", "foobar");
        assertTrue(UserController.exists("real"));
    }
}

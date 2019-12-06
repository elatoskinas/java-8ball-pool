package com.sem.pool.database.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.sem.pool.database.Database;
import com.sem.pool.database.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class UserControllerTest {
    private transient UserController userController;

    @BeforeEach
    public void setUp() {
        Database.setTestMode();
        this.userController = new UserController(Database.getInstance());
    }

    @Test
    public void login() {
        User testUser = new User("real", "foobar");
        this.userController.register("real", "foobar");
        assertEquals(testUser, this.userController.login("real", "foobar"));
    }

    @Test
    public void loginWrongPW() {
        this.userController.register("real", "foobar");
        assertNull(this.userController.login("real", "barfoo"));
    }

    @Test
    public void loginNonExisting() {
        assertNull(this.userController.login("notreal", "foobar"));
    }

    @Test
    public void register() {
        User testUser = new User("other", "foobar");
        assertEquals(testUser, this.userController.register("other", "foobar"));
    }

    @Test
    public void registerExists() {
        this.userController.register("other", "foobar");
        assertNull(this.userController.register("other", "foobar"));
    }

    @Test
    public void nonExistingUser() {
        assertFalse(this.userController.exists("notreal"));
    }

    @Test
    public void existingUser() {
        this.userController.register("real", "foobar");
        assertTrue(this.userController.exists("real"));
    }
}

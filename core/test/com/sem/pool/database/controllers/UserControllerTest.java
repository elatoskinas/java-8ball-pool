package com.sem.pool.database.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sem.pool.database.Database;
import com.sem.pool.database.models.Result;
import com.sem.pool.database.models.User;
import com.sem.pool.database.tables.ResultTable;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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

    @Test
    public void getUserById() {
        User user = this.userController.register("user", "pass");
        User user2 = this.userController.getUser(user.getUserID());
        assertEquals(user, user2);
    }

    @Test
    public void getUserByIdFailed() throws SQLException {
        Database db = Mockito.mock(Database.class);
        ResultTable table = Mockito.mock(ResultTable.class);
        UserController userController = new UserController(db);

        Mockito.when(db.table(Mockito.anyString())).thenReturn(table);
        Mockito.when(table.save(Mockito.any(Result.class))).thenThrow(SQLException.class);

        assertNull(userController.getUser(0));
    }

    @Test
    public void getUserByUsername() {
        User user = this.userController.register("username", "S3cr!d");
        User user2 = this.userController.getUser("username");
        assertEquals(user, user2);
    }

    @Test
    public void getUserByUsernameFailed() throws SQLException {
        Database db = Mockito.mock(Database.class);
        ResultTable table = Mockito.mock(ResultTable.class);
        UserController userController = new UserController(db);

        Mockito.when(db.table(Mockito.anyString())).thenReturn(table);
        Mockito.when(table.save(Mockito.any(Result.class))).thenThrow(SQLException.class);

        assertNull(userController.getUser("thiswillfail"));
    }
}

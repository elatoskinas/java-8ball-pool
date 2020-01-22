package com.sem.pool.database.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sem.pool.database.Database;
import com.sem.pool.database.models.Result;
import com.sem.pool.database.models.Stats;
import com.sem.pool.database.models.User;
import com.sem.pool.database.tables.ResultTable;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sem.pool.database.tables.UserTable;
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

    /**
     * Test if logging in with the correct details works.
     */
    @Test
    public void login() {
        User testUser = new User("real", "foobar");
        this.userController.register("real", "foobar");
        assertEquals(testUser, this.userController.login("real", "foobar"));
    }

    /**
     * Test if logging in with the wrong details fails.
     */
    @Test
    public void loginWrongPW() {
        this.userController.register("real", "foobar");
        assertNull(this.userController.login("real", "barfoo"));
    }

    /**
     * Test if logging in with a non-existing user fails.
     */
    @Test
    public void loginNonExisting() {
        assertNull(this.userController.login("notreal", "foobar"));
    }

    /**
     * Test if registering works.
     */
    @Test
    public void register() {
        User testUser = new User("other", "foobar");
        assertEquals(testUser, this.userController.register("other", "foobar"));
    }

    /**
     * Check if registering an account which exists fails.
     */
    @Test
    public void registerExists() {
        this.userController.register("other", "foobar");
        assertNull(this.userController.register("other", "foobar"));
    }

    /**
     * Check if a non existing user does not exists.
     */
    @Test
    public void nonExistingUser() {
        assertFalse(this.userController.exists("notreal"));
    }

    /**
     * Test if an existing user exists.
     */
    @Test
    public void existingUser() {
        this.userController.register("real", "foobar");
        assertTrue(this.userController.exists("real"));
    }

    /**
     * Test if we can get a user by ID.
     */
    @Test
    public void getUserById() {
        User user = this.userController.register("user", "pass");
        User user2 = this.userController.getUser(user.getUserID());
        assertEquals(user, user2);
    }

    /**
     * Test if we properly catch any exceptions by the database.
     */
    @Test
    public void getUserByIdFailed() throws SQLException {
        Database db = Mockito.mock(Database.class);
        ResultTable table = Mockito.mock(ResultTable.class);
        UserController userController = new UserController(db);

        Mockito.when(db.table(Mockito.anyString())).thenReturn(table);
        Mockito.when(table.save(Mockito.any(Result.class))).thenThrow(SQLException.class);

        assertNull(userController.getUser(0));
    }

    /**
     * Test if we can get a user by username.
     */
    @Test
    public void getUserByUsername() {
        User user = this.userController.register("username", "S3cr!d");
        User user2 = this.userController.getUser("username");
        assertEquals(user, user2);
    }

    /**
     * Test if getting a user by username fails if the user does not exists.
     * @throws SQLException This exception is thrown to test the catching.
     */
    @Test
    public void getUserByUsernameFailed() throws SQLException {
        Database db = Mockito.mock(Database.class);
        ResultTable table = Mockito.mock(ResultTable.class);
        UserController userController = new UserController(db);

        Mockito.when(db.table(Mockito.anyString())).thenReturn(table);
        Mockito.when(table.save(Mockito.any(Result.class))).thenThrow(SQLException.class);

        assertNull(userController.getUser("thiswillfail"));
    }

    /**
     * Check if getting all users returns the correct result.
     */
    @Test
    public void getUsers() throws SQLException {
        Database db = Mockito.mock(Database.class);
        UserTable table = Mockito.mock(UserTable.class);

        ArrayList<User> users = new ArrayList<>();
        User user0 = new User(42, "Hello", "hashedpassword");
        User user1 = new User(666, "World", "password");
        users.add(user0);
        users.add(user1);

        Mockito.when(db.table(Mockito.anyString())).thenReturn(table);
        Mockito.when(table.getUsers()).thenReturn(users);

        UserController userController = new UserController(db);
        ArrayList<User> top = userController.getUsers();

        assertEquals(2, top.size());
        assertEquals(user0, top.get(0));
        assertEquals(user1, top.get(1));
    }

    /**
     * Check an database error is properly catched.
     */
    @Test
    public void getUsersFailed() throws SQLException {
        Database db = Mockito.mock(Database.class);
        UserTable table = Mockito.mock(UserTable.class);

        Mockito.when(db.table(Mockito.anyString())).thenReturn(table);
        Mockito.when(table.getUsers()).thenThrow(SQLException.class);

        UserController userController = new UserController(db);
        assertEquals(new ArrayList<>(), userController.getUsers());
    }
}

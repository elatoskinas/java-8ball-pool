package com.sem.pool.database.tables;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sem.pool.database.Database;
import com.sem.pool.database.models.User;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class UserTableTest {
    private transient UserTable userTable;

    /**
     * Sets up the database in test (in-memory) mode to prepare
     * for integration testing. Also initializes user table to the
     * database's table.
     */
    @BeforeEach
    public void setUp() {
        Database.setTestMode();

        this.userTable = (UserTable) Database.getInstance().table("User");
    }

    /**
     * Test a failing update when the user does not exists.
     * @throws SQLException Should not happen.
     */
    @Test
    public void testNoneUpdate() throws SQLException {
        User user = new User("blabla", "password");
        assertFalse(this.userTable.update(user));
    }

    /**
     * Test getting a user by ID.
     * @throws SQLException Should not happen.
     */
    @Test
    public void testGetUserID() throws SQLException {
        User user = new User("blabla", "password");
        User savedUser = this.userTable.getUser(user.getUsername());

        assertEquals(savedUser, this.userTable.getUser(user.getUserID()));
    }

    /**
     * Test updating of a user.
     * @throws SQLException Should not happen.
     */
    @Test
    public void testUpdate() throws SQLException {
        User user = new User("yeet", "pass");
        assertTrue(this.userTable.save(user));

        user = this.userTable.getUser("yeet");
        user.setUsername("yote");
        assertTrue(this.userTable.update(user));

        user = this.userTable.getUser("yeet");
        assertNull(user);

        user = this.userTable.getUser("yote");
        assertNotNull(user);
    }

    /**
     * Test updating an invalid, existing user.
     * @throws SQLException Should not happen.
     */
    @Test
    public void testInvalidExistingUpdate() throws SQLException {
        User user = new User("abc", "pass");
        assertTrue(this.userTable.save(user));

        user = this.userTable.getUser("abc");
        user.setExisting(false);
        assertFalse(this.userTable.update(user));
    }

    /**
     * Test updating with an invalid name.
     * @throws SQLException Should not occur.
     */
    @Test
    public void testInvalidNameUpdate() throws SQLException {
        User user = new User("def", "pass");
        assertTrue(this.userTable.save(user));

        user = this.userTable.getUser("def");
        user.setUsername("ghi");
        user.setUserID(42);
        assertFalse(this.userTable.update(user));
    }

    /**
     * Test that the update catches database errors.
     * The CloseResource warning is suppressed as this is a mocked object.
     * @throws SQLException Should not occur.
     */
    @Test
    @SuppressWarnings("PMD.CloseResource")
    public void testErrorWhenUpdating() throws SQLException {
        Connection conn = Mockito.mock(Connection.class);
        PreparedStatement stmt = Mockito.mock(PreparedStatement.class);
        DatabaseMetaData meta = Mockito.mock(DatabaseMetaData.class);
        ResultSet tables = Mockito.mock(ResultSet.class);

        Mockito.when(conn.prepareStatement(Mockito.anyString())).thenReturn(stmt);
        Mockito.when(conn.getMetaData()).thenReturn(meta);
        Mockito.when(stmt.executeUpdate()).thenThrow(SQLException.class);
        Mockito
                .when(meta.getTables(
                        Mockito.isNull(),
                        Mockito.isNull(),
                        Mockito.anyString(),
                        Mockito.isNull()
                ))
                .thenReturn(tables);
        Mockito.when(tables.isAfterLast()).thenReturn(false);

        UserTable userTable = new UserTable(conn);

        User user = new User(42, "Jeff", "Dora");
        assertFalse(userTable.update(user));
    }

    /**
     * Test the flow of the saving is correct when a sqlerror occurs.
     * PMD error is ignored, as this is a mock object.
     * @throws SQLException When a exception occurs.
     */
    @Test
    @SuppressWarnings("PMD.CloseResource")
    public void testInvalidStatement() throws SQLException {
        Connection conn = Mockito.mock(Connection.class);
        PreparedStatement stmt = Mockito.mock(PreparedStatement.class);
        DatabaseMetaData meta = Mockito.mock(DatabaseMetaData.class);
        ResultSet tables = Mockito.mock(ResultSet.class);

        Mockito.when(conn.prepareStatement(Mockito.anyString())).thenReturn(stmt);
        Mockito.when(conn.getMetaData()).thenReturn(meta);
        Mockito.when(stmt.executeUpdate()).thenThrow(SQLException.class);
        Mockito
                .when(meta.getTables(
                        Mockito.isNull(),
                        Mockito.isNull(),
                        Mockito.anyString(),
                        Mockito.isNull()
                ))
                .thenReturn(tables);
        Mockito.when(tables.isAfterLast()).thenReturn(false);

        UserTable table = new UserTable(conn);
        User user = new User(69, "user", "passwd");

        assertThrows(SQLException.class, () -> {
            table.save(user);
        });
    }

    /**
     * Test if the table name matches.
     * This is to catch regression bugs
     */
    @Test
    public void testTableName() throws SQLException {
        assertEquals("User", this.userTable.getTableName());
    }

    /**
     * Test if fetching a empty table returns zero results.
     * @throws SQLException Database error, should not happen.
     */
    @Test
    public void testGetUsers() throws SQLException {
        ArrayList<User> result = this.userTable.getUsers();

        assertNotNull(result);
        assertEquals(new ArrayList<>(), result);
    }

    /**
     * Test if fetching a filled table returns it's contents.
     * @throws SQLException Database error, should not happen.
     */
    @Test
    public void testGetUsersWithData() throws SQLException {
        User user0 = new User("Hello", "password");
        User user1 = new User("World", "123456");
        this.userTable.save(user0);
        this.userTable.save(user1);

        final User user0DB = this.userTable.getUser("Hello");
        final User user1DB = this.userTable.getUser("World");

        ArrayList<User> users = this.userTable.getUsers();

        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals(user0DB, users.get(0));
        assertEquals(user1DB, users.get(1));
    }
}

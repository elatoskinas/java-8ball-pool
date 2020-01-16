package com.sem.pool.database.tables;

import com.sem.pool.database.Database;
import com.sem.pool.database.models.Result;
import com.sem.pool.database.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void testNoneUpdate() throws SQLException {
        User user = new User("blabla", "password");
        assertFalse(this.userTable.update(user));
    }

    @Test
    public void testGetUserID() throws SQLException {
        User user = new User("blabla", "password");
        User savedUser = this.userTable.getUser(user.getUsername());

        assertEquals(savedUser, this.userTable.getUser(user.getUserID()));
    }

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

    @Test
    public void testInvalidExistingUpdate() throws SQLException {
        User user = new User("abc", "pass");
        assertTrue(this.userTable.save(user));

        user = this.userTable.getUser("abc");
        user.setExisting(false);
        assertFalse(this.userTable.update(user));
    }

    @Test
    public void testInvalidNameUpdate() throws SQLException {
        User user = new User("def", "pass");
        assertTrue(this.userTable.save(user));

        user = this.userTable.getUser("def");
        user.setUsername("ghi");
        user.setUserID(42);
        assertFalse(this.userTable.update(user));
    }

    @Test
    public void testInvalidStatement() throws SQLException {
        Connection conn = Mockito.mock(Connection.class);
        PreparedStatement stmt = Mockito.mock(PreparedStatement.class);
        UserTable table = new UserTable(conn);

        Mockito.when(conn.prepareStatement(Mockito.anyString())).thenReturn(stmt);
        Mockito.when(stmt.executeUpdate()).thenThrow(SQLException.class);

        User user = new User(69, "user", "pass");

        assertThrows(SQLException.class, () -> {
            table.save(user);
        });
    }
}

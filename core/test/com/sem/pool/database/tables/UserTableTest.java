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
}

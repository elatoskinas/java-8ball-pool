package com.sem.pool.database.tables;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sem.pool.database.Database;
import com.sem.pool.database.models.User;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}

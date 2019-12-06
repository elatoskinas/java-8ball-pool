package com.sem.pool.database.tables;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.sem.pool.database.Database;
import com.sem.pool.database.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class UserTableTest {
    private transient UserTable userTable;

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
}

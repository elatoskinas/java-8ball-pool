package com.sem.pool.database.tables;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sem.pool.database.Database;
import com.sem.pool.database.models.Result;
import com.sem.pool.database.models.User;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ResultTableTest {
    private transient ResultTable resultTable;

    /**
     * Sets up the database in test (in-memory) mode to prepare
     * for integration testing. Also initializes user table to the
     * database's table.
     */
    @BeforeEach
    public void setUp() {
        Database.setTestMode();

        this.resultTable = (ResultTable) Database.getInstance().table("Result");
    }

    /**
     * Test the saving of a result gives the correct result.
     * @throws SQLException Throws an error if not successfully.
     */
    @Test
    public void testSave() throws SQLException {
        User user = new User(69, "user", "pass");
        Result result = new Result(42, user, user);
        assertTrue(this.resultTable.save(result));
    }

    /**
     * Test if you can insert the same result twice.
     * @throws SQLException Throws an error if not successfully.
     */
    @Test
    public void testDoubleInsert() throws SQLException {
        User user = new User(69, "user", "pass");
        Result result = new Result(42, user, user);
        assertTrue(this.resultTable.save(result));
        assertTrue(this.resultTable.save(result));
    }
}

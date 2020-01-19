package com.sem.pool.database.tables;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sem.pool.database.Database;
import com.sem.pool.database.models.Result;
import com.sem.pool.database.models.User;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
     * Test the saving of a result gives the correct result.
     * PMD error ignored as this is a mock object.
     * @throws SQLException Throws an error if not successfully.
     */
    @Test
    @SuppressWarnings("PMD.CloseResource")
    public void testSaveFailed() throws SQLException {
        Connection conn = Mockito.mock(Connection.class);
        PreparedStatement stmt = Mockito.mock(PreparedStatement.class);
        DatabaseMetaData meta = Mockito.mock(DatabaseMetaData.class);
        ResultSet tables = Mockito.mock(ResultSet.class);

        Mockito.when(conn.prepareStatement(Mockito.anyString())).thenReturn(stmt);
        Mockito.when(conn.getMetaData()).thenReturn(meta);
        Mockito.when(stmt.executeUpdate()).thenReturn(0);
        Mockito
                .when(meta.getTables(
                        Mockito.isNull(),
                        Mockito.isNull(),
                        Mockito.anyString(),
                        Mockito.isNull()
                ))
                .thenReturn(tables);
        Mockito.when(tables.isAfterLast()).thenReturn(false);

        ResultTable table = new ResultTable(conn);
        User user = new User(69, "user", "pass");
        Result result = new Result(42, user, user);

        assertFalse(table.save(result));
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

    /**
     * Test if you can insert the same result twice.
     * @throws SQLException Throws an error if not successfully.
     */
    @Test
    public void testGetUserID() throws SQLException {
        User user = new User(69, "userName", "passWord");
        Result result = new Result(42, user, user);
        assertTrue(this.resultTable.save(result));
        assertTrue(this.resultTable.save(result));
    }

    /**
     * Test if the table name matches.
     * This is to catch regression bugs
     */
    @Test
    public void testTableName() throws SQLException {
        assertEquals("Result", this.resultTable.getTableName());
    }
}

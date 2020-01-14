package com.sem.pool.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Abstract class, to be implemented by database tables implementations.
 * Look at `./models` for examples.
 */
public abstract class Table {
    /**
     * Connection to use for queries.
     */
    protected final transient Connection conn;
    /**
     * Name of the table.
     */
    protected final transient String tableName;

    /**
     * Create the table instance.
     *
     * @param conn The connection to use.
     */
    public Table(Connection conn, String tableName) throws SQLException {
        this.conn = conn;
        this.tableName = tableName;

        this.ensureTable();
    }

    /**
     * Create the table.
     * Should be implemented by the extenders.
     */
    protected abstract void createTable() throws SQLException;

    /**
     * Ensure that the table exists.
     * If not call createTable().
     * Warning suppressed as it's a false positive.
     *
     * @throws SQLException Throws on SQL error.
     */
    @SuppressWarnings("PMD.CloseResource")
    private void ensureTable() throws SQLException {
        DatabaseMetaData dbm = this.conn.getMetaData();
        ResultSet tables = dbm.getTables(null, null, this.tableName, null);

        try {
            boolean createTable = tables.isAfterLast();

            if (createTable) {
                this.createTable();
                tables.close();
            }
        } catch (SQLException e) {
            tables.close();
        }
    }
}

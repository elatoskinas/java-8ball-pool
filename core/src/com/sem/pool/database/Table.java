package com.sem.pool.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Abstract class, to be implemented by database tables implementations.
 * Look at `./tables` for examples.
 */
public abstract class Table {
    /**
     * Connection to use for queries.
     */
    protected final transient Connection conn;

    /**
     * Create the table instance.
     *
     * @param conn The connection to use.
     */
    public Table(Connection conn) throws SQLException {
        this.conn = conn;

        this.ensureTable();
    }

    /**
     * Create the table.
     * Should be implemented by the extenders.
     */
    protected abstract void createTable() throws SQLException;

    /**
     * Name of the table.
     * @return the name of the table.
     */
    protected abstract String getTableName();

    /**
     * Ensure that the table exists.
     * If not call createTable().
     * Warning suppressed as it's a false positive.
     *
     * @throws SQLException Throws on SQL error.
     */
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    private void ensureTable() throws SQLException {
        DatabaseMetaData dbm = this.conn.getMetaData();

        try (ResultSet tables = dbm.getTables(null, null, this.getTableName(), null)) {
            if (tables.isAfterLast()) {
                this.createTable();
            }
        }
    }
}

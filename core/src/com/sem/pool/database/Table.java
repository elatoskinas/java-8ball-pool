package com.sem.pool.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Table {
    /**
     * Connection to use for queries.
     */
    protected Connection conn;
    /**
     * Name of the table.
     */
    protected String tableName;

    /**
     * Create the table instance.
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
     * @throws SQLException Throws on SQL error.
     */
    private void ensureTable() throws SQLException {
        DatabaseMetaData dbm = this.conn.getMetaData();
        ResultSet tables = dbm.getTables(null, null, this.tableName, null);

        if(!tables.next()) {
            System.out.println("Creating table for " + this.tableName + "..");
            this.createTable();
        }
    }
}

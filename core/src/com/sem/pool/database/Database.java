package com.sem.pool.database;

import com.sem.pool.database.tables.ResultTable;
import com.sem.pool.database.tables.UserTable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Handle the database.
 */
public class Database {
    /**
     * The database object singleton.
     */
    private static Database db;
    /**
     * The tables of the database.
     */
    private final transient HashMap<String, Table> tables;

    /**
     * Create the database.
     * Private to prevent multiple instances with a singleton.
     * CloseResource suppressed as the connection should never be closed.
     * DoNotCallSystemExit suppressed as this is a fatal error.
         *
     * @param inmemory True if the database should be in memory,
     *                 false if it should be written to a file.
     */
    @SuppressWarnings({"PMD.CloseResource", "PMD.DoNotCallSystemExit"})
    private Database(boolean inmemory) {
        this.tables = new HashMap<>();

        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn;

            if (inmemory) {
                conn = DriverManager.getConnection("jdbc:sqlite::memory:");
            } else {
                conn = DriverManager.getConnection("jdbc:sqlite:database.db");
            }

            // Add all tables.
            this.tables.put(UserTable.TABLE_NAME, new UserTable(conn));
            this.tables.put(ResultTable.TABLE_NAME, new ResultTable(conn));
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite driver not found, do you have it installed?");
            System.exit(1);
        }
    }

    /**
     * Get the database.
     *
     * @return The database instance.
     */
    public static Database getInstance() {
        if (Database.db == null) {
            Database.db = new Database(false);
        }

        return db;
    }

    /**
     * Set the database in test mode.
     * This means the data is stored in memory, not in a file.
     */
    public static void setTestMode() {
        Database.db = new Database(true);
    }

    /**
     * Get a table.
     *
     * @param table The table to get.
     * @return The table requested.
     */
    public Table table(String table) {
        return this.tables.get(table);
    }
}
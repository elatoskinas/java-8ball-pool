package com.sem.pool.database;

import com.sem.pool.database.tables.UserTable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class Database {
    /**
     * The database object singleton.
     */
    private static Database db;
    /**
     * The tables of the database.
     */
    private transient HashMap<String, Table> tables;

    /**
     * Create the database.
     * Private to prevent multiple instances with a singleton.
     *
     * CloseResource suppressed as the connection should never be closed.
     * DoNotCallSystemExit suppressed as this is a fatal error.
     */
    @SuppressWarnings({"PMD.CloseResource", "PMD.DoNotCallSystemExit"})
    private Database() {
        this.tables = new HashMap<String, Table>();

        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:database.db");

            // Add all tables.
            this.tables.put("User", new UserTable(conn));
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
     * @return The database instance.
     */
    public static Database getInstance( ) {
        if(Database.db == null) Database.db = new Database();

        return db;
    }

    /**
     * Get a table
     * @param table The table to get.
     * @return The table requested.
     */
    public Table table(String table) {
        return this.tables.get(table);
    }
}
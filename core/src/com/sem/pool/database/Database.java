package com.sem.pool.database;

import com.sem.pool.database.tables.UserTable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;

public class Database {
    /**
     * The database object singleton.
     */
    private static Database db;
    /**
     * The tables of the database.
     */
    private HashMap<String, Table> tables;

    /**
     * Create the database.
     * Private to prevent multiple instances with a singleton.
     */
    private Database() {
        this.tables = new HashMap<String, Table>();

        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:database.db");

            // Add all tables.
            this.tables.put("User", new UserTable(conn));
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
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
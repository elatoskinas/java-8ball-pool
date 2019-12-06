package com.sem.pool.database.controllers;

import com.sem.pool.database.Database;
import com.sem.pool.database.models.User;
import com.sem.pool.database.tables.UserTable;

import java.sql.SQLException;

/**
 * Controller of the User table.
 */
public class UserController {
    private transient UserTable table;

    /**
     * Constructor, supplies the database to use.
     *
     * @param db The database instance to use.
     */
    public UserController(Database db) {
        this.table = (UserTable) db.table("User");
    }

    /**
     * Try to log a user in.
     *
     * @param username The username of the user to log in.
     * @param password The password to check against.
     * @return Null if login failed. User object if it succeeded.
     */
    public User login(String username, String password) {
        try {
            User user = this.table.getUser(username);

            if (user == null) {
                return null;
            }

            if (user.checkPassword(password)) {
                return user;
            }
        } catch (SQLException ignored) {
            ignored.printStackTrace();
        }

        return null;
    }

    /**
     * Try to register a user.
     *
     * @param username The username of the user to create.
     * @param password The password associate with it.
     * @return Null if registration failed. User object if it succeeded.
     */
    public User register(String username, String password) {
        User user = new User(username, password);

        try {
            if (!this.table.save(user)) {
                return null;
            }

            return this.table.getUser(username);
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Check if a user exists.
     *
     * @param username The username of the user to check.
     * @return True if the user exists, false if it does not.
     */
    public boolean exists(String username) {
        try {
            User user = this.table.getUser(username);
            if (user != null) {
                return true;
            }
        } catch (SQLException e) {
            return false;
        }

        return false;
    }
}

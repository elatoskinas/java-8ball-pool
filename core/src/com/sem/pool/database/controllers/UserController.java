package com.sem.pool.database.controllers;

import com.sem.pool.database.Database;
import com.sem.pool.database.models.User;
import com.sem.pool.database.tables.UserTable;

import java.sql.SQLException;

/**
 * Controller of the User table.
 */
public class UserController {
    /**
     * Prevent anyone from creating an instance of this class, as that does not make sense.
     */
    private UserController() {}

    /**
     * Try to log a user in.
     * @param username The username of the user to log in.
     * @param password The password to check against.
     * @return Null if login failed. User object if it succeeded.
     */
    public static User login(String username, String password) {
        try {
            User user = UserController.table().getUser(username);

            if(user == null) {
                return null;
            }

            if(user.checkPassword(password)) {
                return user;
            }
        } catch (SQLException ignored) { }

        return null;
    }

    /**
     * Try to register a user.
     * @param username The username of the user to create.
     * @param password The password associate with it.
     * @return Null if registration failed. User object if it succeeded.
     */
    public static User register(String username, String password) {
        if(UserController.exists(username)) {
            return null;
        }

        User user = new User(username, password);

        try {
            if(!UserController.table().save(user)) {
                return null;
            }

            return UserController.table().getUser(username);
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Check if a user exists.
     * @param username The username of the user to check.
     * @return True if the user exists, false if it does not.
     */
    public static boolean exists(String username) {
        try {
            User user = UserController.table().getUser(username);
            if(user != null) {
                return true;
            }
        } catch (SQLException e) {
            return false;
        }

        return false;
    }

    /**
     * Get the table from the Database class.
     * @return The UserTable instance to work with.
     */
    private static UserTable table() {
        return (UserTable) Database.getInstance().table("User");
    }
}

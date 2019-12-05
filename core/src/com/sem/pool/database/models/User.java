package com.sem.pool.database.models;

import org.mindrot.jbcrypt.BCrypt;

/**
 * User object, from the database.
 */
public class User {
    private transient boolean existing;
    private transient int id;
    private String username;
    private String password;

    /**
     * Create a new user, without ID.
     * @param username The username to use.
     * @param password The password to use, this is hashed when initializing.
     */
    public User(String username, String password) {
        this.existing = false;
        this.username = username;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Create an existing user.
     * @param id The userID.
     * @param username The username to use.
     * @param password The password to use.
     */
    public User(int id, String username, String password) {
        this.existing = true;
        this.id = id;
        this.username = username;
        this.password = password;
    }

    /**
     * Check if the password is correct.
     * @param password The password to check.
     * @return If the password is correct.
     */
    public boolean checkPassword(String password) {
        return BCrypt.checkpw(password, this.password);
    }

    /**
     * If this is an existing user.
     * @return True if it's existing already.
     */
    public boolean isExisting() {
        return this.existing;
    }

    /**
     * Get the user id.
     * @return user id
     */
    public int getUserID() {
        return this.id;
    }

    /**
     * Get the username.
     * @return username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Get the password.
     * @return password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Set the user ID.
     * @param userID The userID
     */
    public void setUserID(int userID) {
        this.id = userID;
    }

    /**
     * Set the username.
     * @param username The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Set the password.
     * @param password The password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Convert this class to a string.
     * @return String representation of the class.
     */
    @Override
    public String toString() {
        return this.username;
    }
}

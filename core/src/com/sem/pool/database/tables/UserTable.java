package com.sem.pool.database.tables;

import com.sem.pool.database.Table;
import com.sem.pool.database.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Users table, for in the database.
 */
public class UserTable extends Table {
    /**
     * Create the new instance.
     *
     * @param conn Conection to use.
     */
    public UserTable(Connection conn) throws SQLException {
        super(conn, "User");
    }

    /**
     * Get a user by username.
     * Warning suppressed as it's a false positive.
     *
     * @param username Get the user by username.
     * @return A user object.
     * @throws SQLException SQL exceptions.
     */
    @SuppressWarnings("PMD.CloseResource")
    public User getUser(String username) throws SQLException {
        String sql = "select id, username, password from " + this.tableName + " where username = ?";
        PreparedStatement stmt = this.conn.prepareStatement(sql);
        stmt.setString(1, username);
        ResultSet res = stmt.executeQuery();

        if (res.isAfterLast()) {
            stmt.close();
            res.close();
            return null;
        }

        try {
            res.next();
            int id = res.getInt("id");
            String dataUser = res.getString("username");
            String pass = res.getString("password");
            User user = new User(id, dataUser, pass);

            stmt.close();
            res.close();
            return user;
        } catch (Exception e) {
            stmt.close();
            res.close();
            return null;
        }
    }

    /**
     * Save a user object.
     *
     * @param user The user to save.
     * @return The result.
     * @throws SQLException SQL errors.
     */
    public boolean save(User user) throws SQLException {
        if (user.isExisting()) {
            return false;
        }

        String sql = "insert into " + this.tableName + " (username, password) values (?, ?)";
        PreparedStatement stmt = this.conn.prepareStatement(sql);
        stmt.setString(1, user.getUsername());
        stmt.setString(2, user.getPassword());
        return stmt.execute();
    }

    /**
     * Update an exiting user object.
     *
     * @param user The user to update.
     * @return The result.
     * @throws SQLException SQL errors.
     */
    public boolean update(User user) throws SQLException {
        if (user.isExisting()) {
            return false;
        }

        String sql = "update " + this.tableName + " set (username = ?, password = ?) where id = ?";
        PreparedStatement stmt = this.conn.prepareStatement(sql);
        stmt.setString(1, user.getUsername());
        stmt.setString(2, user.getPassword());
        stmt.setInt(3, user.getUserID());
        return stmt.execute();
    }

    /**
     * Create the table.
     * This is only called if the table does not exist.
     *
     * @throws SQLException SQL Errors.
     */
    @Override
    protected void createTable() throws SQLException {
        Statement stmt = this.conn.createStatement();
        String query = "create table " + this.tableName + " ("
                + "   id       integer primary key autoincrement,"
                + "   username text    not null unique,"
                + "   password text    not null"
                + ")";

        // This comment is to ignore IntelIJ to complain about a "fix" which would make
        // PMD complain again.
        // noinspection TryFinallyCanBeTryWithResources
        try {
            stmt.execute(query);
        } finally {
            stmt.close();
        }
        stmt.close();
    }
}

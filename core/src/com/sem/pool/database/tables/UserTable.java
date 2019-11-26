package com.sem.pool.database.tables;

import com.sem.pool.database.Table;
import com.sem.pool.database.models.User;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.*;

public class UserTable extends Table {
    /**
     * Create the new instance.
     * @param conn Conection to use.
     */
    public UserTable(Connection conn) throws SQLException  {
        super(conn, "User");
    }

    /**
     * Get a user by username.
     * @param username Get the user by username.
     * @return A user object.
     * @throws SQLException
     */
    public User getUser(String username) throws SQLException {
        String SQL = "select id, username, password from " + this.tableName + " where username = ?";
        PreparedStatement stmt = this.conn.prepareStatement(SQL);
        stmt.setString(1, username);
        ResultSet res = stmt.executeQuery();

        if(res.isAfterLast()) return null;
        res.next();
        return new User(res.getInt("id"), res.getString("username"), res.getString("password"));
    }

    /**
     * Save a user object.
     * Updates if user.id is set.
     * @param user The user to save.
     * @return The result.
     * @throws SQLException SQL errors.
     */
    public boolean save(User user) throws SQLException {
        if(user.isExisting()) throw new NotImplementedException();

        String SQL = "insert into " + this.tableName + " (username, password) values (?, ?)";
        PreparedStatement stmt = this.conn.prepareStatement(SQL);
        stmt.setString(1, user.getUsername());
        stmt.setString(2, user.getPassword());
        return stmt.execute();
    }

    /**
     * Create the table.
     * This is only called if the table does not exist.
     * @throws SQLException SQL Errors.
     */
    @Override
    protected void createTable() throws SQLException {
        Statement stmt = this.conn.createStatement();
        String query =  "create table " + this.tableName + " (" +
                        "   id       integer primary key autoincrement," +
                        "   username text    not null unique," +
                        "   password text    not null" +
                        ")";
        stmt.executeUpdate(query);
        stmt.close();
    }
}

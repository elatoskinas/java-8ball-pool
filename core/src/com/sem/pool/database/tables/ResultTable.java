package com.sem.pool.database.tables;

import com.sem.pool.database.Database;
import com.sem.pool.database.Table;
import com.sem.pool.database.controllers.UserController;
import com.sem.pool.database.models.Result;
import com.sem.pool.database.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Users table, for in the database.
 */
public class ResultTable extends Table {
    public static final String TABLE_NAME = "Result";

    /**
     * Create the new instance.
     *
     * @param conn Conection to use.
     */
    public ResultTable(Connection conn) throws SQLException {
        super(conn);
    }

    /**
     * Get a list of all results.
     * @return An Arraylist of results.
     */
    public ArrayList<Result> getAll() throws SQLException {
        UserController userController = new UserController(Database.getInstance());
        String sql = "select gameId, winner, loser from Result";
        PreparedStatement stmt = this.conn.prepareStatement(sql);
        ArrayList<Result> results = new ArrayList<>();

        try (ResultSet res = stmt.executeQuery()) {
            if (res.isAfterLast()) {
                stmt.close();
                res.close();
                return results;
            }

            while(res.next()) {
                int id = res.getInt("gameId");
                int winnerId = res.getInt("winner");
                int loserId = res.getInt("loser");

                User winner = userController.getUser(winnerId);
                User loser = userController.getUser(loserId);

                results.add(new Result(id, winner, loser));
            }

            stmt.close();
            res.close();
            return results;
        }
    }

    /**
     * Save a Result.
     * @param result The result to save.
     * @return If the saving succeeded.
     * @throws SQLException SQL errors.
     */
    public boolean save(Result result) throws SQLException {
        String sql = "insert into Result (winner, loser) values (?, ?)";
        PreparedStatement stmt = this.conn.prepareStatement(sql);
        stmt.setInt(1, result.getWinner().getUserID());
        stmt.setInt(2, result.getLoser().getUserID());

        return stmt.executeUpdate() > 0;
    }

    /**
     * Create the table.
     * This is only called if the table does not exist.
     *
     * @throws SQLException SQL Errors.
     */
    @Override
    protected void createTable() throws SQLException {
        try (Statement stmt = this.conn.createStatement()) {
            String query = "create table " + this.getTableName() + " ("
                    + "   gameId    integer primary key autoincrement,"
                    + "   winner    integer not null,"
                    + "   loser     integer not null,"
                    + "   FOREIGN KEY(winner) REFERENCES User(id),"
                    + "   FOREIGN KEY(loser) REFERENCES User(id)"
                    + ")";
            stmt.execute(query);
        }
    }

    protected String getTableName() {
        return ResultTable.TABLE_NAME;
    }
}

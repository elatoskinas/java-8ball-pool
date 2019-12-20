package com.sem.pool.database.tables;

import com.sem.pool.database.Table;
import com.sem.pool.database.models.Result;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Users table, for in the database.
 */
public class ResultTable extends Table {
    /**
     * Create the new instance.
     *
     * @param conn Conection to use.
     */
    public ResultTable(Connection conn) throws SQLException {
        super(conn, "Result");
    }

    /**
     * Save a Result.
     *
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
        Statement stmt = this.conn.createStatement();
        String query = "create table " + this.tableName + " ("
                + "   gameId    integer primary key autoincrement,"
                + "   winner    integer not null,"
                + "   loser     integer not null,"
                + "   FOREIGN KEY(winner) REFERENCES User(id),"
                + "   FOREIGN KEY(loser) REFERENCES User(id)"
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

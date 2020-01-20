package com.sem.pool.database.controllers;

import com.sem.pool.database.Database;
import com.sem.pool.database.models.Result;
import com.sem.pool.database.models.User;
import com.sem.pool.database.tables.ResultTable;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Controller of the Result table.
 */
public class ResultController {
    private transient ResultTable table;

    /**
     * Constructor, supplies the database to use.
     *
     * @param db The database instance to use.
     */
    public ResultController(Database db) {
        this.table = (ResultTable) db.table(ResultTable.TABLE_NAME);
    }

    /**
     * Save a result.
     * @param winner Winner of the game.
     * @param loser Loser of the game.
     * @return If the storing of the result succeeded.
     */
    public boolean createResult(User winner, User loser) {
        Result result = new Result(winner, loser);

        try {
            return this.table.save(result);
        } catch (SQLException stack) {
            stack.printStackTrace();
            return false;
        }
    }

    /**
     * Get a list of results.
     */
    public ArrayList<Result> getAll() {
        try {
            return this.table.getAll();
        } catch (SQLException stack) {
            stack.printStackTrace();
            return new ArrayList<>();
        }
    }
}

package com.sem.pool.database.controllers;

import com.sem.pool.database.Database;
import com.sem.pool.database.tables.ResultTable;

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
        this.table = (ResultTable) db.table("Result");
    }
}

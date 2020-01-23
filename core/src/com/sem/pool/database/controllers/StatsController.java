package com.sem.pool.database.controllers;

import com.sem.pool.database.Database;
import com.sem.pool.database.models.Result;
import com.sem.pool.database.models.Stats;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Controller of the Stats virtual table.
 */
public class StatsController {
    private transient ResultController resultController;

    /**
     * Create a new statistics database.
     * @param db The database class to use.
     */
    public StatsController(Database db) {
        this.resultController = new ResultController(db);
    }

    /**
     * Get the top statistics.
     * Warnings suppressed as this is an known bug within PMD.
     * @return A list of statistics, ordered in descending order.
     */
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public ArrayList<Stats> getTop() {
        ArrayList<Stats> statsArray = this.getStats();

        statsArray.sort((stats1, stats2) -> {
            float difference = stats1.getWinLossRatio() - stats2.getWinLossRatio();

            if (difference == 0) {
                return 0;
            }

            return difference < 0 ? 1 : -1;
        });

        return statsArray;
    }

    /**
     * Get a list of all stats.
     * This is equal to the `.getTop()` method, except the list is not sorted.
     * Warnings suppressed as this is an known bug within PMD.
     * @return A unordered statistics list.
     */
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    private ArrayList<Stats> getStats() {
        ArrayList<Result> results = this.resultController.getAll();
        HashMap<Integer, Stats> stats = new HashMap<>();

        for (Result res : results) {
            if (!stats.containsKey(res.getWinner().getUserID())) {
                stats.put(res.getWinner().getUserID(), new Stats(res.getWinner()));
            }

            if (!stats.containsKey(res.getLoser().getUserID())) {
                stats.put(res.getLoser().getUserID(), new Stats(res.getLoser()));
            }

            stats.get(res.getWinner().getUserID()).addResult(res);
            stats.get(res.getLoser().getUserID()).addResult(res);
        }

        return new ArrayList<>(stats.values());
    }
}

package com.sem.pool.database.models;

import java.util.ArrayList;

public class Stats {
    private User user;
    private ArrayList<Result> results;
    private ArrayList<Result> wins;

    public Stats(User user) {
        this.user = user;

        this.results = new ArrayList<>();
        this.wins = new ArrayList<>();
    }

    public void addResult(Result result) {
        this.results.add(result);

        if(result.getWinner().equals(this.user)) {
            this.wins.add(result);
        }
    }

    public float getWL() {
        if (this.results.size() == 0) {
            return 0;
        }

        return (float) this.wins.size() / this.results.size();
    }

    public int getGameCount() {
        return this.results.size();
    }

    public User getUser() {
        return this.user;
    }
}

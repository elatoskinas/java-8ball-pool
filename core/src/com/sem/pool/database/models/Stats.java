package com.sem.pool.database.models;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Statistic model. This is a virtual table derived from the Result and User table.
 */
public class Stats {
    private transient User user;
    private transient ArrayList<Result> results;
    private transient ArrayList<Result> wins;

    /**
     * Create a new stats object.
     * @param user The user this object is about.
     */
    public Stats(User user) {
        this.user = user;

        this.results = new ArrayList<>();
        this.wins = new ArrayList<>();
    }

    /**
     * Add a result to the statistics.
     * This is not saved to the database.
     * @param result The result to add.
     */
    public void addResult(Result result) {
        this.results.add(result);

        if (result.getWinner().equals(this.user)) {
            this.wins.add(result);
        }
    }

    /**
     * Get the win / lose ratio of this player.
     * @return THe win / lose ratio.
     */
    public float getWinLossRatio() {
        if (this.results.size() == 0) {
            return 0;
        }

        return (float) this.wins.size() / this.results.size();
    }

    /**
     * Get the amount of games this player played.
     * @return The games count.
     */
    public int getGameCount() {
        return this.results.size();
    }

    public User getUser() {
        return this.user;
    }

    /**
     * Implement the equals method.
     * @param object The object to compare against.
     * @return If the object was equals to this.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Stats)) {
            return false;
        }

        Stats other = (Stats) object;

        if (!this.results.equals(other.results)) {
            return false;
        } else if (!this.user.equals(other.user)) {
            return false;
        }

        return this.wins.equals(other.wins);
    }

    /**
     * Override the hashcode.
     * @return The hashcode of this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.user, this.results, this.wins);
    }
}

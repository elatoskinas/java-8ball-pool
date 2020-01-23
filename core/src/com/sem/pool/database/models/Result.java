package com.sem.pool.database.models;

import java.util.Objects;

/**
 * Result object, from the database.
 */
public class Result {
    private transient int gameId;
    private transient User winner;
    private transient User loser;

    /**
     * Create a new result.
     * @param gameId The ID of the game.
     * @param winner The winner of the game.
     * @param loser The loser of the game.
     */
    public Result(int gameId, User winner, User loser) {
        this.gameId = gameId;
        this.winner = winner;
        this.loser = loser;
    }

    /**
     * Create a new result without the id.
     * @param winner The winner of the game.
     * @param loser The loser of the game.
     */
    public Result(User winner, User loser) {
        this.winner = winner;
        this.loser = loser;
    }

    public int getGameID() {
        return this.gameId;
    }

    public User getWinner() {
        return this.winner;
    }

    public User getLoser() {
        return this.loser;
    }

    /**
     * Override the equals method.
     * @param object Object to test against.
     * @return True iff they are equal
     */
    public boolean equals(Object object) {
        if (!(object instanceof Result)) {
            return false;
        }

        Result other = (Result) object;

        if (!this.winner.equals(other.winner)) {
            return false;
        }

        return this.loser.equals(other.loser);
    }

    /**
     * Override the hashcode.
     * @return The hashcode of this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.winner, this.loser);
    }
}

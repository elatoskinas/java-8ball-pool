package com.sem.pool.screens;

import com.badlogic.gdx.Game;
import com.sem.pool.database.models.User;

/**
 * Game class.
 * This handles the switching between screens.
 */
public class MainGame extends Game {
    private User player;
    private User opponent;
    private User winner;

    /**
     * Create the game.
     */
    public void create() {
        this.setScreen(new Login(this));
    }

    /**
     * Reset the game.
     */
    public void reset() {
        this.player = null;
        this.opponent = null;
        this.winner = null;
    }

    public User getPlayer() {
        return this.player;
    }

    public User getOpponent() {
        return this.opponent;
    }

    public User getWinner() {
        return this.winner;
    }

    public void setPlayer(User player) {
        this.player = player;
    }

    public void setOpponent(User opponent) {
        this.opponent = opponent;
    }

    public void setWinner(User winner) {
        this.winner = winner;
    }
}

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

    /**
     * Create the game.
     */
    public void create() {
        this.setScreen(new Login(this));
    }

    public User getPlayer() {
        return this.player;
    }

    public User getOpponent() {
        return this.opponent;
    }

    public void setPlayer(User player) {
        this.player = player;
    }

    public void setOpponent(User opponent) {
        this.opponent = opponent;
    }
}

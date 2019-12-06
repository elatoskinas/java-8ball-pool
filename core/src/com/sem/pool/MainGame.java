package com.sem.pool;

import com.badlogic.gdx.Game;

/**
 * Game class.
 * This handles the switching between screens.
 */
public class MainGame extends Game {
    /**
     * Create the game.
     */
    public void create() {
        this.setScreen(new Login(this));
    }

    /**
     * Start the pool game.
     */
    public void startPool() {
        this.setScreen(new Pool());
    }
}

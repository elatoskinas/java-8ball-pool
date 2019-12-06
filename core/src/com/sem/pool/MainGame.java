package com.sem.pool;

import com.badlogic.gdx.Game;

/**
 * Login game screen.
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

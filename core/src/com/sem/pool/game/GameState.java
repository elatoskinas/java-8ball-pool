package com.sem.pool.game;

import com.sem.pool.scene.Ball3D;

import java.util.List;
import java.util.Set;

/**
 * Class to keep track of the current state of the game with regards to the rules.
 */
public class GameState {
    private List<Player> players;
    private Set<Ball3D> remainingBalls;
    private int playerTurn;
    private int turnCount;

    private transient boolean started;

    /**
     * Creates a new GameState object.
     */
    public GameState() {
    }

    public boolean isStarted() {
        return started;
    }

    /**
     * Starts the pool game by picking a random Player
     * for the break shot.
     */
    public void startGame() {
        // TODO: Implement random player picking
        this.started = true;
    }

    /**
     * Advances the turn of the game, ending the current Player's
     * turn and starting the subsequent Player's turn.
     */
    public void advanceTurn() {

    }

    /**
     * Ends the game with the specified Player ID to be marked
     * as the winner.
     * @param winnerId  ID of the winning player (0-baseed)
     */
    public void winGame(int winnerId) {

    }

    /**
     * Handles ball potting of the specified ball, including
     * special cases on potting the cue and 8-ball, which might
     * result in the victory or loss of the game.
     * @param ball  Ball to pot
     */
    public void onBallPotted(Ball3D ball) {
        
    }
}

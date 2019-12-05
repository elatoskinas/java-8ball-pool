package com.sem.pool.game;

/**
 * Interface for observing GameState classes.
 * Contains functionality necessary to respond to
 * victory (end) of games.
 * TODO: Can extend this with potting events and so on.
 */
public interface GameStateObserver {
    /**
     * Ends the game with the specified Player as the winner.
     * @param winner  The player that won
     */
    public void endGame(Player winner);
}

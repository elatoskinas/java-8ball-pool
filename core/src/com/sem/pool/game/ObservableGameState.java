package com.sem.pool.game;

public interface ObservableGameState {
    /**
     * Makes the specified GameStateObserver now observe ObservableGameState;
     * Further events coming from the Game State will be propagated to the observer.
     * @param observer  Observer to add
     */
    public void addObserver(GameStateObserver observer);

    /**
     * Removes the specified GameStateObserver, and makes it no longer
     * observe the game state.
     * Further events coming from the Game State will not propagate to the removed
     * observer.
     * @param observer  Observer to remove
     */
    public void removeObserver(GameStateObserver observer);

    /**
     * Ends the game with the specified Player as the winner.
     * @param winner  Winning Player
     */
    public void endGame(Player winner);
}

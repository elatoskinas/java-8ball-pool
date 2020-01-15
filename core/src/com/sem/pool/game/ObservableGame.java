package com.sem.pool.game;

import com.sem.pool.scene.Ball3D;

import java.util.Collection;

/**
 * Interface that represents a Game that can be observed by multiple
 * Game Observers. Contains methods relevant for handling observers,
 * and for triggering events that would be interesting to the observers.
 */
public interface ObservableGame {
    /**
     * Adds the specified observer to the observable Game.
     * @param observer  Game Observer to add
     */
    public void addObserver(GameObserver observer);

    /**
     * Removes the specified observer from the observable Game.
     * @param observer  Game Observer to remove
     */
    public void removeObserver(GameObserver observer);

    /**
     * Gets all observers as an abstract collection.
     * @return  Observers of the Observable Game
     */
    public Collection<GameObserver> getObservers();

    /**
     * Starts the game.
     */
    public void startGame();

    /**
     * Sets the game in motion.
     */
    public void startMotion();

    /**
     * Stops the motion of the game with the specified
     * ball as the ball that was touched first in
     * the current loop interval that was just stopped.
     *
     * @param firstTouched  Ball that was touched first by a cue ball
     *                      in current loop interval.
     */
    public void stopMotion(Ball3D firstTouched);

    /**
     * Ends the game.
     */
    public void endGame();
}

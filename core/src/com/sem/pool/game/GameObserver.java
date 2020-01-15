package com.sem.pool.game;

import com.sem.pool.scene.Ball3D;

/**
 * Interface that represents an observer of an ObservableGame.
 * Contains methods to react to events posted by an ObservableGame class.
 */
public interface GameObserver {
    /**
     * Event that is triggered when the observed Game
     * is started.
     */
    public void onGameStarted();

    /**
     * Event that is triggered when a ball is potted
     * in the observed Game.
     * @param ball  Ball that has been potted
     */
    public void onBallPotted(Ball3D ball);

    /**
     * Event that is triggered when the observed Game
     * becomes in motion.
     */
    public void onMotion();

    /**
     * Event that is triggered when the observed Game
     * stops being in motion.
     *
     * @param firstTouched  Fist ball that was touched by the Cue Ball
     *                      in current loop interval that was just stopped.
     */
    public void onMotionStop(Ball3D firstTouched);

    /**
     * Event that is triggered when the observed Game ends.
     */
    public void onGameEnded();
}

package com.sem.pool.game;

import com.badlogic.gdx.Input;
import com.sem.pool.scene.Ball3D;
import com.sem.pool.scene.Scene3D;

/**
 * Class that handles everything related to the pool game.
 * TODO: This is currently only a template, no functionality has been implemented as of yet.
 * TODO: Remove PMD suppressions for avoid duplicate literals; These were added for TODO methods.
 */
public class Game {
    private transient Scene3D scene;
    private transient Input input;
    private transient GameState state;

    // Boolean to keep track whether balls are in motion;
    // If that is the case, we will ignore user input.
    private transient boolean inMotion;

    private transient boolean started;

    /**
     * Constructs a new Game object with the given scene, input, and state.
     * @param scene The scene of the game.
     * @param input The input the game should listen to.
     * @param state The state of the game.
     */
    public Game(Scene3D scene, Input input, GameState state) {
        this.scene = scene;
        this.input = input;
        this.state = state;
        this.started = false;
    }

    public Scene3D getScene() {
        return scene;
    }

    public Input getInput() {
        return input;
    }

    public GameState getState() {
        return state;
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isInMotion() {
        return inMotion;
    }

    /**
     * Starts the game and takes care of starting the
     * Game State as well.
     */
    public void startGame() {
        state.startGame();
        this.started = true;
    }

    /**
     * Publicly accessible method call that handles all of the
     * logic for the current game loop iteration, such as
     * moving the balls, responding to input, and ending
     * the current turn.
     */
    public void advanceGameLoop() {
        if (started) {
            // If no balls are in motion, that means
            // we are at the phase where we can respond to input.
            // Otherwise, we need to move the balls.
            if (!inMotion) {
                respondToInput();
                // TODO: Advance turns:
                //       Implement state transition that goes from in motion to not in-motion
            } else {
                moveBalls();
            }
        } // Do nothing if game is not started
    }

    /**
     * Moves the balls according to our physics implementation. This method will be called
     * every frame in the render() method.
     */
    @SuppressWarnings("PMD.AvoidDuplicateLiterals")
    protected void moveBalls() {
        // Move all the balls in the scene, regardless of whether
        // they are in motion or not. Here, we assume that the ball
        // is simply not moved if it is not in motion (via internal logic of ball)
        for (Ball3D ball : scene.getPoolBalls()) {
            ball.move();
        }

        // TODO: Handle calling pot balls methods

        // TODO: Handle collisions here?

        // TODO: Need to stop balls after some point so that inMotion becomes false
        //       Otherwise we will end up in an infinite movement loop.
    }

    /**
     * Method to handle any input by the player(s), should ignore input if invalid.
     */
    @SuppressWarnings("PMD.AvoidDuplicateLiterals")
    protected void respondToInput() {
        // TODO: Implement logic to respond to input every frame
        // TODO: Call performCueShot on input
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    /**
     * Determines if there is at least one ball that is currently
     * in motion. To be used as a helper for the game loop in
     * ending a movement loop iteration.
     * @return  True if at least one ball is in motion, and false otherwise.
     */
    protected boolean determineIsInMotion() {
        // Check all the balls currently in the scene
        for (Ball3D ball : scene.getPoolBalls()) {
            // Found one that is in motion;
            // Game should also be in motion.
            if (ball.isInMotion()) {
                return true;
            }
        }

        // No ball in motion; Game should not be in motion either.
        return false;
    }

    /**
     * Pots the specified ball in the Pool Game.
     * This makes the specified ball disappear from the table,
     * and updates the Game State to score the potting for the
     * active Player.
     * @param ball  Ball to be potted
     */
    @SuppressWarnings("PMD.AvoidDuplicateLiterals")
    public void potBall(Ball3D ball) {
        // TODO: Implement ball potting logic (3D)
        // TODO: Implement ball potting logic (Game State)
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    /**
     * Performs the cue shot by firing the cue ball with the
     * cue's current power and rotation.
     */
    @SuppressWarnings("PMD.AvoidDuplicateLiterals")
    private void performCueShot() {
        // TODO: Perform cue shot
        throw new UnsupportedOperationException("Not yet implemented!");
    }
}

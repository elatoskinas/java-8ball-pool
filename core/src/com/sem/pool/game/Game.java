package com.sem.pool.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.sem.pool.scene.Ball3D;
import com.sem.pool.scene.Cue3D;
import com.sem.pool.scene.CueBall3D;
import com.sem.pool.scene.Scene3D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class that handles everything related to the pool game.
 * PMD warning suppressed, as it is caused by another bug within PMD.
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class Game implements ObservableGame {
    private transient Scene3D scene;
    private transient Input input;
    private transient GameState state;
    private transient Set<GameObserver> observers;

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
        this.observers = new HashSet<>();
        // Add State as an observer to the game
        // NOTE: Since the Game State is an observer,
        // it will react to al the required functionality for
        // starting the game, potting balls and reacting to motion.
        this.observers.add(state);
    }

    /**
     * Creates a new Game instance with the given scene and input objects.
     * @param scene  Scene to use for the Game
     * @param input  Input handler to use for the Game
     * @return New Game instance
     */
    public static Game createNewGame(Scene3D scene, Input input, ArrayList<Player> players) {
        // Create game state with the scene's pool balls & the two players
        GameState gameState = new GameState(players, scene.getPoolBalls());

        // Create a Game object from the parameters
        return new Game(scene, input, gameState);
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

    /**
     * Publicly accessible method call that handles all of the
     * logic for the current game loop iteration, such as
     * moving the balls, responding to input, and ending
     * the current turn.
     * @param deltaTime deltaTime, time between current and last frame.
     */
    public void advanceGameLoop(float deltaTime) {
        if (!state.isStarted()) {
            // Do nothing if game is not started
            return;
        }

        // Check if Game has a winning Player
        if (state.getWinningPlayer().isPresent()) {
            endGame(this.state.getWinningPlayer().get(),
                    this.state.getTurnHandler().getPlayers());
        } else {
            // Check if any ball is in motion
            determineIsInMotion();
            performGameLoopAction(deltaTime);
        }
    }

    /**
     * Performs an action in the game loop based on the
     * internal state of the game.
     * This could be moving the balls or responding
     * to user input.
     * @param deltaTime  deltaTime, time between current and last frame.
     */
    private void performGameLoopAction(float deltaTime) {
        if (state.isInMotion()) {
            moveBalls(deltaTime);
        } else if (state.isIdle()) {
            respondToInput();
        }
    }

    /**
     * Moves the balls according to our physics implementation. This method will be called
     * every frame in the render() method.
     */
    // Seems like there is a false positive with regards to UR anomalies that
    // is caused by the loop.
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    protected void moveBalls(float deltaTime) {
        // Move all the balls in the scene, regardless of whether
        // they are in motion or not. Here, we assume that the ball
        // is simply not moved if it is not in motion (via internal logic of ball)
        for (Ball3D ball : scene.getPoolBalls()) {
            ball.move(deltaTime);
        }

        // Check collisions for current game loop iteration
        List<Ball3D> potted = scene.triggerCollisions();

        // Pot the ball for every ball that was determined to be potted
        // by the scene
        for (Ball3D ball : potted) {
            potBall(ball);
        }
    }

    /**
     * Method to handle any input by the player(s), should ignore input if invalid.
     */
    protected void respondToInput() {
        if (this.state.isCueBallPotted()) {
            handleCueBallPlacement();
        } else {
            processCueInput();
        }
        
    }

    /**
     * Method to handle the replacement of the cue ball when necessary.
     */
    protected void handleCueBallPlacement() {
        // The placeCueBall method returns true iff the cue ball was placed successfully,
        // if this is the case, the cueBallPotted variable will be set to false.
        if (this.scene.placeCueBall(input)) {
            this.state.getGameBallState().resetCueBall();
        }
    }
    
    /**
     * Process the input mouse input for the cue.
     */
    protected void processCueInput() {
        Cue3D cue = scene.getCue();

        if (cue.getState() == Cue3D.State.Hidden) {
            cue.showCue();
        }

        if (input.isButtonPressed(Input.Buttons.LEFT)) {
            Vector3 mousePosition = scene.getUnprojectedMousePosition();

            // Enter dragging
            if (cue.getState() == Cue3D.State.Rotating) {
                cue.setToDragging(mousePosition);
            }

            CueBall3D cueBall = scene.getCueBall();
            cue.toDragPosition(mousePosition, cueBall);

        } else if (cue.getState() == Cue3D.State.Dragging) {
            if (cue.getCurrentForce() > 0) {
                startMotion();

                CueBall3D cueBall = scene.getCueBall();
                cue.shoot(cueBall);
                scene.getSoundPlayer().playCueSound();
            } else {
                // Cancel shot -> go back to rotating
                cue.setToRotating();
            }
        }

        if (cue.getState() == Cue3D.State.Rotating) {
            Vector3 mousePosition = scene.getUnprojectedMousePosition();
            CueBall3D cueBall = scene.getCueBall();
            cue.toPosition(mousePosition, cueBall);
        }
    }

    /**
     * Determines if there is at least one ball that is currently
     * in motion. To be used as a helper for the game loop in
     * ending a movement loop iteration.
     * @return  True if at least one ball is in motion, and false otherwise.
     */
    // Seems like there is a false positive with regards to UR anomalies that
    // is caused by the loop.
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
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
        // If no balls are in motion, that means
        // we are at the phase where we can respond to input.
        // Otherwise, we need to move the balls.
        if (state.isInMotion()) {
            // Get the ball that was touched first
            Ball3D touched = scene.getFirstTouched();
            scene.clearFirstTouched();

            stopMotion(touched);
        }

        return false;
    }

    /**
     * Pots the specified ball in the Pool Game.
     * This makes the specified ball disappear from the table,
     * and updates the Game State to score the potting for the
     * active Player.
     * @param ball  Ball to be potted
     */
    // False positive in the foreach loop with regards to variable 'o'.
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public void potBall(Ball3D ball) {
        // Pot the ball (handles potting the ball visually)
        ball.pot();

        // Notify all observers of the potted ball
        for (GameObserver o : observers) {
            o.onBallPotted(ball);
        }
    }

    @Override
    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    /**
     * End the game.
     * Error suppressed as this is a bug in PMD.
     * @param winner The winner of the game.
     * @param players All players in the game.
     */
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public void endGame(Player winner, List<Player> players) {
        for (GameObserver observer : this.observers) {
            observer.onGameEnded(winner, players);
        }
    }
    
    public Collection<GameObserver> getObservers() {
        return observers;
    }

    @Override
    public void startGame() {
        observers.forEach(GameObserver::onGameStarted);
    }

    @Override
    public void startMotion() {
        observers.forEach(GameObserver::onMotion);
    }

    @Override
    public void stopMotion(Ball3D touched) {
        observers.forEach(x -> x.onMotionStop(touched));
    }
}

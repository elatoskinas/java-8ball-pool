package com.sem.pool.game;

import com.sem.pool.scene.Ball3D;

import java.util.List;
import java.util.Optional;

/**
 * Class to keep track of the current state of the game with regards to the rules.
 * GameState can be observed by implementing the GameStateObserver interface.
 * The methods that u
 * pdate observers are WinGame.
 * TODO: Remove PMD suppressions for avoid duplicate literals; These were added for TODO methods.
 */
// We had to suppress DataflowAnomalyAnalysis warnings 4 times due to bugs in PMD
// Because of this, PMD started complaining again.
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class GameState implements GameObserver {
    private transient Player winningPlayer; // Winning Player

    public enum State {
        Stopped,
        Idle,
        InMotion,
        Ended
    }

    private transient State state;
    private transient GameBallState gameBallState;
    private transient TurnHandler turnHandler;
    private transient BallPottingHandler ballPottingHandler;

    /**
     * Creates a new game state with the specified Players and
     * the specified Pool balls.
     * @param players     List of Players for the game
     * @param poolBalls   List of pool balls to use for the game
     */
    public GameState(List<Player> players, List<Ball3D> poolBalls) {
        this.state = State.Stopped;
        this.gameBallState = new GameBallState(poolBalls);
        this.turnHandler = new TurnHandler(players);
        this.ballPottingHandler = new BallPottingHandler(gameBallState);
    }

    public boolean isStarted() {
        return state != State.Stopped && state != State.Ended;
    }

    public boolean isInMotion() {
        return state == State.InMotion;
    }

    public boolean isIdle() {
        return state == State.Idle;
    }

    public boolean isStopped() {
        return !isStarted();
    }

    public GameBallState getGameBallState() {
        return this.gameBallState;
    }

    public TurnHandler getTurnHandler() {
        return this.turnHandler;
    }

    public BallPottingHandler getBallPottingHandler() {
        return this.ballPottingHandler;
    }

    /**
     * Starts the pool game by picking a random Player
     * for the break shot.
     */
    public void onGameStarted() {
        turnHandler.initializeStartingPlayer();
        this.state = State.Idle;
    }

    /**
     * Advances the turn of the game, ending the current Player's
     * turn and starting the subsequent Player's turn.
     */
    public void advanceTurn() {
        Optional<Boolean> outcome = ballPottingHandler.handleBallPotting(turnHandler);

        if (outcome.isPresent()) {
            winGame(outcome.get());
        } else {
            turnHandler.advanceTurn(gameBallState);
        }

        state = State.Idle;
    }

    //    /**
    //     * Ends the game with the specified Player ID to be marked
    //     * as the winner.
    //     * Notifies the observers of the won game.
    //     * @param winner  Winning Player object
    //     */
    //    // False positive on Dataflow Anomaly for the observer
    //    // loop in the method. Also false positive for the winningPlayer.
    //    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    //    public void winGame(Player winner) {
    //        // Stop the game
    //        state = State.Stopped;
    //    }

    /**
     * Determines the winner of the game & updates the internal
     * state to "Won".
     *
     * @param won  True if the current player won
     */
    public void winGame(boolean won) {
        winningPlayer = won ? turnHandler.getActivePlayer() : turnHandler.getNextInactivePlayer();
    }

    @Override
    public void onMotion() {
        this.state = State.InMotion;
    }

    @Override
    public void onMotionStop(Ball3D touched) {
        gameBallState.setFirstBallTouched(touched);
        advanceTurn();
    }

    @Override
    public void onGameEnded(Player winner, List<Player> players) {
        this.state = State.Ended;
    }

    /**
     * Pots the specified ball for the current turn of the Game State.
     * @param ball  Ball to pot
     */
    @Override
    public void onBallPotted(Ball3D ball) {
        // Pot ball in current turn
        gameBallState.addPottedBall(ball);
    }

    /**
     * Returns an object representing the winning Player.
     * If there is no winner yet, the returned Optional object
     * is empty.
     * @return  Optional object holding the winner.
     */
    public Optional<Player> getWinningPlayer() {
        return Optional.ofNullable(winningPlayer);
    }

    public boolean isCueBallPotted() {
        return gameBallState.isCueBallPotted();
    }
}

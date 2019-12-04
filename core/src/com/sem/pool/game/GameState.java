package com.sem.pool.game;

import com.sem.pool.scene.Ball3D;
import com.sem.pool.scene.CueBall3D;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class to keep track of the current state of the game with regards to the rules.
 * GameState can be observed by implementing the GameStateObserver interface.
 * The methods that update observers are WinGame.
 * TODO: Remove PMD suppressions for avoid duplicate literals; These were added for TODO methods.
 */
public class GameState {
    private transient List<Player> players;
    private transient Set<Ball3D> remainingBalls;
    private transient int playerTurn;
    private transient int turnCount;

    private transient boolean started;

    private transient Set<GameStateObserver> observers;

    /**
     * Creates a new game state with the specified Players and
     * the specified Pool balls.
     * @param players     List of Players for the game
     * @param poolBalls   List of pool balls to use for the game
     */
    public GameState(List<Player> players, List<Ball3D> poolBalls) {
        this.players = players;
        this.remainingBalls = new HashSet<>();
        this.observers = new HashSet<>();

        // Add all pool balls except cue ball to remaining balls set
        for (Ball3D ball : poolBalls) {
            if (!(ball instanceof CueBall3D)) {
                remainingBalls.add(ball);
            }
        }
    }

    public boolean isStarted() {
        return started;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Set<Ball3D> getRemainingBalls() {
        return remainingBalls;
    }

    public Set<GameStateObserver> getObservers() {
        return observers;
    }

    public void addObserver(GameStateObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(GameStateObserver observer) {
        observers.remove(observer);
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
    @SuppressWarnings("PMD.AvoidDuplicateLiterals")
    public void advanceTurn() {
        // TODO: Turn should be advanced
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    /**
     * Ends the game with the specified Player ID to be marked
     * as the winner.
     * Notifies the observers of the won game.
     * @param winnerId  ID of the winning player (0-baseed)
     */
    // False positive on Dataflow Anomaly for the observer
    // loop in the method. Also false positive for the winningPlayer.
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public void winGame(int winnerId) {
        Player winningPlayer = players.get(winnerId);

        // Notify the observers of the victory
        for (GameStateObserver observer : observers) {
            observer.endGame(winningPlayer);
        }

        // Stop the game
        started = false;
    }

    /**
     * Handles ball potting of the specified ball, including
     * special cases on potting the cue and 8-ball, which might
     * result in the victory or loss of the game.
     * @param ball  Ball to pot
     */
    @SuppressWarnings("PMD.AvoidDuplicateLiterals")
    public void onBallPotted(Ball3D ball) {
        // TODO: Ball should be potted and tracked for the Player
        // TODO: Should handle dispatching events back to Game
        throw new UnsupportedOperationException("Not yet implemented!");
    }
}

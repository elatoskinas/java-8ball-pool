package com.sem.pool.game;

import com.sem.pool.scene.Ball3D;
import com.sem.pool.scene.CueBall3D;
import com.sem.pool.scene.EightBall3D;
import com.sem.pool.scene.RegularBall3D;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Class to keep track of the current state of the game with regards to the rules.
 * GameState can be observed by implementing the GameStateObserver interface.
 * The methods that u
 * pdate observers are WinGame.
 * TODO: Remove PMD suppressions for avoid duplicate literals; These were added for TODO methods.
 */
public class GameState implements GameObserver {
    private transient List<Player> players;
    private transient Set<Ball3D> remainingBalls;
    private transient List<Ball3D> currentPottedBalls; // Balls potted in current turn
    private transient List<Ball3D> allPottedBalls; // All Balls potted in any turn.

    private transient int playerTurn;
    private transient int turnCount;
    private transient boolean typesAssigned; // if ball types have been assigned yet

    private transient Player winningPlayer;

    public enum State {
        Stopped,
        Idle,
        InMotion,
        Ended
    }

    private transient State state;

    /**
     * Creates a new game state with the specified Players and
     * the specified Pool balls.
     * @param players     List of Players for the game
     * @param poolBalls   List of pool balls to use for the game
     */
    public GameState(List<Player> players, List<Ball3D> poolBalls) {
        this.state = State.Stopped;
        this.players = players;
        this.remainingBalls = new HashSet<>();
        this.currentPottedBalls = new ArrayList<>();
        this.allPottedBalls = new ArrayList<>();
        this.typesAssigned = false;
        // Add all pool balls except cue ball to remaining balls set
        for (Ball3D ball : poolBalls) {
            if (!(ball instanceof CueBall3D)) {
                remainingBalls.add(ball);
            }
        }
    }

    public boolean isStarted() {
        return state != State.Stopped && state != State.Ended;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Set<Ball3D> getRemainingBalls() {
        return remainingBalls;
    }

    public List<Ball3D> getCurrentPottedBalls() {
        return currentPottedBalls;
    }

    public void setInMotion() {
        this.state = State.InMotion;
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

    public int getPlayerTurn() {
        return playerTurn;
    }

    /**
     * Gets the active player.
     * @return active player
     */
    public Player getActivePlayer() {
        return players.get(playerTurn);
    }

    /**
     * Gets the next inactive player.
     * @return an inactive player
     */
    public Player getNextInactivePlayer() {
        return players.get((playerTurn + 1) % players.size());
    }

    /**
     * Starts the pool game by picking a random Player
     * for the break shot.
     */
    public void onGameStarted() {
        initStartingPlayer();

        this.state = State.Idle;
    }

    /**
     * Initializes the starting player at random.
     */
    public void initStartingPlayer() {
        playerTurn = (int) Math.round(Math.random());
    }

    /**
     * Advances the turn of the game, ending the current Player's
     * turn and starting the subsequent Player's turn.
     */
    public void advanceTurn() {
        handleBallPotting();

        // Increment player turn and wrap turn ID around
        // players size to keep it within bounds
        playerTurn = (playerTurn + 1) % players.size();

        state = State.Idle;
        turnCount += 1;
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
     * @param allPotted  True if the current Player had all of their balls potted.
     */
    public void winGame(boolean allPotted) {
        if (allPotted) {
            // All balls + 8-ball potted; Active player wins.
            winningPlayer = getActivePlayer();
        } else {
            // Not all balls potted; Other Player wins.
            winningPlayer = getNextInactivePlayer();
        }
    }

    @Override
    public void onMotion() {
        this.state = State.InMotion;
    }

    @Override
    public void onMotionStop() {
        advanceTurn();
    }

    @Override
    public void onGameEnded() {
        this.state = State.Ended;
    }

    public boolean getTypesAssigned() {
        return typesAssigned;
    }

    public List<Ball3D> getAllPottedBalls() {
        return allPottedBalls;
    }

    /**
     * Pots the specified ball for the current turn of the Game State.
     * @param ball  Ball to pot
     */
    public void onBallPotted(Ball3D ball) {
        // Pot ball in current turn
        currentPottedBalls.add(ball);
    }

    /**
     * Adds all the balls that were potted before types were
     * assigned to the proper player.
     */
    // Since the issue is raised due to a bug in PMD, it is suppressed.
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    private void postPotBalls() {
        for (Ball3D pottedBall: allPottedBalls) {
            if (pottedBall instanceof RegularBall3D) {
                for (Player player: players) {
                    player.potBall((RegularBall3D) pottedBall);
                }
            }
        }
        allPottedBalls.clear();
    }

    /**
     * Handles ball potting logic of all balls in the current turn,
     * including special cases on potting the cue and 8-ball, which might
     * result in the victory or loss of the game.
     */
    // UR anomaly false positive triggered by foreach loop (ball variable)
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    protected void handleBallPotting() {
        // TODO: Special cue ball handling

        // Check if Player has potted all of their assigned ball
        // type balls. We check for this before potting all balls
        // because a Player might pot the 8-ball and then all of
        // their balls after, which would result in a win when
        // it should be a loss.
        boolean allPotted = getActivePlayer().allBallsPotted(remainingBalls);
        boolean eightPotted = false;

        for (Ball3D ball : currentPottedBalls) {
            if (!typesAssigned && !(ball instanceof CueBall3D)) {
                allPottedBalls.add(ball); // until types are assigned
                // keep track of balls potted
            }
            if (ball instanceof RegularBall3D) {
                potRegularBall((RegularBall3D) ball);
            } else if (ball instanceof EightBall3D) {
                eightPotted = true;
            }
            //        else if (ball instanceof CueBall3D) {
            //            // TODO: Logic for cue ball potted
            //            // TODO: reset Cueball after turn and make the turn invalid
            //        }

            // Remove the ball from the remaining balls set
            remainingBalls.remove(ball);
        }

        // 8-ball potted
        if (eightPotted) {
            winGame(allPotted);
        }

        // Reset potted balls for next turn
        currentPottedBalls.clear();
    }

    public void setTypesAssigned(boolean typesAssigned) {
        this.typesAssigned = typesAssigned;
    }

    /**
     * Logic for a regular ball pot.
     * If the players don't have a ball type -> assign ball types to players.
     * @param ball regular ball
     */
    // PMD calls a warning because a change is made to an object and not always used.
    // This warning is incorrect and therefore ignored.
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public void potRegularBall(RegularBall3D ball) {
        Player activePlayer = getActivePlayer();

        // if turncount == 0, this is the first turn (breakshot)
        // so types should not be assigned
        if (turnCount > 0 && !typesAssigned) {
            assignBallTypesToPlayers(ball);
        }

        // Valid pot
        if (typesAssigned) {
            if (activePlayer.getBallType() == ball.getType()) {
                activePlayer.potBall(ball);
            } else { // else pot for other player.
                getNextInactivePlayer().potBall(ball);
            }
        }
    }

    /**
     * Assigns the ball type of the first valid potted ball to the active player.
     * The other player gets the other ball type
     * @param ball first regular ball that is potted in a valid way
     */
    public void assignBallTypesToPlayers(RegularBall3D ball) {
        Player activePlayer = getActivePlayer();
        Player otherPlayer = getNextInactivePlayer();
        activePlayer.assignBallType(ball.getType());
        RegularBall3D.Type otherType;

        // Assign the other ball type to the other player
        if (ball.getType() == RegularBall3D.Type.STRIPED) {
            otherType = RegularBall3D.Type.FULL;
        } else {
            otherType = RegularBall3D.Type.STRIPED;
        }
        otherPlayer.assignBallType(otherType);
        postPotBalls(); // adds balls that were potted
        // before assignment to the proper player.
        typesAssigned = true;
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

    // Might come useful at some point; Determines count for specified
    // Ball type.
    //    /**
    //     * Returns the remaining number of balls of the specified type.
    //     * @param type  Type of ball to get count for
    //     * @return  Number of balls remaining in Game of specified type.
    //     */
    //    public int getRemainingBallCount(RegularBall3D.Type type) {
    //        int count = 0;
    //
    //        for (Ball3D ball : remainingBalls) {
    //            if (ball instanceof RegularBall3D
    //                    && ((RegularBall3D)ball).getType() == type) {
    //                count++;
    //            }
    //        }
    //
    //        return count;
    //    }
}

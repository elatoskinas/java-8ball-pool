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
// We had to suppress DataflowAnomalyAnalysis warnings 4 times due to bugs in PMD
// Because of this, PMD started complaining again.
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class GameState implements GameObserver {
    private transient List<Player> players;
    private transient Set<Ball3D> remainingBalls;
    private transient List<Ball3D> currentPottedBalls; // Balls potted in current turn
    private transient List<Ball3D> allPottedBalls; // All Balls potted in any turn.

    private transient int playerTurn;
    private transient int turnCount;
    private transient boolean typesAssigned; // if ball types have been assigned yet
    private transient boolean cueBallPotted;
    private transient boolean eightBallPotted;

    private transient Player winningPlayer;

    // First ball touched in current turn
    private transient Ball3D firstBallTouched;

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
        this.cueBallPotted = false;

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
        handleTurnAdvancement();
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
    public void winGame(boolean allPotted, boolean cuePotted) {
        if (allPotted && !cuePotted) {
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
    public void onMotionStop(Ball3D touched) {
        this.firstBallTouched = touched;
        advanceTurn();
    }

    @Override
    public void onGameEnded(Player winner, List<Player> players) {
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
        // Check if Player has potted all of their assigned ball
        // type balls. We check for this before potting all balls
        // because a Player might pot the 8-ball and then all of
        // their balls after, which would result in a win when
        // it should be a loss.
        boolean allPotted = getActivePlayer().allBallsPotted(remainingBalls);
        this.cueBallPotted = false;
        this.eightBallPotted = false;

        for (Ball3D ball : currentPottedBalls) {
            // Handle logic for potting the ball
            potSingleBall(ball);

            // Remove the ball from the remaining balls set
            remainingBalls.remove(ball);
        }

        // 8-ball potted
        if (this.eightBallPotted) {
            winGame(allPotted, this.cueBallPotted);
        }

        // Reset potted balls for next turn
        currentPottedBalls.clear();
    }

    /**
     * Pots a single ball by handling any necessary state
     * changes baseed on the ball type.
     * @param ball  Ball to pot
     */
    private void potSingleBall(Ball3D ball) {
        handleUnassignedBallPotting(ball);

        if (ball instanceof RegularBall3D) {
            potRegularBall((RegularBall3D) ball);
        } else if (ball instanceof EightBall3D) {
            this.eightBallPotted = true;
        } else if (ball instanceof CueBall3D) {
            this.cueBallPotted = true;
        }
    }

    /**
     * Handles keeping tack of potting balls when the ball
     * types are not yet assigned to the players.
     * Does nothing if the ball types are already assigned.
     * @param ball  Ball to pot
     */
    private void handleUnassignedBallPotting(Ball3D ball) {
        if (!typesAssigned && !(ball instanceof CueBall3D)) {
            allPottedBalls.add(ball); // until types are assigned
            // keep track of balls potted
        }
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
     * Method to handle all logic with regards to gaining an extra turn.
     */
    // Warnings are suppressed because the 'DU'-anomaly isn't actually applicable here,
    // and it suddenly showed up. Very probable to be a bug in PMD.
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public void handleTurnAdvancement() {
        state = State.Idle;
        Player activePlayer = getActivePlayer();

        // Advance turn to the next Player if the current
        // player should lose their turn.
        if (doesPlayerLoseTurn()) {
            loseTurn();
        }

        // Reset temporary variable
        activePlayer.setPottedCorrectBall(false);
        
        // Increment the turn counter
        turnCount += 1;
    }

    /**
     * Determines whether the active Player should lose their current turn.
     * Check for four criteria:
     * - Did the player touch the right type of ball first
     * - Did the player not pot the cue ball
     * - Did the player pot a ball of the wrong type
     * - Did the player pot a ball of the correct type
     *  Special case: if any ball is potted during the break shot, keep the turn
     * @return  True if the active Player should lose their turn.
     */
    private boolean doesPlayerLoseTurn() {
        // Not all criteria were satisfied -> player loses the turn
        return !isPlayerRegularPottingValid() || cueBallPotted;
    }

    /**
     * Checks whether the Player satisfies all conditions for regular
     * ball potting to gain the next turn.
     * @return  True if the Player has not violated any potting rules
     *          for regular (non-cue) ball potting.
     */
    private boolean isPlayerRegularPottingValid() {
        // If break shot, we only care if the Player potted
        // any balls at all
        if (turnCount == 0) {
            return !allPottedBalls.isEmpty();
        } else {
            // If not break shot, we have to verify
            // that the Player touched & potted the
            // correct ball type.
            return isBallContactValid();
        }
    }

    /**
     * Checks whether the Player contacted the cue ball with the valid
     * pool balls to gain the next turn. This includes touching
     * and potting the correct ball first.
     * @return  True if the Player contacted the right pool balls to
     *          gain next turn.
     */
    private boolean isBallContactValid() {
        // Check whether the first touched ball is correct
        boolean firstTouchCorrect = false;

        if (firstBallTouched instanceof RegularBall3D) {
            RegularBall3D firstTouched = (RegularBall3D) firstBallTouched;
            firstTouchCorrect = firstTouched.getType() == getActivePlayer().getBallType();
        }

        // Additional check to see whether the Player potted the correct ball
        return firstTouchCorrect && getActivePlayer().getPottedCorrectBall();
    }

    public boolean isCueBallPotted() {
        return this.cueBallPotted;
    }

    /**
     * Handles the game logic when a player loses its turn.
     */
    protected void loseTurn() {
        // Increment player turn and wrap turn ID around
        // players size to keep it within bounds
        playerTurn = (playerTurn + 1) % players.size();
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

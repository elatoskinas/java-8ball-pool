package com.sem.pool.game;

import com.sem.pool.scene.Ball3D;
import com.sem.pool.scene.CueBall3D;
import com.sem.pool.scene.RegularBall3D;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class that holds the Pool Ball state during a Pool game.
 * State includes the remaining balls, the potted balls during
 * an iteration, flags whether the cue & eight ball were potted
 * and the first touched ball.
 */
public class GameBallState {
    private transient Set<Ball3D> remainingBalls; // Balls remaining to be potted
    private transient List<Ball3D> currentPottedBalls; // Balls potted in current turn
    private transient List<Ball3D> allPottedBalls; // All Balls potted in any turn.

    // Cue & eight ball potted flags
    private transient boolean cueBallPotted;
    private transient boolean eightBallPotted;

    // First ball touched in current turn
    private transient Ball3D firstBallTouched;

    /**
     * Creates a new Game Ball State instance with the specified
     * Pool Balls.
     * @param poolBalls  List of Pool Balls to initialize Ball State with
     */
    public GameBallState(List<Ball3D> poolBalls) {
        this.remainingBalls = new HashSet<>();
        this.currentPottedBalls = new ArrayList<>();
        this.allPottedBalls = new ArrayList<>();
        this.cueBallPotted = false;

        // Add all pool balls except cue ball to remaining balls set
        for (Ball3D ball : poolBalls) {
            if (!(ball instanceof CueBall3D)) {
                remainingBalls.add(ball);
            }
        }
    }

    public Set<Ball3D> getRemainingBalls() {
        return remainingBalls;
    }

    public List<Ball3D> getCurrentPottedBalls() {
        return currentPottedBalls;
    }

    public List<Ball3D> getAllPottedBalls() {
        return allPottedBalls;
    }

    public Ball3D getFirstBallTouched() {
        return firstBallTouched;
    }

    public boolean isCueBallPotted() {
        return this.cueBallPotted;
    }

    public boolean isEightBallPotted() {
        return this.eightBallPotted;
    }

    public void setFirstBallTouched(Ball3D touched) {
        this.firstBallTouched = touched;
    }

    /**
     * Adds the specified Ball as a potted ball.
     * @param ball  Ball to pot
     */
    public void addPottedBall(Ball3D ball) {
        // Pot ball in current turn
        currentPottedBalls.add(ball);
    }

    /**
     * Clears the current turn potted balls List.
     */
    public void clearPottedBalls() {
        currentPottedBalls.clear();
    }


    /**
     * Removes the specified ball from the remaining balls collection.
     * @param ball  Ball to remove
     */
    public void removeBall(Ball3D ball) {
        remainingBalls.remove(ball);
    }

    /**
     * Adds the specified ball to the pre- type assignment collection
     * of pool balls.
     * @param ball  Ball to pot
     */
    public void addPreAssignedPottedBall(Ball3D ball) {
        allPottedBalls.add(ball);
    }

    /**
     * Clears the pre- assignment collection of pool balls.
     */
    public void clearPreAssignedPottedBalls() {
        allPottedBalls.clear();
    }

    /**
     * Resets the ball potting flags from the previous turn.
     * This includes the Cue & Eight ball potted flags.
     */
    public void resetBallPotFlags() {
        this.eightBallPotted = false;
        this.cueBallPotted = false;
    }

    /**
     * Resets the ball potting flag for the cue ball from the previous turn.
     */
    public void resetCueBall() {
        this.cueBallPotted = false;
    }

    /**
     * Marks the eight ball as potted.
     */
    public void markEightBallAsPotted() {
        this.eightBallPotted = true;
    }

    /**
     * Marks the cue ball as potted.
     */
    public void markCueBallAsPotted() {
        this.cueBallPotted = true;
    }

    /**
     * Checks whether there exists a potted ball in the pre-type assignment
     * stage.
     * @return  True iff potted ball exists
     */
    public boolean existsPottedPreassignedBall() {
        return !allPottedBalls.isEmpty();
    }

    /**
     * Checks whether the Player contacted the cue ball with the valid
     * pool balls to gain the next turn. This includes touching
     * and potting the correct ball first.
     * @return  True if the Player contacted the right pool balls to
     *          gain next turn.
     */
    // Warnings are suppressed because the 'DU'-anomaly isn't actually applicable here,
    // and it suddenly showed up. Very probable to be a bug in PMD.
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public boolean isBallContactValid(Player activePlayer) {
        // Check whether the first touched ball is correct
        boolean firstTouchCorrect = assertCorrectTouch(activePlayer);

        // If the first touch was incorrect,
        // then the next player can place the cue ball wherever it wants
        if (!firstTouchCorrect) {
            this.markCueBallAsPotted();
        }
        
        // Additional check to see whether the Player potted the correct ball
        return firstTouchCorrect && activePlayer.getPottedCorrectBall();
    }

    /**
     * Method that asserts whether the first touch of the current player was correct.
     * @param activePlayer The current active player.
     * @return True iff the first touch was correct.
     */
    private boolean assertCorrectTouch(Player activePlayer) {
        Ball3D firstTouched = getFirstBallTouched();

        if (firstTouched instanceof RegularBall3D) {
            RegularBall3D firstTouchedRegular = (RegularBall3D) firstTouched;
            return assertCorrectType(activePlayer, firstTouchedRegular);
        }
        
        return false;
    }

    /**
     * Method that asserts whether the type of a first touched ball was correct 
     * for the current active player.
     * @param activePlayer The current active player.
     * @param firstTouched The ball the player first touched.
     * @return True iff the type was correct
     */
    private boolean assertCorrectType(Player activePlayer, RegularBall3D firstTouched) {
        // If the assigned type of the player is UNASSIGNED, then any first touch is correct.
        if (activePlayer.getBallType() != RegularBall3D.Type.UNASSIGNED) {
            return firstTouched.getType() == activePlayer.getBallType();
        } else {
            return true;
        }
    }
}

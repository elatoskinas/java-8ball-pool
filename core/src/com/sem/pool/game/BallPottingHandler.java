package com.sem.pool.game;

import com.sem.pool.scene.Ball3D;
import com.sem.pool.scene.CueBall3D;
import com.sem.pool.scene.EightBall3D;
import com.sem.pool.scene.RegularBall3D;

import java.util.Optional;

/**
 * Class that handles the ball potting logic.
 * Provides an API to invoke ball potting handling.
 */
public class BallPottingHandler {
    private transient GameBallState gameBallState;
    private transient boolean typesAssigned; // if ball types have been assigned yet

    /**
     * Creates a new instance of a Ball Potting Handler with the
     * specified game ball state to keep track of.
     * @param gameBallState  Game ball state instance
     */
    public BallPottingHandler(GameBallState gameBallState) {
        this.gameBallState = gameBallState;
    }

    public boolean getTypesAssigned() {
        return typesAssigned;
    }

    /**
     * Adds all the balls that were potted before types were
     * assigned to the proper player.
     */
    // Since the issue is raised due to a bug in PMD, it is suppressed.
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public void potBallsToPlayers(TurnHandler turnHandler) {
        for (Ball3D pottedBall: gameBallState.getAllPottedBalls()) {
            if (pottedBall instanceof RegularBall3D) {
                for (Player player : turnHandler.getPlayers()) {
                    player.potBall((RegularBall3D) pottedBall);
                }
            }
        }
        gameBallState.clearPreAssignedPottedBalls();
    }

    /**
     * Handles ball potting logic of all balls in the current turn,
     * including special cases on potting the cue and 8-ball, which might
     * result in the victory or loss of the game.
     */
    // UR anomaly false positive triggered by foreach loop (ball variable)
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public Optional<Boolean> handleBallPotting(TurnHandler turnHandler) {
        // Check if Player has potted all of their assigned ball
        // type balls. We check for this before potting all balls
        // because a Player might pot the 8-ball and then all of
        // their balls after, which would result in a win when
        // it should be a loss.
        boolean allPotted = turnHandler.getActivePlayer()
                .allBallsPotted(gameBallState.getRemainingBalls());

        gameBallState.resetBallPotFlags();

        for (Ball3D ball : gameBallState.getCurrentPottedBalls()) {
            // Handle logic for potting the ball
            potSingleBall(ball, turnHandler);

            // Remove the ball from the remaining balls set
            gameBallState.removeBall(ball);
        }

        // 8-ball potted
        if (gameBallState.isEightBallPotted()) {
            return Optional.of(getEndGameResult(allPotted));
        }

        // Reset potted balls for next turn
        gameBallState.clearPottedBalls();

        return Optional.empty();
    }

    /**
     * Returns the outcome of the end of the game.
     * @param allPotted  Boolean to indicate if all (assigned) balls were potted by
     *                   current Player
     * @return  True if the current Player wins, false otherwise
     */
    private boolean getEndGameResult(boolean allPotted) {
        // All balls + 8-ball potted; Active player wins.
        return allPotted && !gameBallState.isCueBallPotted();
    }

    /**
     * Pots a single ball by handling any necessary state
     * changes baseed on the ball type.
     * @param ball  Ball to pot
     */
    private void potSingleBall(Ball3D ball, TurnHandler turnHandler) {
        handleUnassignedBallPotting(ball);

        if (ball instanceof RegularBall3D) {
            potRegularBall((RegularBall3D) ball, turnHandler);
        } else if (ball instanceof EightBall3D) {
            gameBallState.markEightBallAsPotted();
        } else if (ball instanceof CueBall3D) {
            gameBallState.markCueBallAsPotted();
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
            gameBallState.addPreAssignedPottedBall(ball);
        }
    }

    public void setTypesAssigned(boolean typesAssigned) {
        this.typesAssigned = typesAssigned;
    }

    /**
     * Logic for a regular ball pot.
     * If the players don't have a ball type -> assign ball types to players.
     * PMD calls a warning because a change is made to an object and not always used.
     * This warning is incorrect and therefore ignored.
     * @param ball regular ball
     */
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public void potRegularBall(RegularBall3D ball, TurnHandler turnHandler) {
        Player activePlayer = turnHandler.getActivePlayer();
        Player nextPlayer = turnHandler.getNextInactivePlayer();

        // if turncount == 0, this is the first turn (breakshot)
        // so types should not be assigned
        if (turnHandler.getTurnCount() > 0 && !typesAssigned) {
            assignBallTypesToPlayers(ball, turnHandler);
        }

        // Valid pot
        if (typesAssigned) {
            if (activePlayer.getBallType() == ball.getType()) {
                activePlayer.potBall(ball);
            } else { // else pot for other player.
                nextPlayer.potBall(ball);
            }
        }
    }

    /**
     * Assigns the ball type of the first valid potted ball to the active player.
     * The other player gets the other ball type
     * @param ball first regular ball that is potted in a valid way
     */
    public void assignBallTypesToPlayers(RegularBall3D ball, TurnHandler turnHandler) {
        turnHandler.getActivePlayer().assignBallType(ball.getType());
        RegularBall3D.Type otherType;

        // Assign the other ball type to the other player
        if (ball.getType() == RegularBall3D.Type.STRIPED) {
            otherType = RegularBall3D.Type.FULL;
        } else {
            otherType = RegularBall3D.Type.STRIPED;
        }
        turnHandler.getNextInactivePlayer().assignBallType(otherType);
        potBallsToPlayers(turnHandler); // adds balls that were potted
        // before assignment to the proper player.
        typesAssigned = true;
    }
}

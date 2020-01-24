package com.sem.pool.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.sem.pool.scene.RegularBall3D;
import org.junit.jupiter.api.Test;

/**
 * Game State and Turn Handler integration testing
 * to verify that Player turns are advanced correctly.
 */
class GameStateTurnIntegrationTest extends GameStateBaseTest {
    /**
     * Tests case to verify that the player turn
     * is set to the next player.
     */
    @Test
    void testAdvancePlayerTurn() {
        int playerTurn = gameState.getTurnHandler().getPlayerTurn();
        gameState.advanceTurn();
        int newPlayerTurn = gameState.getTurnHandler().getPlayerTurn();

        assertNotEquals(playerTurn, newPlayerTurn);
        assertEquals(Math.abs(playerTurn - newPlayerTurn), 1);

        gameState.advanceTurn();
        assertEquals(playerTurn, gameState.getTurnHandler().getPlayerTurn());
    }

    /**
     * Test case to verify the get active player.
     */
    @Test
    void testGetActivePlayer() {
        int activePlayerIndex = gameState.getTurnHandler().getPlayerTurn();
        assertEquals(players.get(activePlayerIndex), gameState.getTurnHandler().getActivePlayer());
    }

    /**
     * Test case to verify the get inactive player.
     */
    @Test
    void testGetInactivePlayer() {
        int inactivePlayerIndex = gameState.getTurnHandler().getPlayerTurn() + 1;
        assertEquals(players.get(inactivePlayerIndex),
                gameState.getTurnHandler().getNextInactivePlayer());
    }

    /**
     * Test case to verify that the next inactive player
     * becomes the active player when turn advances.
     */
    @Test
    void testGetNextActivePlayer() {
        Player inactivePlayer = gameState.getTurnHandler().getNextInactivePlayer();
        gameState.advanceTurn();
        assertEquals(gameState.getTurnHandler().getActivePlayer(), inactivePlayer);
    }

    /**
     * Test case to verify that a player keeps their turn if they pot a correct ball
     * while ball types are not yet assigned.
     */
    @Test
    void testKeepTurnAfterBallPotUnassigned() {
        balls = constructBallsList(true, true, 2, 2);

        gameState = new GameState(players, balls);

        // Keep track of current player and pot a ball
        Player current = gameState.getTurnHandler().getActivePlayer();
        gameState.onBallPotted(balls.get(2));
        gameState.onMotionStop(balls.get(2)); // Advances turn

        assertEquals(current, gameState.getTurnHandler().getActivePlayer());
    }

    /**
     * Test case to verify that a player keeps its turn when it pots a ball
     * while ball types are already assigned.
     */
    @Test
    void testKeepTurnAfterBallPotAssigned() {
        balls = constructBallsList(true, true, 2, 2);

        gameState = new GameState(players, balls);

        // Skip break shot
        gameState.advanceTurn();
        // pot a ball to assign types
        gameState.onBallPotted(balls.get(2));
        // This should result in the player keeping their turn (tested in previous test case)
        gameState.onMotionStop(balls.get(2));
        // Keep track of current player and pot another ball
        Player current = gameState.getTurnHandler().getActivePlayer();
        gameState.onBallPotted(balls.get(3));
        gameState.onMotionStop(balls.get(3));

        // This should result in the player keeping its turn again
        assertEquals(current, gameState.getTurnHandler().getActivePlayer());
    }

    /**
     * Test case to verify that a player keeps its turn when it pots a ball during the breakshot.
     */
    @Test
    void testKeepTurnAfterBallPotBreakShot() {
        balls = constructBallsList(true, true, 2, 2);

        gameState = new GameState(players, balls);

        // Break shot -> pot a ball while keeping track of the player
        Player current = gameState.getTurnHandler().getActivePlayer();
        gameState.onBallPotted(balls.get(2));
        gameState.onMotionStop(balls.get(2));

        assertEquals(current, gameState.getTurnHandler().getActivePlayer());
        assertFalse(gameState.getBallPottingHandler().getTypesAssigned());
    }

    /**
     * Test case to verify that a player loses their turn if they pot a correct ball,
     * but touch a wrong ball first.
     */
    @Test
    void loseTurnAfterWrongFirstTouch() {
        balls = constructBallsList(true, true, 2, 2);

        gameState = new GameState(players, balls);

        // Break shot
        gameState.advanceTurn();
        // Keep track of current player and pot a ball
        Player current = gameState.getTurnHandler().getActivePlayer();
        gameState.onBallPotted(balls.get(2));
        // However, first touch the 8 ball.
        gameState.onMotionStop(balls.get(0));

        assertNotEquals(current, gameState.getTurnHandler().getActivePlayer());
    }

    /**
     * Test case to verify that a player loses their turn if they pot a correct ball,
     * but pot the cue ball as well.
     */
    @Test
    void loseTurnWhenCueBallPotted() {
        balls = constructBallsList(true, true, 2, 2);

        gameState = new GameState(players, balls);

        // Break shot
        gameState.advanceTurn();
        // Keep track of current player and pot a ball
        gameState.onBallPotted(balls.get(2));
        // Pot the cue ball
        gameState.onBallPotted(balls.get(0));
        Player current = gameState.getTurnHandler().getActivePlayer();

        gameState.onMotionStop(balls.get(2));

        assertNotEquals(current, gameState.getTurnHandler().getActivePlayer());
    }

    /**
     * Test case to verify that a player loses their turn when
     * they only pot a ball of the wrong type.
     */
    @Test
    void loseTurnWhenOnlyWrongBallPotted() {
        balls = constructBallsList(true, true, 2, 2);

        gameState = new GameState(players, balls);

        // Break shot
        gameState.advanceTurn();
        // Keep track of current player and pot a wrong ball
        gameState.getTurnHandler().getActivePlayer().assignBallType(RegularBall3D.Type.STRIPED);
        Player current = gameState.getTurnHandler().getActivePlayer();
        gameState.onBallPotted(balls.get(2));

        gameState.onMotionStop(balls.get(5));

        assertNotEquals(current, gameState.getTurnHandler().getActivePlayer());
    }

    /**
     * Test case to verify that a player loses its turn when it pots the cue ball
     * as well as a regular ball during the break shot.
     */
    @Test
    void loseTurnWhenCueBallPottedBreakShot() {
        balls = constructBallsList(true, true, 2, 2);

        gameState = new GameState(players, balls);

        // Pot a regular ball and cue ball during breakshot while keeping track of the player
        gameState.onBallPotted(balls.get(2));
        gameState.onBallPotted(balls.get(0));
        Player current = gameState.getTurnHandler().getActivePlayer();
        gameState.onMotionStop(balls.get(2));

        assertNotEquals(current, gameState.getTurnHandler().getActivePlayer());
    }
}
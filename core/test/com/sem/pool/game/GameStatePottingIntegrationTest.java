package com.sem.pool.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sem.pool.scene.Ball3D;
import com.sem.pool.scene.RegularBall3D;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Game State, Game Ball State and Potting integration test
 * class to verify that the components work together properly.
 */
class GameStatePottingIntegrationTest extends GameStateBaseTest {
    /**
     * Test case to verify that potting a ball via the
     * onBallPotted call in GameState removes the Ball
     * from the remaining pool balls set.
     */
    @Test
    void testBallPottedRemovedFromRemaining() {
        // Get first ball (constructed in setUp)
        // NOTE: Id 0 won't work since it's a cue ball
        // NOTE: Id 1 won't work since it's a eight ball

        RegularBall3D ball = (RegularBall3D) balls.get(2);

        // Verify that ball is contained in remaining set (handled
        // in constructor)
        assertTrue(gameState.getGameBallState().getRemainingBalls().contains(ball));

        // Pot the ball
        gameState.onBallPotted(ball);
        gameState.advanceTurn();

        // Assert ball is no longer contained in remaining ball set
        assertFalse(gameState.getGameBallState().getRemainingBalls().contains(ball));
    }

    /**
     * Test case to verify that upon a ball being potted
     * in the Game State, the active Player (which is assumed
     * to be player with id 0 by default) pots the ball.
     * Because it is the first regular ball that is potted,
     * both players get a ball type assigned.
     * TODO: Update test case for break shot or unvalid pot.
     */
    @Test
    void testBallPottedPlayerInteraction() {
        assertEquals(players.get(0).getBallType(), RegularBall3D.Type.UNASSIGNED);
        assertEquals(players.get(1).getBallType(), RegularBall3D.Type.UNASSIGNED);

        RegularBall3D ball = (RegularBall3D) balls.get(2);
        assertEquals(ball.getType(), RegularBall3D.Type.FULL);

        // Pot a full regular ball
        gameState.onBallPotted(ball);
        gameState.advanceTurn();

        // Verify the active player (which is the first Player by default
        // after constructing GameState object) pots the ball
        assertTrue(gameState.getGameBallState().getAllPottedBalls().contains(ball));

        // Ensure that both player types are not updated
        // since the ball was potted in the first turn (break shot)
        assertEquals(players.get(0).getBallType(), RegularBall3D.Type.UNASSIGNED);
        assertEquals(players.get(1).getBallType(), RegularBall3D.Type.UNASSIGNED);
    }

    /**
     * Test case to verify that upon a full regular ball
     * being potted in the Game State, the active player and
     * the other player get the right ball type assigned.
     */
    @Test
    void ballTypeAssignmentPlayersFullBall() {
        // Create List of 2 mocked players
        Player player1 = Mockito.mock(Player.class);
        Player player2 = Mockito.mock(Player.class);
        players.clear();
        players.add(player1);
        players.add(player2);

        Mockito.when(player1.getBallType()).thenReturn(RegularBall3D.Type.UNASSIGNED);
        Mockito.when(player2.getBallType()).thenReturn(RegularBall3D.Type.UNASSIGNED);

        // Re-create game state with mocked players
        gameState = new GameState(players, balls);

        RegularBall3D ball = (RegularBall3D) balls.get(2);
        assertEquals(ball.getType(), RegularBall3D.Type.FULL);

        // Pot a full regular ball
        gameState.advanceTurn();
        gameState.onBallPotted(ball);
        gameState.advanceTurn();
        Mockito.verify(player1).assignBallType(RegularBall3D.Type.STRIPED);
        Mockito.verify(player2).assignBallType(RegularBall3D.Type.FULL);
    }

    /**
     * Test case to verify that upon a striped regular ball
     * being potted in the Game State, the active player and
     * the other player get the right ball type assigned.
     */
    @Test
    void ballTypeAssignmentPlayersStripedBall() {
        // Create List of 2 mocked players
        Player player1 = Mockito.mock(Player.class);
        Player player2 = Mockito.mock(Player.class);
        players.clear();
        players.add(player1);
        players.add(player2);

        Mockito.when(player1.getBallType()).thenReturn(RegularBall3D.Type.UNASSIGNED);
        Mockito.when(player2.getBallType()).thenReturn(RegularBall3D.Type.UNASSIGNED);

        // Re-create game state with mocked players
        gameState = new GameState(players, balls);

        RegularBall3D ball = (RegularBall3D) balls.get(4);
        assertEquals(ball.getType(), RegularBall3D.Type.STRIPED);

        // Pot a full regular ball
        gameState.advanceTurn();
        gameState.onBallPotted(ball);
        gameState.advanceTurn();
        Mockito.verify(player1).assignBallType(RegularBall3D.Type.FULL);
        Mockito.verify(player2).assignBallType(RegularBall3D.Type.STRIPED);
    }

    /**
     * Test case to verify that the current potted balls
     * List is initialized to empty List initially (after constructcion).
     */
    @Test
    void testGetCurrentPottedBalls() {
        List<Ball3D> currentPotted = gameState.getGameBallState().getCurrentPottedBalls();

        assertNotNull(currentPotted);
        assertEquals(0, currentPotted.size());
    }

    /**
     * Test case to verify that advancing the turn in Game State
     * clears the potted balls.
     */
    @Test
    void testAdvanceTurnClearPottedBalls() {
        final int potCount = 4;

        // Pot balls
        for (int i = 0; i < potCount; ++i) {
            RegularBall3D ball = Mockito.mock(RegularBall3D.class);
            gameState.onBallPotted(ball);
        }

        assertEquals(potCount, gameState.getGameBallState().getCurrentPottedBalls().size());

        gameState.advanceTurn();
        assertEquals(0, gameState.getGameBallState().getCurrentPottedBalls().size());
    }

    /**
     * Test case to verify that upon potting ball in Game State, the
     * current potted balls List is updated accordingly.
     */
    @Test
    void testPotBallsAddToPotted() {
        assertEquals(0, gameState.getGameBallState().getCurrentPottedBalls().size());

        final int potCount = 2;

        // Pot balls
        for (int i = 0; i < potCount; ++i) {
            RegularBall3D ball = Mockito.mock(RegularBall3D.class);
            gameState.onBallPotted(ball);
        }

        assertEquals(potCount, gameState.getGameBallState().getCurrentPottedBalls().size());
    }

    /**
     * Test case to verify that potting multiple balls of
     * the same type as the active player's assigned type
     * properly pots the balls for that Player.
     */
    @Test
    void testHandleBallPottingMultipleSame() {
        final RegularBall3D.Type type = RegularBall3D.Type.FULL;
        final int potCount = 3;

        Player player = gameState.getTurnHandler().getActivePlayer();
        player.assignBallType(type);
        gameState.getBallPottingHandler().setTypesAssigned(true);

        for (int i = 0; i < potCount; ++i) {
            RegularBall3D ball = Mockito.mock(RegularBall3D.class);
            Mockito.when(ball.getType()).thenReturn(type);
            gameState.onBallPotted(ball);
        }

        gameState.advanceTurn();

        assertEquals(potCount, player.getPottedBalls().size());
    }

    /**
     * Test case to verify that potting multiple balls of
     * the same type as the active player's assigned type
     * properly pots the balls for that Player.
     */
    @Test
    void testHandleBallPottingMultipleSDiffering() {
        final RegularBall3D.Type type = RegularBall3D.Type.FULL;
        Player player = gameState.getTurnHandler().getActivePlayer();
        gameState.getBallPottingHandler().setTypesAssigned(true);
        player.assignBallType(type);

        RegularBall3D ball1 = Mockito.mock(RegularBall3D.class);
        RegularBall3D ball2 = Mockito.mock(RegularBall3D.class);

        Mockito.when(ball1.getType()).thenReturn(RegularBall3D.Type.FULL);
        Mockito.when(ball2.getType()).thenReturn(RegularBall3D.Type.STRIPED);

        gameState.getBallPottingHandler().potRegularBall(ball1, gameState.getTurnHandler());
        gameState.getBallPottingHandler().potRegularBall(ball2, gameState.getTurnHandler());

        gameState.advanceTurn();
        assertEquals(1, player.getPottedBalls().size());
    }

    /**
     * Test case to verify that when an eight ball is potted
     * when the Player has not yet potted all of their balls,
     * then the Player loses the game.
     */
    @Test
    void testPotEightBallLossNotAllPotted() {
        balls = constructBallsList(true, true, 2, 2);
        Ball3D eightBall = balls.get(1);

        gameState = new GameState(players, balls);

        // Assign ball type to Player
        gameState.getTurnHandler().getActivePlayer().assignBallType(RegularBall3D.Type.FULL);

        // Pot eight ball for current Player
        gameState.onBallPotted(eightBall);
        gameState.advanceTurn();

        Player expectedWinner = gameState.getTurnHandler().getNextInactivePlayer();
        Optional<Player> winner = gameState.getWinningPlayer();

        assertTrue(winner.isPresent());
        assertEquals(expectedWinner, winner.get());
    }

    /**
     * Test case to verify that when all the balls
     * of the Player's assigned type are potted
     * during the current turn together with the 8-ball,
     * a loss occurs.
     */
    @Test
    void testPotEightBallLossPotAll() {
        balls = constructBallsList(true, true, 2, 1);

        gameState = new GameState(players, balls);

        // Assign ball type to Player
        gameState.getTurnHandler().getActivePlayer().assignBallType(RegularBall3D.Type.FULL);

        // Pot balls for current Player
        gameState.onBallPotted(balls.get(2));
        gameState.onBallPotted(balls.get(3));
        gameState.onBallPotted(balls.get(1));
        gameState.advanceTurn();

        Player expectedWinner = gameState.getTurnHandler().getNextInactivePlayer();
        Optional<Player> winner = gameState.getWinningPlayer();

        assertTrue(winner.isPresent());
        assertEquals(expectedWinner, winner.get());
    }


    /**
     * Test case to verify that when -ONLY- an eight ball is potted
     * when the Player has potted all of their balls, then
     * the Player wins the game.
     */
    @Test
    void testPotOnlyEightBallWin() {
        balls = constructBallsList(true, true, 0, 0);

        gameState = new GameState(players, balls);
        gameState.getTurnHandler().getActivePlayer().assignBallType(RegularBall3D.Type.FULL);

        // Pot eight ball for current Player
        gameState.onBallPotted(balls.get(1));
        gameState.advanceTurn();

        Player expectedWinner = gameState.getTurnHandler().getActivePlayer();
        Optional<Player> winner = gameState.getWinningPlayer();

        assertTrue(winner.isPresent());
        assertEquals(expectedWinner, winner.get());
    }

    /**
     * Test case to verify that when the active Player
     * has already potted all of their balls, and they
     * pot the 8-ball, but accidentally pot balls which
     * are not of their type, they still win the game.
     */
    @Test
    void testPotEightBallAndOthersWin() {
        balls = constructBallsList(true, true, 0, 3);

        gameState = new GameState(players, balls);
        gameState.getTurnHandler().getActivePlayer().assignBallType(RegularBall3D.Type.FULL);

        // Pot eight ball for current Player
        gameState.onBallPotted(balls.get(3));
        gameState.onBallPotted(balls.get(1));
        gameState.advanceTurn();

        Player expectedWinner = gameState.getTurnHandler().getActivePlayer();
        Optional<Player> winner = gameState.getWinningPlayer();

        assertTrue(winner.isPresent());
        assertEquals(expectedWinner, winner.get());
    }

    /**
     * Simple test case to test that at the second turn ball types are assigned as they should.
     */
    @Test
    void testAssignmentSimple() {
        balls = constructBallsList(true, true, 3, 3);
        gameState = new GameState(players, balls);

        gameState.advanceTurn();
        // Now it's player 2's turn
        gameState.onBallPotted(balls.get(2));
        gameState.advanceTurn();
        assertEquals(players.get(1).getBallType(), RegularBall3D.Type.FULL);
        assertEquals(players.get(0).getBallType(), RegularBall3D.Type.STRIPED);
    }

    /**
     * Test case to test that after assignment of types,
     * the assignment does not change.
     */
    @Test
    void testAssignmentDouble() {
        balls = constructBallsList(true, true, 3, 3);
        gameState = new GameState(players, balls);

        gameState.advanceTurn();
        // Now it's player 2's turn
        gameState.onBallPotted(balls.get(2));
        gameState.advanceTurn(); // handle turn events
        assertEquals(players.get(1).getBallType(), RegularBall3D.Type.FULL);
        assertEquals(players.get(0).getBallType(), RegularBall3D.Type.STRIPED);
        gameState.advanceTurn();
        gameState.onBallPotted(balls.get(7));
        assertEquals(players.get(1).getBallType(), RegularBall3D.Type.FULL);
        assertEquals(players.get(0).getBallType(), RegularBall3D.Type.STRIPED);
    }


    /**
     * Test case to test that after assignment of types,
     * the list of all potted balls is set to null.
     */
    @Test
    void testAllPottedBalls() {
        balls = constructBallsList(true, true, 3, 3);
        gameState = new GameState(players, balls);
        gameState.onBallPotted(balls.get(2));
        gameState.onBallPotted(balls.get(0));
        gameState.advanceTurn(); // handle turn events
        assertTrue(gameState.getGameBallState().getAllPottedBalls().contains(balls.get(2)));
        assertFalse(gameState.getGameBallState().getAllPottedBalls().contains(balls.get(0)));
    }

    /**
     * Test case to test that balls potted during
     * the break shot do not impact the ball assignment.
     */
    @Test
    void testAssignmentBreakShot() {
        balls = constructBallsList(true, true, 3, 3);
        gameState = new GameState(players, balls);

        // stillAtBreakShot
        gameState.onBallPotted(balls.get(2));
        gameState.advanceTurn();
        assertEquals(players.get(1).getBallType(), RegularBall3D.Type.UNASSIGNED);
        assertEquals(players.get(0).getBallType(), RegularBall3D.Type.UNASSIGNED);
    }

    /**
     * Test case to test that when the cue ball
     * is potted, no ball types are assigned.
     */
    @Test
    void testAssignmentCuePot() {
        balls = constructBallsList(true, true, 3, 3);
        gameState = new GameState(players, balls);

        gameState.advanceTurn();
        // not at breakshot but since cue ball is potted no type should be assigned
        gameState.onBallPotted(balls.get(0));
        gameState.advanceTurn();
        assertEquals(players.get(1).getBallType(), RegularBall3D.Type.UNASSIGNED);
        assertEquals(players.get(0).getBallType(), RegularBall3D.Type.UNASSIGNED);
    }

    /**
     * Test case to test that balls potted during
     * the break shot are added to a players potted balls,
     * if the player gets assigned a ball type.
     */
    @Test
    void testAssignmentBreakShotPot() {
        balls = constructBallsList(true, true, 3, 3);
        gameState = new GameState(players, balls);

        // pot a two balls during breakshot
        gameState.onBallPotted(balls.get(5));
        gameState.onBallPotted(balls.get(3));
        // handle turn, this results in player 0 keeping their turn
        gameState.onMotionStop(balls.get(5)); 
        // Player 0 pots full ball
        gameState.onBallPotted(balls.get(2));
        gameState.onMotionStop(balls.get(2)); // handle turn
        assertEquals(players.get(0).getBallType(), RegularBall3D.Type.FULL);
        assertEquals(players.get(1).getBallType(), RegularBall3D.Type.STRIPED);
        assertTrue(players.get(0).getPottedBalls().contains(balls.get(2)));
        assertTrue(players.get(0).getPottedBalls().contains(balls.get(3)));
        assertTrue(players.get(1).getPottedBalls().contains(balls.get(5)));
    }

    /**
     * Test case to assert that the 8-Ball is not added to a players potted balls.
     */
    @Test
    void testAddPottedBallsWithEightBall() {
        balls = constructBallsList(true, true, 3, 3);
        gameState = new GameState(players, balls);

        // pot a two balls during breakshot
        gameState.onBallPotted(balls.get(5));
        gameState.onBallPotted(balls.get(3));
        // if this happens the game should immediately end.
        gameState.onMotionStop(balls.get(5));
        // Player 1 pots full ball
        gameState.onBallPotted(balls.get(2));
        gameState.onMotionStop(balls.get(5));
        assertEquals(players.get(1).getBallType(), RegularBall3D.Type.STRIPED);
        assertEquals(players.get(0).getBallType(), RegularBall3D.Type.FULL);
        assertTrue(players.get(0).getPottedBalls().contains(balls.get(2)));
        assertTrue(players.get(0).getPottedBalls().contains(balls.get(3)));
        assertTrue(players.get(1).getPottedBalls().contains(balls.get(5)));
        assertFalse(players.get(0).getPottedBalls().contains(balls.get(1)));
        assertFalse(players.get(1).getPottedBalls().contains(balls.get(1)));
    }


    /**
     * Test case to assert that a ball is added to the right player.
     */
    @Test
    void testAddPottedBallToRightPlayer() {
        balls = constructBallsList(true, true, 3, 3);
        gameState = new GameState(players, balls);

        // pot a two balls during breakshot
        gameState.onBallPotted(balls.get(5));
        gameState.onBallPotted(balls.get(3));
        // if this happens the game should immediately end.
        gameState.onMotionStop(balls.get(5)); // player 0 keeps the turn here,
        // as a ball was potted during the break shot
        // Player 2 pots full ball
        gameState.onBallPotted(balls.get(2));
        gameState.onMotionStop(balls.get(2));
        assertEquals(players.get(0).getBallType(), RegularBall3D.Type.FULL);
        assertEquals(players.get(1).getBallType(), RegularBall3D.Type.STRIPED);
        // Player 1 pots a full ball, should be added to player 2's potted balls
        gameState.onBallPotted(balls.get(3));
        assertTrue(players.get(0).getPottedBalls().contains(balls.get(3)));
    }

    /**
     * Test case to assert that a ball is not in any player's list if
     * players have not been assigned a type yet.
     */
    @Test
    void testAddPottedBallToNoPlayer() {
        balls = constructBallsList(true, true, 3, 3);
        gameState = new GameState(players, balls);

        // pot a ball during breakshot
        gameState.onBallPotted(balls.get(5));
        // if this happens the game should immediately end.
        gameState.advanceTurn();
        assertFalse(players.get(0).getPottedBalls().contains(balls.get(5)));
        assertFalse(players.get(1).getPottedBalls().contains(balls.get(5)));
    }

    /**
     * Test case to verify that potting both the cue ball and the
     * 8-ball for a Player who has already potted all of their balls
     * results in a loss. The scenario is tested when the Player
     * pots the 8-ball, and then the cue ball after.
     */
    @Test
    void testPotCueAndEightBallEightFirst() {
        balls = constructBallsList(true, true, 0, 3);

        gameState = new GameState(players, balls);
        gameState.getTurnHandler().getActivePlayer().assignBallType(RegularBall3D.Type.FULL);

        // Pot eight ball for current Player
        gameState.onBallPotted(balls.get(1));
        gameState.onBallPotted(balls.get(0));
        gameState.advanceTurn();

        // Next Player is the winner
        Player expectedWinner = gameState.getTurnHandler().getNextInactivePlayer();
        Optional<Player> winner = gameState.getWinningPlayer();

        assertTrue(winner.isPresent());
        assertEquals(expectedWinner, winner.get());
    }

    /**
     * Test case to verify that potting both the cue ball and the
     * 8-ball for a Player who has already potted all of their balls
     * results in a loss. The scenario is tested where the Player
     * pots the 8-ball, and then the cue ball right after.
     */
    @Test
    void testPotCueAndEightBallCueBallFirst() {
        balls = constructBallsList(true, true, 4, 0);

        gameState = new GameState(players, balls);
        gameState.getTurnHandler().getActivePlayer().assignBallType(RegularBall3D.Type.STRIPED);

        // Pot eight ball for current Player
        gameState.onBallPotted(balls.get(0));
        gameState.onBallPotted(balls.get(1));
        gameState.advanceTurn();

        // Next Player is the winner
        Player expectedWinner = gameState.getTurnHandler().getNextInactivePlayer();
        Optional<Player> winner = gameState.getWinningPlayer();

        assertTrue(winner.isPresent());
        assertEquals(expectedWinner, winner.get());
    }
}
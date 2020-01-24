package com.sem.pool.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import com.sem.pool.scene.NullBall;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Class for unit tests for the Game Stete class
 */
class GameStateTest extends GameStateBaseTest {
    /**
     * Test case to verify that the constructor of the GameState properly instantiates
     * the GameState with the specified Players list, and adds all the balls
     * except the cue ball to the remaining balls list.
     */
    @Test
    void testConstructor() {
        final int solidBalls = 3;
        final int stripedBalls = 2;
        balls = constructBallsList(true, true, solidBalls, stripedBalls);

        // We do not expect the cue ball to be in remaining balls list
        final int expectedBalls = solidBalls + stripedBalls + 1;

        GameState gameState2 = new GameState(players, balls);

        assertEquals(players, gameState2.getTurnHandler().getPlayers());
        assertEquals(expectedBalls, gameState2.getGameBallState().getRemainingBalls().size());
    }


    /**
     * Tests if the number of the random
     * starting player is valid.
     */
    @Test
    void testInitStartingPlayer() {
        for (int i = 0; i < 10; i++) {
            gameState.getTurnHandler().initializeStartingPlayer();
            int playerTurn = gameState.getTurnHandler().getPlayerTurn();
            assertTrue(playerTurn == 1 || playerTurn == 0);
        }
    }

    /**
     * Tests the transition of starting the game, which
     * transitions from a newly created (stopped) game state
     * to a started game state.
     */
    @Test
    void testStartGame() {
        assertFalse(gameState.isStarted());

        gameState.onGameStarted();

        assertTrue(gameState.isStarted());
    }


    /**
     * Test case to verify that the game is correctly set to running.
     */
    @Test
    void testGameStateIsRunning() {
        assertFalse(gameState.isInMotion());
        gameState.onMotion();
        assertTrue(gameState.isInMotion());
    }

    /**
     * Test case to verify that triggering the end game event
     * causes the inner Game State to be tracked as stopped.
     */
    @Test
    void testEndGame() {
        Player winner = Mockito.mock(Player.class);
        Player loser = Mockito.mock(Player.class);

        ArrayList<Player> players = new ArrayList<>();
        players.add(winner);
        players.add(loser);

        gameState.onGameEnded(winner, players);
        assertTrue(gameState.isStopped());
    }

    /**
     * Test case to verify that the player cannot place the cue ball 
     * when the state is not yet idle. 
     */
    @Test
    void placeCueBallNotIdle() {
        GameState spyGameState = Mockito.spy(gameState);
        Mockito.doReturn(false).when(spyGameState).isIdle();

        // Pot the cue ball
        spyGameState.onBallPotted(balls.get(0));
        spyGameState.onMotionStop(balls.get(2));

        assertFalse(spyGameState.canPlaceCueBall());
    }

    /**
     * Test case to verify that the cue ball cannot be placed when it is not potted.
     */
    @Test
    void placeCueBallNotPotted() {
        GameState spyGameState = Mockito.spy(gameState);
        Mockito.doReturn(true).when(spyGameState).isIdle();

        assertFalse(spyGameState.canPlaceCueBall());
    }

    /**
     * Test case to verify that the cue ball can be placed when 
     * it is both potted and the state is idle.
     */
    @Test
    void canPlaceCueBallPotted() {
        GameState spyGameState = Mockito.spy(gameState);
        Mockito.doReturn(true).when(spyGameState).isIdle();

        // Pot the cue ball
        spyGameState.onBallPotted(balls.get(0));
        spyGameState.onMotionStop(balls.get(2));

        assertTrue(spyGameState.canPlaceCueBall());
    }

    /**
     * Test case to verify that the cue ball can be replaced
     * when the player did not touch a single ball.
     */
    @Test
    void canPlaceCueBallNoTouch() {
        // Don't hit a single ball
        gameState.onMotionStop(new NullBall());

        assertTrue(gameState.canPlaceCueBall());
    }

    /**
     * Test case to verify that no touches are considered 'incorrect'
     * before types are assigned.
     */
    @Test
    void cannotPlaceCueBallUnassignedTypes() {
        // Hit a solid ball
        gameState.onMotionStop(balls.get(2));
        assertFalse(gameState.canPlaceCueBall());

        // Hit a striped ball
        gameState.onMotionStop(balls.get(4));
        assertFalse(gameState.canPlaceCueBall());
    }

    /**
     * Test case to verify that a player can place the cue ball 
     * when the previous player made an incorrect first touch.
     */
    @Test
    void canPlaceCueBallIncorrectTouch() {
        // Break shot
        gameState.onMotionStop(balls.get(2));

        // Have player 2 pot a solid ball
        gameState.onBallPotted(balls.get(2));
        gameState.onMotionStop(balls.get(2));

        // Now, player 2 keeps the turn
        // Have player 2 touch a striped ball
        gameState.onMotionStop(balls.get(4));

        // Assert that the active player is player 1, and the cue ball can be placed.
        assertEquals(gameState.getTurnHandler().getActivePlayer(), players.get(0));
        assertTrue(gameState.canPlaceCueBall());
    }

    //    /**
    //     * Test case to verify that when retrieving a winner
    //     * when a winner is not yet determined returns an
    //     * empty optional object.
    //     */
    //    @Test
    //    void testGetWinnerNoWinner() {
    //        Optional<Player> winner = gameState.getWinningPlayer();
    //        assertFalse(winner.isPresent());
    //    }
    //
    //    /**
    //     * Test case to verify that retrieving the ball count
    //     * for solid balls returns the appropriate count.
    //     */
    //    @Test
    //    void testGetCountSolid() {
    //        final int solid = 4;
    //        final int striped = 5;
    //        final RegularBall3D.Type type = RegularBall3D.Type.FULL;
    //
    //        balls = constructBallsList(true, true, solid, striped);
    //        gameState = new GameState(players, balls);
    //
    //        int result = gameState.getRemainingBallCount(type);
    //
    //        assertEquals(result, solid);
    //    }
    //
    //    /**
    //     * Test case to verify that retrieving the ball count
    //     * for striped balls returns the appropriate count.
    //     */
    //    @Test
    //    void testGetCountStriped() {
    //        final int solid = 6;
    //        final int striped = 7;
    //        final RegularBall3D.Type type = RegularBall3D.Type.STRIPED;
    //
    //        balls = constructBallsList(true, true, solid, striped);
    //        gameState = new GameState(players, balls);
    //
    //        int result = gameState.getRemainingBallCount(type);
    //
    //        assertEquals(striped, result);
    //    }
}
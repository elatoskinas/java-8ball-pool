package com.sem.pool.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sem.pool.scene.Ball3D;
import com.sem.pool.scene.CueBall3D;
import com.sem.pool.scene.EightBall3D;
import com.sem.pool.scene.RegularBall3D;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class GameStateTest {
    transient GameState gameState;
    transient List<Player> players;
    transient List<Ball3D> balls;

    @BeforeEach
    public void setUp() {
        Player player1 = new Player(0);
        Player player2 = new Player(1);
        players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        balls = constructBallsList(true, true, 2, 2);

        gameState = new GameState(players, balls);
    }

    /**
     * Helper method to construct a List of Ball3D objects from the specified configuraion.
     *
     * @param cueBall     True to add the cue ball to resulting list
     * @param eightBall   True to add he eight ball to resulting list
     * @param solid       Number of solid balls to add to list
     * @param striped     Number of striped balls to add to list
     * @return            Resulting list of balls
     */
    private List<Ball3D> constructBallsList(boolean cueBall, boolean eightBall,
                                            int solid, int striped) {
        List<Ball3D> result = new ArrayList<>();

        if (cueBall) {
            CueBall3D cueBall3D = Mockito.mock(CueBall3D.class);
            result.add(cueBall3D);
        }

        if (eightBall) {
            EightBall3D eightBall3D = Mockito.mock(EightBall3D.class);
            result.add(eightBall3D);
        }

        for (int i = 0; i < solid; ++i) {
            RegularBall3D ball = Mockito.mock(RegularBall3D.class);
            Mockito.when(ball.getType()).thenReturn(RegularBall3D.Type.FULL);
            result.add(ball);
        }

        for (int i = 0; i < striped; ++i) {
            RegularBall3D ball = Mockito.mock(RegularBall3D.class);
            Mockito.when(ball.getType()).thenReturn(RegularBall3D.Type.STRIPED);
            result.add(ball);
        }

        return result;
    }

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

        assertEquals(players, gameState2.getPlayers());
        assertEquals(expectedBalls, gameState2.getRemainingBalls().size());
        assertNotNull(gameState2.getObservers());
        assertTrue(gameState2.getObservers().isEmpty());
    }


    /**
     * Tests if the number of the random
     * starting player is valid.
     */
    @Test
    void testInitStartingPlayer() {
        for (int i = 0; i < 10; i++) {
            gameState.initStartingPlayer();
            int playerTurn = gameState.getPlayerTurn();
            assertTrue(playerTurn == 1 || playerTurn == 0);
        }
    }

    /**
     * Tests case to verify that the player turn
     * is set to the next player.
     */
    @Test
    void testAdvancePlayerTurn() {
        int playerTurn = gameState.getPlayerTurn();
        gameState.advanceTurn();
        int newPlayerTurn = gameState.getPlayerTurn();

        assertNotEquals(playerTurn, newPlayerTurn);
        assertEquals(Math.abs(playerTurn - newPlayerTurn), 1);

        gameState.advanceTurn();
        assertEquals(playerTurn, gameState.getPlayerTurn());
    }

    /**
     * Tests the transition of starting the game, which
     * transitions from a newly created (stopped) game state
     * to a started game state.
     */
    @Test
    void testStartGame() {
        assertFalse(gameState.isStarted());

        gameState.startGame();

        assertTrue(gameState.isStarted());
    }

    /**
     * Test case to verify that adding an observer to the observers of the
     * GameState adds the observer successfully.
     */
    @Test
    void testAddObserver() {
        assertTrue(gameState.getObservers().isEmpty());

        GameStateObserver observer = Mockito.mock(GameStateObserver.class);
        gameState.addObserver(observer);

        assertTrue(gameState.getObservers().contains(observer));
    }

    /**
     * Test case to verify that removing an observer from the observers
     * of the GameState successfully removes it from the observers.
     */
    @Test
    void testRemoveObserver() {
        GameStateObserver observer = Mockito.mock(GameStateObserver.class);

        gameState.addObserver(observer);
        assertFalse(gameState.getObservers().isEmpty());

        gameState.removeObserver(observer);
        assertTrue(gameState.getObservers().isEmpty());
    }

    /**
     * Test case to verify that removing an observer from the GameState
     * that does not exist does not affect the observers collection
     * of the Game State.
     */
    @Test
    void testRemoveObserverNonExistent() {
        GameStateObserver observer = Mockito.mock(GameStateObserver.class);
        GameStateObserver observer2 = Mockito.mock(GameStateObserver.class);

        gameState.addObserver(observer);
        assertFalse(gameState.getObservers().isEmpty());

        gameState.removeObserver(observer2);
        assertFalse(gameState.getObservers().isEmpty());
    }

    /**
     * Test case to verify that adding multiple observers to the
     * GameState successfully adds them all.
     */
    @Test
    void testAddMultipleObservers() {
        final int observers = 5;

        for (int i = 0; i < observers; ++i) {
            GameStateObserver observer = Mockito.mock(GameStateObserver.class);
            gameState.addObserver(observer);
        }

        assertEquals(observers, gameState.getObservers().size());
    }

    /**
     * Test case to verify that when the Player wins,
     * the Game is stopped and the single attached
     * observer is notified of the victory.
     */
    @Test
    void testPlayerWinOneObserver() {
        final int winnerId = 1;
        final Player winner = gameState.getPlayers().get(winnerId);

        // Add observer to the GameState
        GameStateObserver observer = Mockito.mock(GameStateObserver.class);
        gameState.addObserver(observer);

        // Make the winnerId-th player win the game
        gameState.winGame(winnerId);

        // Verify that the observer is notified of the winner
        Mockito.verify(observer).endGame(winner);

        // Assert that game is stopped
        assertFalse(gameState.isStarted());
    }

    /**
     * Test case to verify that when the Player wins,
     * the Game is stopped and the two attached
     * observers are notified of the victory.
     */
    @Test
    void testPlayerWinTwoObservers() {
        final int winnerId = 0;
        final Player winner = gameState.getPlayers().get(winnerId);

        // Add observers to the GameState
        GameStateObserver observer = Mockito.mock(GameStateObserver.class);
        GameStateObserver observer2 = Mockito.mock(GameStateObserver.class);
        gameState.addObserver(observer);
        gameState.addObserver(observer2);

        // Make the winnerId-th player win the game
        gameState.winGame(winnerId);

        // Verify that the observers are notified of the winner
        Mockito.verify(observer).endGame(winner);
        Mockito.verify(observer2).endGame(winner);

        // Assert that game is stopped
        assertFalse(gameState.isStarted());
    }

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
        assertTrue(gameState.getRemainingBalls().contains(ball));

        // Pot the ball
        gameState.onBallPotted(ball);

        // Assert ball is no longer contained in remaining ball set
        assertFalse(gameState.getRemainingBalls().contains(ball));
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

        // Verify the active player (which is the first Player by default
        // after constructing GameState object) pots the ball
        assertEquals(players.get(0).getPottedBalls().size(), 1);

        // Ensure that both player types are updated
        // from unassigned to the right regular ball type
        assertEquals(players.get(0).getBallType(), RegularBall3D.Type.FULL);
        assertEquals(players.get(1).getBallType(), RegularBall3D.Type.STRIPED);
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
        gameState.onBallPotted(ball);

        Mockito.verify(player1).assignBallType(RegularBall3D.Type.FULL);
        Mockito.verify(player2).assignBallType(RegularBall3D.Type.STRIPED);
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
        gameState.onBallPotted(ball);

        Mockito.verify(player1).assignBallType(RegularBall3D.Type.STRIPED);
        Mockito.verify(player2).assignBallType(RegularBall3D.Type.FULL);
    }


    /**
     * Test case to verify that the game is correctly set to running.
     */
    @Test
    void testGameStateIsRunning() {
        assertFalse(gameState.isInMotion());
        gameState.setInMotion();
        assertTrue(gameState.isInMotion());
    }

    /**
     * Test case to verify the get active player.
     */
    @Test
    void testGetActivePlayer() {
        int activePlayerIndex = gameState.getPlayerTurn();
        assertEquals(players.get(activePlayerIndex), gameState.getActivePlayer());
    }

    /**
     * Test case to verify the get inactive player.
     */
    @Test
    void testGetInactivePlayer() {
        int inactivePlayerIndex = gameState.getPlayerTurn() + 1;
        assertEquals(players.get(inactivePlayerIndex), gameState.getInactivePlayer());
    }


    /**
     * Test case to verify that the next inactive player
     * becomes the active player when turn advances.
     */
    @Test
    void testGetNextActivePlayer() {
        Player inactivePlayer = gameState.getInactivePlayer();
        gameState.advanceTurn();
        assertEquals(gameState.getActivePlayer(), inactivePlayer);
    }
}
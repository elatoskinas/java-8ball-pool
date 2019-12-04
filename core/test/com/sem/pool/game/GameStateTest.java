package com.sem.pool.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
}
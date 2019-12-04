package com.sem.pool.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {
    GameState gameState;

    @BeforeEach
    public void setUp() {
        gameState = new GameState();
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
}
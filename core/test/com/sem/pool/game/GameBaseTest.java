package com.sem.pool.game;

import com.badlogic.gdx.Input;
import com.sem.pool.scene.Ball3D;
import com.sem.pool.scene.Scene3D;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for extracting common functionality for testing
 * the Game class.
 */
abstract class GameBaseTest {
    protected transient Scene3D scene;
    protected transient Input input;
    protected transient GameState gameState;
    protected transient Game game;
    protected transient List<Player> players;
    protected transient List<Ball3D> poolBalls;

    @BeforeEach
    void setUp() {
        scene = Mockito.mock(Scene3D.class);
        input = Mockito.mock(Input.class);

        players = new ArrayList<>();
        players.add(Mockito.mock(Player.class));
        players.add(Mockito.mock(Player.class));

        poolBalls = new ArrayList<>();
    }

    // Seems like an FP indicating a DU caused by the loop
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    protected void setupScenePoolBallsHelper(boolean... motion) {
        List<Ball3D> balls = new ArrayList<>();

        for (boolean b : motion) {
            Ball3D ball = Mockito.mock(Ball3D.class);
            Mockito.when(ball.isInMotion()).thenReturn(b);
            balls.add(ball);
        }

        Mockito.when(scene.getPoolBalls()).thenReturn(balls);
    }
}
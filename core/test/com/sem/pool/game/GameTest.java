package com.sem.pool.game;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.badlogic.gdx.Input;
import com.sem.pool.game.Game;
import com.sem.pool.game.GameState;
import com.sem.pool.scene.Scene3D;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class GameTest {

    /**
     * Test method to verify that the Game object is instantiated correctly.
     * (Scene and input are assigned without error)
     */
    @Test
    void testConstructor() {
        Scene3D scene = Mockito.mock(Scene3D.class);
        Input input = Mockito.mock(Input.class);
        GameState state = Mockito.mock(GameState.class);
        
        Game game = new Game(scene, input, state);
        
        assertEquals(scene, game.getScene());
        assertEquals(input, game.getInput());
        assertEquals(state, game.getState());
    }
}

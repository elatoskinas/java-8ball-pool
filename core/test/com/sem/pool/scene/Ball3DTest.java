package com.sem.pool.scene;

import com.badlogic.gdx.graphics.g3d.Model;
import com.sem.pool.scene.Ball3D;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class Ball3DTest {
    /**
     * Test method to verify that the Ball3D object instance
     * is constructed properly (the right values are set)
     */
    @Test
    public void testConstructor() {
        final int id = 3;
        final Model model = Mockito.mock(Model.class);

        Ball3D ball = new Ball3D(id, model);

        assertEquals(id, ball.getId());
        assertEquals(model, ball.getModel());
    }
}
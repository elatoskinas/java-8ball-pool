package com.sem.pool.scene;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class RegularBall3DTest extends Ball3DTest {

    /**
     * Test method to verify that the Ball3D object instance
     * is constructed properly (the right values for the id and
     * model are set) and that the ball is a FullBall.
     */
    @Test
    public void testConstructorFull() {
        final int id = 3;
        final ModelInstance model = Mockito.mock(ModelInstance.class);

        RegularBall3D ball = new RegularBall3D(id, model);
        assertEquals(id, ball.getId());
        assertEquals(model, ball.getModel());
        assertTrue(ball.getIsFull());
    }

    /**
     * Test method to verify that the Ball3D object instance
     * is constructed properly (the right values for the id and
     * model are set) and that the ball is not a FullBall.
     */
    @Test
    public void testConstructorStriped() {
        final int id = 14;
        final ModelInstance model = Mockito.mock(ModelInstance.class);

        RegularBall3D ball = new RegularBall3D(id, model);

        assertEquals(id, ball.getId());
        assertEquals(model, ball.getModel());
        assertFalse(ball.getIsFull());
    }

    /**
     * Tests the constructor.
     */
    @Override
    public void testConstructor() {
        final int id = 14;
        final ModelInstance model = Mockito.mock(ModelInstance.class);

        RegularBall3D ball = new RegularBall3D(id, model);

        assertEquals(id, ball.getId());
        assertEquals(model, ball.getModel());
    }

    /**
     * Returns an instance of RegularBall3D.
     * @param id id of the ball.
     * @param model model of the ball.
     * @return Instance of a RegularBall3D.
     */
    @Override
    protected Ball3D getBall(int id, ModelInstance model) {
        return new RegularBall3D(id, model);
    }
}

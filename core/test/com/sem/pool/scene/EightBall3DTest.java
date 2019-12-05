package com.sem.pool.scene;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class EightBall3DTest extends Ball3DTest {
    /**
     * Test method to verify that the Ball3D object instance
     * is constructed properly (the right values for the id and
     * model are set).
     */
    @Test
    public void testConstructor() {
        final int id = 8;
        final ModelInstance model = Mockito.mock(ModelInstance.class);

        Ball3D ball = new EightBall3D(id, model);
        assertEquals(id, ball.getId());
        assertEquals(model, ball.getModel());
    }

    /**
     * Returns an instance of EightBall3D.
     * @param id id of the ball.
     * @param model model of the ball.
     * @return Instance of EightBall3D.
     */
    @Override
    protected Ball3D getBall(int id, ModelInstance model) {
        return new EightBall3D(id, model);
    }
}

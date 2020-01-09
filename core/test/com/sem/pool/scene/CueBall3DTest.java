package com.sem.pool.scene;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CueBall3DTest extends Ball3DTest {

    /**
     * Test method to verify that the Ball3D object instance
     * is constructed properly (the right values for the id and
     * model are set).
     */
    @Test
    public void testConstructor() {
        final int id = 0;
        final ModelInstance model = Mockito.mock(ModelInstance.class);

        Ball3D ball = new CueBall3D(id, model);
        assertEquals(id, ball.getId());
        assertEquals(model, ball.getModel());
    }

    @Test
    public void testPot() {
        float speed = 5.1f;
        ModelInstance mockModel = Mockito.mock(ModelInstance.class);
        mockModel.transform = Mockito.mock(Matrix4.class);
        Ball3D ball = getBall(0, mockModel);
        ball.setSpeed(speed);

        assertEquals(speed, ball.getSpeed());

        ball.pot();

        assertEquals(0, ball.getSpeed());
        assertEquals(new Vector3(), ball.getDirection());
    }

    /**
     * Returns an instance of CueBall3D.
     * @param id id of the ball.
     * @param model model of the ball.
     * @return Instance of CueBall3D.
     */
    @Override
    protected Ball3D getBall(int id, ModelInstance model) {
        return new CueBall3D(id, model);
    }
}

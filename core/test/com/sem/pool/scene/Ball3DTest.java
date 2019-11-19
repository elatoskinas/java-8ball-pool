package com.sem.pool.scene;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
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
        final ModelInstance model = Mockito.mock(ModelInstance.class);

        Ball3D ball = new Ball3D(id, model);

        assertEquals(id, ball.getId());
        assertEquals(model, ball.getModel());
    }

    @Test
    public void testEquals() {
        final int id = 1;
        final ModelInstance model = Mockito.mock(ModelInstance.class);

        Ball3D ball1 = new Ball3D(id, model);
        Ball3D ball2 = new Ball3D(id, model);

        assertEquals(ball1, ball2);
    }

    @Test
    public void testNotEqualId() {
        final int id1 = 1;
        final int id2 = 2;
        final ModelInstance model = Mockito.mock(ModelInstance.class);

        Ball3D ball1 = new Ball3D(id1, model);
        Ball3D ball2 = new Ball3D(id2, model);

        assertNotEquals(ball1, ball2);
    }

    @Test
    public void testNotEqualsModelInstance() {
        final int id = 1;
        final ModelInstance model1 = Mockito.mock(ModelInstance.class);
        final ModelInstance model2 = Mockito.mock(ModelInstance.class);

        Ball3D ball1 = new Ball3D(id, model1);
        Ball3D ball2 = new Ball3D(id, model2);

        assertNotEquals(ball1, ball2);
    }
}
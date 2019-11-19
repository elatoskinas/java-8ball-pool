package com.sem.pool;

import com.badlogic.gdx.graphics.g3d.Model;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class Ball3DTest {
    @Test
    public void testConstructor() {
        final int id = 3;
        final Model model = Mockito.mock(Model.class);

        Ball3D ball = new Ball3D(id, model);

        assertEquals(id, ball.getId());
        assertEquals(model, ball.getModel());
    }
}
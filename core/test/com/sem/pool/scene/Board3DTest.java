package com.sem.pool.scene;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class Board3DTest {
    @Test
    public void testConstructor() {
        ModelInstance model = Mockito.mock(ModelInstance.class);
        Board3D board = new Board3D(model);

        assertEquals(model, board.getModel());
    }
}
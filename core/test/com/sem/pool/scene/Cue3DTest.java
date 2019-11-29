package com.sem.pool.scene;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Test class containing unit tests for the Cue3D class.
 */
public class Cue3DTest {

    /**
     * Test case to verify that the constructor of Cue3D
     * properly sets the model of the cue.
     */
    @Test
    public void testConstructor() {
        ModelInstance model = Mockito.mock(ModelInstance.class);
        Cue3D cue = new Cue3D(model);

        assertEquals(model, cue.getModel());
    }
}

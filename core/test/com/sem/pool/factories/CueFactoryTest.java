package com.sem.pool.factories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.sem.pool.scene.Cue3D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Test class containing unit tests for the CueFactory class and
 * some integration testing to test integration between CueFactory and
 * Cue3D classes.
 */
public class CueFactoryTest {

    transient CueFactory factory;
    transient Texture texture;
    transient AssetLoader assetLoader;

    /**
     * Sets up the test fixture by creating mock
     * dependencies & instantiating a CueFactory with
     * the mocked dependencies.
     */
    @BeforeEach
    public void setUp() {
        texture = Mockito.mock(Texture.class);
        assetLoader = Mockito.mock(AssetLoader.class);
        factory = new CueFactory(texture, assetLoader);
    }

    /**
     * Test to verify that the dependencies are set correctly
     * upon instantiating a CueFactory object.
     */
    @Test
    public void testConstructor() {
        assertEquals(texture, factory.getTexture());
    }

    /**
     * Test to verify the setter of the Texture for the
     * TextureFactory.
     */
    @Test
    public void testSetTexture() {
        Texture sampleTexture = Mockito.mock(Texture.class);

        factory.setTexture(sampleTexture);
        assertEquals(sampleTexture, factory.getTexture());
    }

    /**
     * Test case to verify that creating a Cue via the CueFactory
     * creates a Cue with the appropriate Cue model.
     */
    @Test
    public void testCreateCue() {
        final ModelInstance model = Mockito.mock(ModelInstance.class);

        Mockito.when(assetLoader.loadModel(CueFactory.MODEL_TYPE)).thenReturn(model);

        Cue3D cue = factory.createObject();

        Cue3D expectedCue = new Cue3D(model);
        assertEquals(expectedCue.getModel(), cue.getModel());
    }
}

package com.sem.pool.scene;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Test class containing unit tests for the BallFactory class and
 * some integration testing to test integration between BallFactory and
 * Ball3D classes.
 */
class BallFactoryTest {
    transient BallFactory factory;
    transient List<Texture> textures;
    transient AssetLoader assetLoader;

    @BeforeEach
    public void setUp() {
        textures = new ArrayList<Texture>();
        assetLoader = Mockito.mock(AssetLoader.class);
        factory = new BallFactory(textures, assetLoader);
    }

    /**
     * Test case to verify that the BallFactory constructor
     * properly assigns the textures to the BallFactory.
     */
    @Test
    public void testConstructor() {
        assertEquals(textures, factory.getTextures());
    }

    /**
     * Test case to ensure that creating a Pool Ball via the
     * BallFactory returns a Pool Ball with the correct id and
     * a new Ball model.
     */
    @Test
    public void testCreateBall() {
        final int id = 0;
        final ModelInstance model = Mockito.mock(ModelInstance.class);

        // We must also verify that the appropriate call is made in the asset loader.
        Mockito.when(assetLoader.loadModel(BallFactory.MODEL_TYPE)).thenReturn(model);

        Ball3D ball = factory.createBall(id);

        Ball3D expectedBall = new Ball3D(id, model);
        assertEquals(expectedBall, ball);
    }

    /**
     * Test case to ensure that the setter for the textures of
     * the BallFactory accordingly sets the textures.
     */
    @Test
    public void testSetTextures() {
        // Create some new List of textures
        List<Texture> newTextures = new ArrayList<>();
        Texture sampleTexture = Mockito.mock(Texture.class);
        newTextures.add(sampleTexture);

        factory.setTextures(newTextures);

        assertEquals(newTextures, factory.getTextures());
    }
}
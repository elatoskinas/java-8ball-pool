package com.sem.pool.factories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.sem.pool.game.GameConstants;
import com.sem.pool.scene.Ball3D;
import com.sem.pool.scene.CueBall3D;
import com.sem.pool.scene.EightBall3D;
import com.sem.pool.scene.RegularBall3D;

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
        textures = new ArrayList<>();
        assetLoader = Mockito.mock(AssetLoader.class);
        factory = new BallFactory(textures, assetLoader);
        Bullet.init();
    }

    /**
     * Test that the balls returned by the ball factory are the right type of ball.
     */
    @Test
    public void testBallTypes() {
        ModelInstance mockModelInstance = Mockito.mock(ModelInstance.class);
        Mockito.when(assetLoader.loadModel(AssetLoader.ModelType.BALL))
                .thenReturn(mockModelInstance);
        
        for (int i = 0; i < GameConstants.BALL_COUNT; i++) {
            Ball3D ball = factory.createBall(i);
            if (i == GameConstants.CUEBALL_ID) {
                assertTrue(ball instanceof CueBall3D);
            } else if (i == GameConstants.EIGHTBALL_ID) {
                assertTrue(ball instanceof EightBall3D);
            } else {
                assertTrue(ball instanceof RegularBall3D);
                assertEquals(i < GameConstants.EIGHTBALL_ID,
                        (RegularBall3D.Type.FULL == ((RegularBall3D) ball).getType()));
            }
        }
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

        Ball3D expectedBall = new CueBall3D(id, model);
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

    /**
     * Test case to ensure that creating a ball with
     * id 0 sets the texture to the texture with id 0.
     */
    @Test
    public void testBallTextureAssignedCueBall() {
        final int ballId = 0;
        final int textureId = 0;
        final int textureCount = 2;

        testBallTextureAssignHelper(ballId, textureId, textureCount);
    }

    /**
     * Test case to ensure that creating a ball with an
     * id not equal to 0 sets the texture to the texture
     * with the same id as the ball id.
     */
    @Test
    public void testBallTextureAssignedNonCueBall() {
        final int ballId = 2;
        final int textureId = 2;
        final int textureCount = 4;

        testBallTextureAssignHelper(ballId, textureId, textureCount);
    }

    /**
     * Test case to ensure that creating a ball with a ball id
     * larger than the textures size wraps the texture id to
     * stay within bounds.
     */
    @Test
    public void testBallTextureAssignedLargerIndex() {
        final int ballId = 3;
        final int textureId = 0;
        final int textureCount = 2;

        testBallTextureAssignHelper(ballId, textureId, textureCount);
    }

    /**
     * Test case to ensure that creating a ball with a ball id
     * equal to the last id in the textures list sets the id
     * properly (test for off-by-one).
     */
    @Test
    public void testBallTextureAssignedLastBall() {
        final int ballId = 3;
        final int textureId = 3;
        final int textureCount = 4;

        testBallTextureAssignHelper(ballId, textureId, textureCount);
    }

    /**
     * Helper method to verify the setting of id given a creation of
     * a ball with specified id, the texture count and the expected texture id.
     * The method creates a List of mock textures equal to the specified count
     * for verification.
     * @param ballId  ID of the ball to create
     * @param textureId ID of the expected texture
     * @param textureCount Number of mock textures to initialize
     */
    private void testBallTextureAssignHelper(int ballId, int textureId, int textureCount) {
        // Add the specified texture count of textures to the
        // textures of the BallFactory.
        for (int i = 0; i < textureCount; ++i) {
            Texture mockTexture = Mockito.mock(Texture.class);
            textures.add(mockTexture);
        }

        // Create model dependency and make the AssetLoader return
        // the specified Model when loading Pool Ball models
        final ModelInstance model = Mockito.mock(ModelInstance.class);
        Mockito.when(assetLoader.loadModel(BallFactory.MODEL_TYPE)).thenReturn(model);

        // Create material dependency (note that a non-mock is used, this is to
        // prevent errors when using material.set(), which causes an NPE
        // when done with a mock)
        final Material material = new Material();

        // Make the model return the specified material when getting
        // a material for the name "ball"

        Mockito.when(model.getMaterial(BallFactory.BALL_MATERIAL_NAME)).thenReturn(material);

        // Get the expected texture ID, and create the required attribute
        // for the texture
        Texture texture = textures.get(textureId);
        TextureAttribute attribute = TextureAttribute.createDiffuse(texture);

        // Finally, create the Ball model using the factory and
        // ensure that the texture attribute has been set correctly.
        Ball3D ball = factory.createBall(ballId);

        // Get the attribute set to the material of the created ball model
        Attribute resultingAttribute = ball.getModel()
                .getMaterial(BallFactory.BALL_MATERIAL_NAME)
                .get(attribute.type);

        // Verify attribute matches expected
        assertEquals(attribute, resultingAttribute);
    }

    @Test
    public void testAssertions() {
        final ModelInstance model = Mockito.mock(ModelInstance.class);

        // We must also verify that the appropriate call is made in the asset loader.
        Mockito.when(assetLoader.loadModel(BallFactory.MODEL_TYPE)).thenReturn(model);

        assertThrows(AssertionError.class, () ->  {
            factory.createBall(-1);
        });
        assertThrows(AssertionError.class, () ->  {
            factory.createBall(16);
        });
    }
}
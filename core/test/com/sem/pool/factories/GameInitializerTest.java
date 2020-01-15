package com.sem.pool.factories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.sem.pool.game.Game;
import com.sem.pool.scene.Scene3D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Test case that tests the creation of factories and
 * games presented in the Game Initializer class. This
 * class also acts as an integration test between all the
 * factories, namely the Scene Factory.
 */
class GameInitializerTest {
    private transient AssetLoader assetLoader;
    private transient ModelBatch modelBatch;
    private transient Input input;

    private transient Vector2 resolution;
    private transient Vector3 cameraPosition;

    private transient GameInitializer gameInitializer;

    @BeforeEach
    public void setUp() {
        // Arbitrary width & height params
        final int width = 100;
        final int height = 100;

        // Mock & instantiate all needed dependencies
        assetLoader = Mockito.mock(AssetLoader.class);
        modelBatch = Mockito.mock(ModelBatch.class);
        input = Mockito.mock(Input.class);
        resolution = new Vector2(width, height);
        cameraPosition = Vector3.Z;

        // Create new game initializer
        gameInitializer = new GameInitializer(assetLoader, modelBatch, input,
                resolution, cameraPosition);

        Bullet.init();
    }

    /**
     * Test case to verify that a camera factory is created successfully
     * upon calling the camera factory creation method.
     */
    @Test
    void testCreateCameraFactory() {
        Camera injected = injectCameraCreator();
        CameraFactory factory = gameInitializer.createCameraFactory();

        assertNotNull(factory);
        assertNotNull(factory.createCamera());
        assertEquals(injected, factory.createCamera());
    }

    /**
     * Test case to verify that a Ball Factory is created successfully
     * upon calling the ball factory creation method.
     */
    @Test
    void testCreateBallFactory() {
        BallFactory factory = gameInitializer.createBallFactory();

        // Load mock model instance upon loading ball
        Mockito.when(assetLoader.loadModel(AssetLoader.ModelType.BALL))
                .thenReturn(Mockito.mock(ModelInstance.class));

        assertNotNull(factory);
        assertEquals(assetLoader.getBallTextures(), factory.getTextures());
        assertNotNull(factory.createObject());
    }

    /**
     * Test case to verify that a Cue factory is created successfully
     * upon calling the cue factory creation method.
     */
    @Test
    void testCreateCueFactory() {
        setupMockModelInstantiation();
        CueFactory factory = gameInitializer.createCueFactory();

        assertNotNull(factory);
        assertEquals(assetLoader.getCueTexture(), factory.getTexture());
        assertNotNull(factory.createObject());
    }

    /**
     * Test case to verify that a Table factory is created successfully
     * upon calling the table factory creation method.
     */
    @Test
    void testCreateTableFactory() {
        TableFactory factory = gameInitializer.createTableFactory();

        // Load mock model instance upon loading table
        Mockito.when(assetLoader.loadModel(AssetLoader.ModelType.TABLE))
                .thenReturn(Mockito.mock(ModelInstance.class));

        assertNotNull(factory);
        assertEquals(assetLoader.getTableTexture(), factory.getTexture());
        assertNotNull(factory.createObject());
    }

    /**
     * Test case to verify that a Scene factory is created successfully upon
     * calling the scene factory creation method.
     */
    @Test
    void testCreateSceneFactory() {
        injectCameraCreator();
        setupMockModelInstantiation();

        SceneFactory factory = gameInitializer.createSceneFactory();

        assertNotNull(factory);
        Scene3D scene = factory.createScene();
        assertNotNull(scene);
    }

    /**
     * Test case to verify that a Game is created successfully
     * upon using the create game method.
     */
    @Test
    void testCreateGame() {
        injectCameraCreator();
        setupMockModelInstantiation();

        Game game = gameInitializer.createGame();

        assertNotNull(game);
        assertNotNull(game.getScene());
        assertEquals(input, game.getInput());
    }

    /**
     * Helper method to inject mock Camera Creator to make
     * camera creation related tests pass.
     * @return  injected mock Camera
     */
    private Camera injectCameraCreator() {
        CameraCreator creator = Mockito.mock(CameraCreator.class);
        Camera camera = Mockito.mock(Camera.class);
        Mockito.when(creator.createCamera(Mockito.eq(resolution),
                Mockito.anyFloat())).thenReturn(camera);
        gameInitializer.setCameraCreator(creator);
        return camera;
    }

    /**
     * Sets up mocked model instantiation for the Asset Loader
     * to avoid NPE on model interactions.
     */
    private void setupMockModelInstantiation() {
        // Set up mock model & transform to avoid NPE errors
        // related to model instances & libGDX transforming.
        ModelInstance mockModel = Mockito.mock(ModelInstance.class);
        mockModel.transform = new Matrix4();

        // Mock calculation of bounding box to allow tests to pass
        Mockito.when(mockModel.calculateBoundingBox(Mockito.any(BoundingBox.class)))
                .thenReturn(new BoundingBox());

        Mockito.when(mockModel.getMaterial(Mockito.anyString())).thenReturn(new Material());

        // Load mock model instance upon loading any model.
        Mockito.when(assetLoader.loadModel(Mockito.any()))
                .thenReturn(mockModel);
    }
}
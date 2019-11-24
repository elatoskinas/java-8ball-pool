package com.sem.pool.scene;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Test class containing unit tests for the AssetLoader class.
 */
class AssetLoaderTest {
    transient AssetLoader loader;
    transient AssetManager manager;

    @BeforeEach
    public void setUp() {
        manager = Mockito.mock(AssetManager.class);
        loader = new AssetLoader(manager);
    }

    /**
     * Test case to verify that the ModelType enum returns
     * the proper path value for the Ball ModelType.
     */
    @Test
    public void testModelTypeBallGetPath() {
        AssetLoader.ModelType type = AssetLoader.ModelType.BALL;

        String expectedPath = AssetLoader.BALL_MODEL_PATH;
        String actualPath = type.getPath();

        assertEquals(expectedPath, actualPath);
    }

    /**
     * Test case to verify that the ModelType enum returns
     * the proper path value for the Table ModelType.
     */
    @Test
    public void testModelTypeBoardGetPath() {
        AssetLoader.ModelType type = AssetLoader.ModelType.TABLE;

        String expectedPath = AssetLoader.TABLE_MODEL_PATH;
        String actualPath = type.getPath();

        assertEquals(expectedPath, actualPath);
    }

    /**
     * Test case to ensure that the constructor assigns the
     * AssetManager correctly to the AssetLoader.
     */
    @Test
    public void testConstructor() {
        assertEquals(manager, loader.getAssetManager());
    }

    /**
     * Test case to verify that loading a model via the AssetLoader
     * triggers the right calls to the underlying AssetManager.
     */
    @Test
    public void testLoadModel() {
        Model model = new Model();

        // Make the underlying manager dependency return the created
        // model upon trying to create a ball model.
        Mockito.when(manager.get(AssetLoader.BALL_MODEL_PATH, Model.class))
                .thenReturn(model);

        // Load the ball model and ensure that the underlying Manager
        // gets invoked with the retrieval of the model.
        loader.loadModel(AssetLoader.ModelType.BALL);
        Mockito.verify(manager, Mockito.times(1))
                .get(AssetLoader.BALL_MODEL_PATH, Model.class);
    }

    /**
     * Test case to ensure that calling the initializeAssets
     * method on the AssetLoader initializes the Ball & Table models
     * properly by making the right AssetManager calls.
     */
    @Test
    public void testInitializeModels() {
        loader.initializeAssets();

        Mockito.verify(manager, Mockito.times(1))
                .load(AssetLoader.BALL_MODEL_PATH, Model.class, loader.getObjectLoaderParameters());

        Mockito.verify(manager, Mockito.times(1))
                .load(AssetLoader.TABLE_MODEL_PATH, Model.class,
                        loader.getObjectLoaderParameters());

        Mockito.verifyNoMoreInteractions(manager);
    }

    /**
     * Test case to ensure that disposing the AssetLoader
     * disposes the underlying AssetManager properly.
     */
    @Test
    public void testDispose() {
        loader.dispose();
        Mockito.verify(manager).dispose();
    }

    /**
     * Test case to ensure that the ObjectLoaderParameters are
     * set to flip the texture coordinates.
     */
    @Test
    public void testGetObjectLoaderParameters() {
        ObjLoader.ObjLoaderParameters objLoaderParameters = loader.getObjectLoaderParameters();

        // Make sure that the texture coordinates are flipped
        assertTrue(objLoaderParameters.flipV);
    }
}
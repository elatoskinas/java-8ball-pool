package com.sem.pool.factories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.sem.pool.game.GameConstants;
import java.util.ArrayList;
import java.util.List;

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
    }

    /**
     * Test case to ensure that calling the initializeAssets
     * method on the AssetLoader initializes the Ball textures
     * properly by making the right AssetManager calls.
     */
    @Test
    public void testInitializeTextures() {
        loader.initializeAssets();

        Mockito.verify(manager, Mockito.times(GameConstants.BALL_COUNT))
                .load(Mockito.anyString(), eq(Texture.class));
    }

    /**
     * Test case to ensure that getting the ball textures
     * from the Asset Loader returns the full list of ball textures
     * such that the size is equal to the expected ball count,
     * and that every texture inside the returned List is initialized.
     */
    @Test
    public void testGetBallTextures() {
        Texture mockTexture = Mockito.mock(Texture.class);

        List<Texture> expectedTextures = new ArrayList<>();
        for (int i = 0; i < GameConstants.BALL_COUNT; ++i) {
            expectedTextures.add(mockTexture);
        }

        Mockito.when(manager.get(Mockito.anyString(), eq(Texture.class)))
                .thenReturn(mockTexture);

        List<Texture> textures = loader.getBallTextures();

        assertEquals(expectedTextures, textures);
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

    /**
     * Test case to ensure that the getBallSound method returns the proper
     * Music object.
     */
    @Test
    public void testGetBallSound() {
        Music mockedBallSound = Mockito.mock(Music.class);
        Mockito.when(manager.get(loader.BALL_SOUND_PATH, Music.class)).thenReturn(mockedBallSound);
        assertEquals(mockedBallSound, loader.getBallSound());
    }

    /**
     * Test case to ensure that the getCueSound method returns the proper
     * Music object.
     */
    @Test
    public void testGetCueSound() {
        Music mockedCueSound = Mockito.mock(Music.class);
        Mockito.when(manager.get(loader.CUE_SOUND_PATH, Music.class)).thenReturn(mockedCueSound);
        assertEquals(mockedCueSound, loader.getCueSound());
    }

    /**
     * Test case to ensure that the getBallSound method returns the proper
     * Music object.
     */
    @Test
    public void testGetTableSound() {
        Music mockedTableSound = Mockito.mock(Music.class);
        Mockito.when(manager.get(loader.TABLE_SOUND_PATH, Music.class))
                .thenReturn(mockedTableSound);
        assertEquals(mockedTableSound, loader.getTableSound());
    }

    /**
     * Test case to ensure that the getBallSound method returns the proper
     * Music object.
     */
    @Test
    public void testGetPotSound() {
        Music mockedPotSound = Mockito.mock(Music.class);
        Mockito.when(manager.get(loader.POT_SOUND_PATH, Music.class)).thenReturn(mockedPotSound);
        assertEquals(mockedPotSound, loader.getPotSound());
    }
}
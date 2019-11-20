package com.sem.pool.scene;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AssetLoaderTest {
    AssetLoader loader;
    AssetManager manager;

    public AssetLoader getLoader() {
        return loader;
    }

    public void setLoader(AssetLoader loader) {
        this.loader = loader;
    }

    public AssetManager getManager() {
        return manager;
    }

    public void setManager(AssetManager manager) {
        this.manager = manager;
    }

    @BeforeEach
    public void setUp() {
        manager = Mockito.mock(AssetManager.class);
        loader = new AssetLoader(manager);
    }

    @Test
    public void testModelTypeGetPath() {
        AssetLoader.ModelType type = AssetLoader.ModelType.BALL;

        String expectedPath = AssetLoader.BALL_MODEL_PATH;
        String actualPath = type.getPath();

        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void testConstructor() {
        assertEquals(manager, loader.getAssetManager());
    }

    @Test
    public void testLoadModel() {
        Model model = Mockito.spy(Model.class);

        Mockito.when(manager.get(AssetLoader.BALL_MODEL_PATH, Model.class))
                .thenReturn(model);

        loader.loadModel(AssetLoader.ModelType.BALL);
        Mockito.verify(manager, Mockito.times(1))
                .get(AssetLoader.BALL_MODEL_PATH, Model.class);
    }

    @Test
    public void testInitializeModels() {
        loader.initializeModels();

        Mockito.verify(manager, Mockito.times(1))
                .load(AssetLoader.BALL_MODEL_PATH, Model.class);

        Mockito.verify(manager, Mockito.times(1))
                .load(AssetLoader.BOARD_MODEL_PATH, Model.class);

        Mockito.verifyNoMoreInteractions(manager);
    }
}
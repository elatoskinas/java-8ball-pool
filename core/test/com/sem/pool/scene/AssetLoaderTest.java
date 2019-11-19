package com.sem.pool.scene;

import com.badlogic.gdx.assets.AssetManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class AssetLoaderTest {
    AssetLoader loader;
    AssetManager manager;

    @BeforeEach
    public void setup() {
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
}
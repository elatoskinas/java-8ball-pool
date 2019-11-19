package com.sem.pool.scene;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class AssetLoaderTest {
    AssetLoader loader;

    @BeforeEach
    public void setup() {
        loader = new AssetLoader();
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
        assertNotNull(loader.getAssetManager());
    }
}
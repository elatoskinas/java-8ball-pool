package com.sem.pool;

import com.sem.pool.scene.AssetLoader;
import com.sem.pool.scene.Scene3D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Sample test class. To be removed in the future.
 */
class PoolTest {
    transient Pool pool;

    @BeforeEach
    public void setUp() {
        pool = new Pool();
    }

    @Test
    public void testCreate() {
        pool.create();

        AssetLoader loader = pool.getAssetLoader();
        Scene3D scene = pool.getScene();

        // Assert that loader & scene have been properly created
        assertNotNull(loader);
        assertNotNull(scene);

        // Ensure asset loader is set to the game's Asset Loader
        assertEquals(loader, scene.getAssetLoader());

        // Ensure that assets have been queued for loading (part of initialization)
        assertNotEquals(0, scene.getAssetLoader().getAssetManager().getQueuedAssets());
    }
}
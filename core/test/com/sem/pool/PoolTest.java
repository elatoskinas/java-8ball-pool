package com.sem.pool;

import com.sem.pool.screens.Pool;
import org.junit.jupiter.api.BeforeEach;

/**
 * Sample test class. To be removed in the future.
 * TODO: For now, the Pool class was developed without tests beforehand.
 * TODO: This is due to Pool making rendering & asset loading calls,
 * TODO: which are difficult to test with the LibGDX architecture.
 * TODO: In the future, tests should be extended to test the workflow
 * TODO: of the Pool class.
 */
class PoolTest {
    transient Pool pool;

    /*@BeforeEach
    public void setUp() {
        pool = new Pool();
    }*/

    /*@Test
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
    }*/
}
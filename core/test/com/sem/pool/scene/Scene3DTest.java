package com.sem.pool.scene;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class Scene3DTest {
    private static final int BALL_COUNT = 16;

    transient Scene3D scene;
    transient AssetLoader assetLoader;
    transient ModelBatch batch;

    transient BallFactory ballFactory;
    transient TableFactory tableFactory;
    transient Camera camera;

    @BeforeEach
    public void setUp() {
        assetLoader = Mockito.mock(AssetLoader.class);
        batch = Mockito.mock(ModelBatch.class);
        scene = new Scene3D(assetLoader, batch);

        ballFactory = Mockito.mock(BallFactory.class);
        tableFactory = Mockito.mock(TableFactory.class);
        camera = Mockito.mock(Camera.class);
    }

    @Test
    public void testConstructor() {
        assertEquals(assetLoader, scene.getAssetLoader());
    }

    /*@Test
    public void testSetEnvironment() {
        Environment environment = new Environment();
        scene.setEnvironment(environment);

        assertEquals(environment, scene.getEnvironment());
    }

    @Test
    public void testSetCamera() {
        Camera camera = new PerspectiveCamera();
        scene.setCamera(camera);

        assertEquals(camera, scene.getCamera());
    }*/

    /**
     * Test case to ensure that all the required elements of
     * the 3D scene are instantiated after calling instantiate().
     */
    @Test
    public void testInstantiateSuccessful() {
        scene.instantiate();
        assertNotNull(scene.getEnvironment());
        assertNotNull(scene.getCamera());
        assertNotNull(scene.getPoolBalls());
        assertNotNull(scene.getTable());
    }

    /**
     * Test to verify that the models are properly instantiated
     * after calling instantiate() on the scene.
     */
    @Test
    public void testInstantiateModels() {
        scene.instantiate(ballFactory, tableFactory, camera);

        assertNotNull(scene.getModels());

        // Total amount of models to load: ball count + 1 for the board
        final int modelCount = BALL_COUNT + 1;
        assertEquals(modelCount, scene.getModels().size());
    }

    /**
     * Test case to verify that the necessary render-related
     * calls are made with the scene's elements.
     */
    @Test
    public void testRenderModels() {
        scene.render();

        // Get the elements of the scene that should be used
        // in the rendering process
        Camera camera = scene.getCamera();
        List<ModelInstance> models = scene.getModels();
        Environment environment = scene.getEnvironment();

        // Verify that all the necessary render calls have been made
        Mockito.verify(batch).begin(camera);
        Mockito.verify(batch).render(models, environment);
        Mockito.verify(batch).end();
    }

    /**
     * Test case to ensure that after calling the dispose method
     * on the scene, all the necessary calls are made to clean
     * up the scene & models.
     * The test case covers the transition from game instantiation to disposal.
     */
    @Test
    public void testDispose() {
        scene.instantiate(ballFactory, tableFactory, camera);
        scene.dispose();

        Mockito.verify(batch).dispose();
        assertEquals(0, scene.getModels().size());
        Mockito.verify(assetLoader).dispose();
    }
}
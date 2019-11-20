package com.sem.pool.scene;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

class Scene3DTest {
    Scene3D scene;
    AssetLoader assetLoader;
    ModelBatch batch;

    @BeforeEach
    public void setUp() {
        assetLoader = Mockito.mock(AssetLoader.class);
        batch = Mockito.mock(ModelBatch.class);
        scene = new Scene3D(assetLoader, batch);
    }

    public Scene3D getScene() {
        return scene;
    }

    public void setScene(Scene3D scene) {
        this.scene = scene;
    }

    public AssetLoader getAssetLoader() {
        return assetLoader;
    }

    public void setAssetLoader(AssetLoader assetLoader) {
        this.assetLoader = assetLoader;
    }

    public ModelBatch getBatch() {
        return batch;
    }

    public void setBatch(ModelBatch batch) {
        this.batch = batch;
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
        scene.instantiate();

        assertNotNull(scene.getModels());

        // TODO: Change this to check for all 16 balls
        // Total amount of models to load: ball count + 1 for the board
        final int ballCount = 2;
        final int modelCount = ballCount + 1;
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
}
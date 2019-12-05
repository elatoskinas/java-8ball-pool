package com.sem.pool.scene;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Test class containing unit tests for the Scene3D class.
 */
class Scene3DTest {
    transient Scene3D scene;

    transient ModelBatch batch;
    transient Camera camera;
    transient Environment environment;

    transient List<Ball3D> poolBalls;
    transient Table3D table;
    transient Cue3D cue;

    /**
     * Handles setting up the test fixture by
     * creating mocks of all the required dependencies
     * for creating a Scene object instance.
     */
    @BeforeEach
    public void setUp() {
        batch = Mockito.mock(ModelBatch.class);
        camera = Mockito.mock(Camera.class);
        environment = Mockito.mock(Environment.class);

        table = Mockito.mock(Table3D.class);
        poolBalls = new ArrayList<>();
        cue = Mockito.mock(Cue3D.class);

        scene = new Scene3D(environment, camera, poolBalls, table, cue, batch);
    }

    /**
     * Test to verify that the correct objects are set to the
     * scene's parameters upon constructing a new Scene.
     */
    @Test
    public void testConstructor() {
        assertEquals(camera, scene.getCamera());
        assertEquals(environment, scene.getEnvironment());
        assertEquals(poolBalls, scene.getPoolBalls());
        assertEquals(table, scene.getTable());
    }

    /**
     * Test to verify that the correct number of models
     * are added to the scene's model List upon constructing
     * a Scene object.
     */
    @Test
    public void testConstructorModelsSize() {
        // Poolballs + Table + Cue
        int expectedSize = poolBalls.size() + 2;
        int actualSize = scene.getModels().size();
        assertEquals(expectedSize, actualSize);
    }

    /**
     * Test to verify that the correct number of models
     * are added to the scene's model list upon specifying
     * a non-empty List of pool balls as one of the parameters
     * for the Scene to instantiate.
     */
    @Test
    public void testConstructorModelsSizeBallsPresent() {
        final int ballCount = 6;

        List<Ball3D> poolBalls2 = new ArrayList<>();

        for (int i = 0; i < ballCount; ++i) {
            poolBalls2.add(Mockito.mock(Ball3D.class));
        }

        scene = new Scene3D(environment, camera, poolBalls2, table, cue, batch);

        // Poolballs + Table + Cue
        int expectedSize = poolBalls2.size() + 2;
        int actualSize = scene.getModels().size();
        assertEquals(expectedSize, actualSize);
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
     */
    @Test
    public void testDispose() {
        scene.dispose();

        Mockito.verify(batch).dispose();
        assertEquals(0, scene.getModels().size());
    }
}
package com.sem.pool.scene;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class Scene3DTest {
    private static final int BALL_COUNT = 16;

    transient Scene3D scene;

    transient ModelBatch batch;
    transient Camera camera;
    transient Environment environment;

    transient List<Ball3D> poolBalls;
    transient Table3D table;

    @BeforeEach
    public void setUp() {
        batch = Mockito.mock(ModelBatch.class);
        camera = Mockito.mock(Camera.class);
        environment = Mockito.mock(Environment.class);

        Table3D table = Mockito.mock(Table3D.class);
        poolBalls = new ArrayList<Ball3D>();

        scene = new Scene3D(environment, camera, poolBalls, table, batch);
    }

    @Test
    public void testConstructor() {
        assertEquals(camera, scene.getCamera());
        assertEquals(environment, scene.getEnvironment());
        assertEquals(poolBalls, scene.getPoolBalls());
        assertEquals(table, scene.getTable());
    }

    @Test
    public void testConstructorModelsSize() {
        int expectedSize = poolBalls.size() + 1;
        int actualSize = scene.getModels().size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testConstructorModelsSizeBallsPresent() {
        final int ballCount = 6;

        List<Ball3D> poolBalls2 = new ArrayList<Ball3D>();

        for (int i = 0; i < ballCount; ++i) {
            poolBalls2.add(Mockito.mock(Ball3D.class));
        }

        int expectedSize = poolBalls2.size() + 1;
        int actualSize = scene.getModels().size();
        assertEquals(expectedSize, actualSize);
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
        scene.dispose();

        Mockito.verify(batch).dispose();
        assertEquals(0, scene.getModels().size());
    }
}
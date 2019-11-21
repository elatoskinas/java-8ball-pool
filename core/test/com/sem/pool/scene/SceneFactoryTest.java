package com.sem.pool.scene;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class SceneFactoryTest {
    private static final int BALL_COUNT = 16;

    SceneFactory sceneFactory;

    transient BallFactory ballFactory;
    transient TableFactory tableFactory;
    transient Camera camera;
    transient ModelBatch modelBatch;

    @BeforeEach
    public void setUp() {
        ballFactory = Mockito.mock(BallFactory.class);
        tableFactory = Mockito.mock(TableFactory.class);
        camera = Mockito.mock(Camera.class);
        modelBatch = Mockito.mock(ModelBatch.class);

        sceneFactory = new SceneFactory(tableFactory, ballFactory, camera, modelBatch);
    }


    @Test
    public void testConstructor() {
        assertEquals(ballFactory, sceneFactory.getBallFactory());
        assertEquals(tableFactory, sceneFactory.getTableFactory());
        assertEquals(camera, sceneFactory.getCamera());
    }

    @Test
    public void testSetBallFactory() {
        BallFactory newFactory = Mockito.mock(BallFactory.class);
        sceneFactory.setBallFactory(newFactory);

        assertEquals(newFactory, sceneFactory.getBallFactory());
    }

    @Test
    public void testSetTableFactory() {
        TableFactory newFactory = Mockito.mock(TableFactory.class);
        sceneFactory.setTableFactory(newFactory);

        assertEquals(newFactory, sceneFactory.getTableFactory());
    }

    @Test
    public void testSetCamera() {
        Camera camera = Mockito.mock(Camera.class);
        sceneFactory.setCamera(camera);

        assertEquals(camera, sceneFactory.getCamera());
    }

    /**
     * Test case to ensure that all the required elements of
     * the 3D scene are instantiated after constructing a Scene.
     */
    @Test
    public void testInstantiateSuccessful() {
        Scene3D scene = sceneFactory.createScene();

        assertNotNull(scene.getEnvironment());
        assertNotNull(scene.getCamera());
        assertNotNull(scene.getPoolBalls());
        assertNotNull(scene.getTable());
    }

    /**
     * Test to verify that the models are properly instantiated
     * after instantiating the scene.
     */
    @Test
    public void testInstantiateModels() {
        Scene3D scene = sceneFactory.createScene();

        assertNotNull(scene.getModels());

        // Total amount of models to load: ball count + 1 for the board
        final int modelCount = BALL_COUNT + 1;
        assertEquals(modelCount, scene.getModels().size());
    }

}
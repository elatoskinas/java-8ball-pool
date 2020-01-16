package com.sem.pool.factories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.sem.pool.game.GameConstants;
import com.sem.pool.scene.Ball3D;
import com.sem.pool.scene.Cue3D;
import com.sem.pool.scene.Scene3D;
import com.sem.pool.scene.SoundPlayer;
import com.sem.pool.scene.Table3D;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


/**
 * Test class containing unit tests for the SceneFactory class and
 * some integration testing to test integration between SceneFactory and
 * Scene3D classes.
 */
class SceneFactoryTest {
    transient SceneFactory sceneFactory;
    transient BallFactory ballFactory;
    transient TableFactory tableFactory;
    transient CameraFactory cameraFactory;
    transient CueFactory cueFactory;
    transient ModelBatch modelBatch;
    transient SoundPlayer soundPlayer;

    /**
     * Handles setting up the test fixture by
     * creating mock dependencies for the SceneFactory
     * which return non-null objects upon using the create
     * functionality.
     */
    @BeforeEach
    public void setUp() {
        ballFactory = Mockito.mock(BallFactory.class);
        tableFactory = Mockito.mock(TableFactory.class);
        cameraFactory = Mockito.mock(CameraFactory.class);
        cueFactory = Mockito.mock(CueFactory.class);
        modelBatch = Mockito.mock(ModelBatch.class);
        soundPlayer = Mockito.mock(SoundPlayer.class);


        sceneFactory =
                new SceneFactory(tableFactory, ballFactory, cameraFactory, cueFactory, modelBatch,
                                soundPlayer);

        Mockito.when(tableFactory.createObject())
                .thenReturn(Mockito.mock(Table3D.class));

        Mockito.when(ballFactory.createObject())
                .thenReturn(Mockito.mock(Ball3D.class));

        Mockito.when(cameraFactory.createCamera())
                .thenReturn(Mockito.mock(Camera.class));

        Mockito.when(cueFactory.createObject())
                .thenReturn(Mockito.mock(Cue3D.class));
    }


    /**
     * Test to verify that the construction of a SceneFactory
     * object instance sets the elements of the SceneFactory
     * properly.
     */
    @Test
    public void testConstructor() {
        assertEquals(ballFactory, sceneFactory.getBallFactory());
        assertEquals(tableFactory, sceneFactory.getTableFactory());
        assertEquals(cameraFactory, sceneFactory.getCameraFactory());
    }

    /**
     * Test case to verify the BallFactory setter of
     * the SceneFactory.
     */
    @Test
    public void testSetBallFactory() {
        BallFactory newFactory = Mockito.mock(BallFactory.class);
        sceneFactory.setBallFactory(newFactory);

        assertEquals(newFactory, sceneFactory.getBallFactory());
    }

    /**
     * Test case to verify the TableFactory setter of
     * the SceneFactory.
     */
    @Test
    public void testSetTableFactory() {
        TableFactory newFactory = Mockito.mock(TableFactory.class);
        sceneFactory.setTableFactory(newFactory);

        assertEquals(newFactory, sceneFactory.getTableFactory());
    }

    /**
     * Test case to verify the Camera setter of
     * the SceneFactory.
     */
    @Test
    public void testSetCameraFactory() {
        CameraFactory cameraFactory = Mockito.mock(CameraFactory.class);
        sceneFactory.setCameraFactory(cameraFactory);

        assertEquals(cameraFactory, sceneFactory.getCameraFactory());
    }

    /**
     * Test case to ensure that all the required elements of
     * the 3D scene are instantiated after constructing a Scene.
     */
    @Test
    public void testInstantiateSuccessful() {
        Bullet.init();
        Scene3D scene = sceneFactory.createScene();

        assertNotNull(scene.getEnvironment());
        assertNotNull(scene.getCamera());
        assertNotNull(scene.getPoolBalls());
        assertNotNull(scene.getTable());
    }

    /**
     * Test to verify that the game elements
     * (pool balls and table) are properly instantiated
     * in the scene created by the SceneFactory.
     */
    @Test
    public void testInstantiateModels() {
        Bullet.init();
        Scene3D scene = sceneFactory.createScene();

        // Verify the required amount of pool balls for scene
        assertNotNull(scene.getPoolBalls());
        assertEquals(GameConstants.BALL_COUNT, scene.getPoolBalls().size());

        // Verify that the table is instantiated
        assertNotNull(scene.getTable());
    }

}
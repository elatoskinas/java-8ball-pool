package com.sem.pool.scene;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class SceneFactoryTest {
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
        sceneFactory.setCamera(newFactory);

        assertEquals(camera, sceneFactory.getCamera());
    }
}
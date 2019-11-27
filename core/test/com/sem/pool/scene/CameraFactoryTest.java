package com.sem.pool.scene;

import org.junit.jupiter.api.BeforeEach;

import org.mockito.Mockito;

public class CameraFactoryTest {

    transient CameraFactory cameraFactory;

    // TODO: Create a scene to use gdx.graphics width and height
    // TODO: Use a camera config file to test with the same values as in src
    @BeforeEach
    public void setUp() {
    //  Vector3 position = new Vector3(0f,1f,0f);
    //  float width = Gdx.graphics.getWidth();
    //  float height = Gdx.graphics.getHeight();
        cameraFactory = Mockito.mock(CameraFactory.class);
    }

}
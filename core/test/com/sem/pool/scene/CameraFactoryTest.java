package com.sem.pool.scene;

import com.badlogic.gdx.graphics.Camera;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

public class CameraFactoryTest {

    transient CameraFactory cameraFactory;

    @BeforeEach
    public void setUp() {
        cameraFactory = Mockito.mock(CameraFactory.class);

        Mockito.when(cameraFactory.createCamera())
                .thenReturn(Mockito.mock(Camera.class));
    }

}
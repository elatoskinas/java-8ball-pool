package com.sem.pool.scene;

import org.junit.jupiter.api.BeforeEach;

import org.mockito.Mockito;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class CameraFactoryTest {

    transient CameraFactory cameraFactory;

    // TODO: Create a scene to use gdx.graphics width and height
    // TODO: Use a camera config file to test with the same values as in src
    @BeforeEach
    public void setUp() {
//        Vector3 position = new Vector3(0f,1f,0f);
//        float width = Gdx.graphics.getWidth();
//        float height = Gdx.graphics.getHeight();
        cameraFactory = Mockito.mock(CameraFactory.class);

    }
//
//    /**
//     * Test case to verify that creating a Table via the TableFactory
//     * creates a Table with the appropriate table model.
//     */
//    @Test
//    public void testCreateCamera() {
//        Camera camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        assertEquals(cameraFactory.createCamera(), camera);
//    }
}
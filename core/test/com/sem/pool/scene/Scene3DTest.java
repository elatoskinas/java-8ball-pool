package com.sem.pool.scene;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sun.tools.java.Environment;

class Scene3DTest {
    Scene3D scene;
    AssetLoader assetLoader;

    @BeforeEach
    public void setUp() {
        assetLoader = Mockito.mock(AssetLoader.class);
        scene = new Scene3D(assetLoader);
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

    @Test
    public void testConstructor() {
        assertEquals(assetLoader, scene.getAssetLoader());
    }

    @Test
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
    }

    @Test
    public void testInstantiate() {
        assertNotNull(scene.getEnvironment());
        assertNotNull(scene.getCamera());
        assertNotNull(scene.getModels());
    }
}
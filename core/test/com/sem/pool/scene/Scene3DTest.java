package com.sem.pool.scene;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
        scene.instantiate();
        assertNotNull(scene.getEnvironment());
        assertNotNull(scene.getCamera());
        assertNotNull(scene.getModels());
    }
}
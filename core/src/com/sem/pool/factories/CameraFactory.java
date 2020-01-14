package com.sem.pool.factories;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Factory class which allows the instantiation
 * of camera objects.
 */
public class CameraFactory {

    // Direction the camera is pointed at
    private static final Vector3 LOOK_AT = new Vector3(0, 0, 0);

    private static final float ZOOM = 120f;

    private transient float viewportWidth;
    private transient float viewportHeight;
    private final transient Vector3 position;

    // Wrapper to create LibGDX camera; Used to increase
    // code testability {@see CameraCreator}
    private transient CameraCreator cameraCreator;

    /**
     * Creates a new Camera Factory instance.
     *
     * @param viewportWidth width of the viewport to load right camera width
     * @param viewportHeight height of the viewport to load right camera height
     * @param position position of the camera
     */
    public CameraFactory(float viewportWidth, float viewportHeight, Vector3 position) {
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        this.position = position;
    }

    /**
     * Sets the internal Camera Creator used to create Cameras.
     * @param creator  Camera Creator object.
     */
    public void setCameraCreator(CameraCreator creator) {
        this.cameraCreator = creator;
    }

    /**
     * Creates a Camera object.
     * @return New Camera object
     */
    public Camera createCamera() {
        Vector2 viewport = new Vector2(viewportWidth, viewportHeight);
        Camera camera = cameraCreator.createCamera(viewport, ZOOM);

        if (camera.position != null) {
            camera.position.set(position);
        }

        camera.lookAt(LOOK_AT);
        return camera;
    }
}

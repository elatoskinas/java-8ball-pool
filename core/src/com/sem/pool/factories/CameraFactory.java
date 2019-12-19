package com.sem.pool.factories;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
     * Creates a Camera object.
     * @return New Camera object
     */
    public Camera createCamera() {
        Camera camera = new OrthographicCamera(viewportWidth / ZOOM, viewportHeight / ZOOM);
        camera.position.set(position);
        camera.lookAt(LOOK_AT);
        return camera;
    }
}

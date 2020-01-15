package com.sem.pool.factories;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

/**
 * Wrapper class to abstract away Camera class creation
 * in a method. Used to increase testability due to the
 * LibGDX camera creation resulting in errors.
 */
public class CameraCreator {
    /**
     * Creates a new instance of the Camera Creator.
     */
    public CameraCreator() {

    }

    /**
     * Creates a LibGDX ortographic camera with the specified
     * viewport and zoom parameters.
     * @param viewport  Viewport coordinates of the camera (2D vector of width and height)
     * @param zoom      Zoom level of Camera
     * @return          New ortographic camera object instance
     */
    public Camera createCamera(Vector2 viewport, float zoom) {
        return new OrthographicCamera(viewport.x / zoom, viewport.y / zoom);
    }
}

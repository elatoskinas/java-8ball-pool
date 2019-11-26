package com.sem.pool.scene;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;


public class CameraFactory {

    private static final Vector3 LOOK_AT = new Vector3(0, 0,0);
    private static final float NEAR = 1f;
    private static final float FAR = 300f;

    private transient float fieldOfViewY;
    private transient float viewportWidth;
    private transient float viewportHeight;
    private transient Vector3 position;


    public CameraFactory(float fieldOfViewY, float viewportWidth, float viewportHeight, Vector3 position) {
        this.fieldOfViewY = fieldOfViewY;
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        this.position = position;
    }

    public Camera createCamera() {
        Camera camera = new PerspectiveCamera(fieldOfViewY, viewportWidth, viewportHeight);
        camera.position.set(position);
        camera.lookAt(LOOK_AT);
        camera.near = NEAR;
        camera.far = FAR;
        return camera;
    }
}

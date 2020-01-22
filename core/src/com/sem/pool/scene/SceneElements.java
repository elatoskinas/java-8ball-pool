package com.sem.pool.scene;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;

/**
 * Collection of Scene elements that handle functionalities
 * and the environment of the Scene, such as the Camera or
 * lighting.
 * This class groups such objects together in one cohesive unit.
 */
public final class SceneElements {
    private final Environment environment;
    private final Camera camera;
    private final SoundPlayer soundPlayer;

    /**
     * Creates a new group of Scene elements.
     * @param environment    Environment of the Scene
     * @param camera         Camera of the Scene
     * @param soundPlayer    Sound Player to use for the Scene
     */
    public SceneElements(Environment environment, Camera camera, SoundPlayer soundPlayer) {
        this.environment = environment;
        this.camera = camera;
        this.soundPlayer = soundPlayer;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public Camera getCamera() {
        return camera;
    }

    public SoundPlayer getSoundPlayer() {
        return soundPlayer;
    }
}

package com.sem.pool.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Base screen for the UI.
 * All screen should extend this class, and reimplement methods if needed.
 */
public abstract class UiScreen implements Screen {
    protected transient MainGame game;
    protected transient Stage stage;
    protected transient Skin skin;
    protected transient TextureAtlas atlas;

    /**
     * Create the new screen.
     * @param game Game state to pull from.
     */
    public UiScreen(MainGame game) {
        this.game = game;
    }

    /**
     * Show the screen.
     * This actually renders the screen.
     */
    @Override
    public void show() {

    }

    /**
     * Render the screen.
     * @param delta Delta in seconds since last render call.
     */
    @Override
    public void render(float delta) {

    }

    /**
     * Resize event of the screen.
     *
     * @param w The new width
     * @param h The new height
     */
    @Override
    public void resize(int w, int h) {
        this.stage.getViewport().update(w, h);
    }

    /**
     * Pausing of the screen.
     * This is currently not needed, so it is empty.
     */
    @Override
    public void pause() {

    }

    /**
     * Resuming of the paused screen.
     * It's currently not possible to pause, so you can't resume either.
     */
    @Override
    public void resume() {

    }

    /**
     * Hide the screen.
     * This is currently just killing the screen.
     */
    @Override
    public void hide() {
        this.dispose();
    }

    /**
     * Dispose of the screen.
     * Cleaning up any old objects.
     */
    @Override
    public void dispose() {
        this.stage.dispose();
        this.skin.dispose();
        this.atlas.dispose();
    }
}

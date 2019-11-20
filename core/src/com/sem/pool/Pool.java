package com.sem.pool;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.sem.pool.scene.AssetLoader;
import com.sem.pool.scene.Scene3D;

public class Pool extends ApplicationAdapter {
    //SpriteBatch batch;
    //Texture img;
    Scene3D scene;

    @Override
    public void create() {
        //batch = new SpriteBatch();
        //img = new Texture("badlogic.jpg");
        AssetManager manager = new AssetManager();
        AssetLoader loader = new AssetLoader(manager);
        loader.initializeModels();
        ModelBatch modelBatch = new ModelBatch();

        scene = new Scene3D(loader, modelBatch);
    }

    @Override
    public void render() {
        if (scene.getAssetLoader().getAssetManager().update()) {
            scene.instantiate();
            scene.getCamera().position.set(0f, 5f, 0f);
            scene.getCamera().lookAt(0,0,0);
            scene.getCamera().near = 1f;
            scene.getCamera().far = 300f;
            scene.getCamera().update();
        }

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        //batch.begin();
        //batch.draw(img, 0, 0);
        //batch.end();
        if (scene.getModels() != null) scene.render();
    }

    @Override
    public void dispose() {
        //batch.dispose();
        //img.dispose();
    }
}

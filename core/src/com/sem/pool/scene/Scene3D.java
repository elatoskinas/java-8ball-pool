package com.sem.pool.scene;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * 3D Scene representation of a single Pool game.
 */
public class Scene3D {
    // ModelBatch LibGDX dependency that allows rendering
    private transient ModelBatch modelBatch;

    // Scene elements
    private transient Environment environment;
    private transient Camera camera;
    private transient List<ModelInstance> models;

    // Game elements
    private transient List<Ball3D> poolBalls;
    private transient Table3D table;

    /**
     * Creates an instance of a 3D Pool Game scene from the specified
     * parameters of the scene.
     *
     * @param environment Environment settings of the scene (e.g. light)
     * @param camera      Camera used in the scene
     * @param poolBalls   List of pool balls part of the scene
     * @param table       The table to use for the scene
     * @param batch       Model Batch to use for rendering
     */
    public Scene3D(Environment environment, Camera camera, List<Ball3D> poolBalls,
                   Table3D table, ModelBatch batch) {
        this.environment = environment;
        this.camera = camera;
        this.poolBalls = poolBalls;
        this.table = table;
        this.modelBatch = batch;

        // For all the pool balls and the table, add the models
        // of the entities to a single List for rendering.
        this.models = new ArrayList<>();
        models.add(table.getModel());
        // Add
        ModelBuilder modelBuilder = new ModelBuilder();
        ModelInstance rectangleE = new ModelInstance(modelBuilder.createBox(10f, 0.2f, 5f,
                new Material(ColorAttribute.createDiffuse(Color.WHITE)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal));
        models.add(rectangleE);
        for (Ball3D ball : poolBalls) {
            models.add(ball.getModel());
        }
    }

    public Environment getEnvironment() {
        return environment;
    }

    public Camera getCamera() {
        return camera;
    }

    public List<ModelInstance> getModels() {
        return models;
    }

    public List<Ball3D> getPoolBalls() {
        return poolBalls;
    }

    public Table3D getTable() {
        return table;
    }

    /**
     * Renders the scene with the scene's models, environment
     * and camera. Should be called on every game loop iteration.
     */
    public void render() {
        modelBatch.begin(camera);
        modelBatch.render(models, environment);
        modelBatch.end();
    }

    /**
     * Disposes & completely cleans up the scene of models.
     * To be used when the lifecycle of the game making use
     * of the 3D scene ends.
     */
    public void dispose() {
        modelBatch.dispose();
        models.clear();
    }
}

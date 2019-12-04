package com.sem.pool.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.CollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionAlgorithm;
import com.badlogic.gdx.physics.bullet.collision.btCollisionAlgorithmConstructionInfo;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcherInfo;
import com.badlogic.gdx.physics.bullet.collision.btManifoldResult;
import com.badlogic.gdx.physics.bullet.collision.btSphereBoxCollisionAlgorithm;

import java.util.ArrayList;

/**
 * Class representing a 3D Board of a single game.
 */
public class Table3D {
    private transient ModelInstance model;

    private transient ArrayList<HitBox> hitBoxes;
    private transient ArrayList<ModelInstance> modelInstances;
    private btDefaultCollisionConfiguration collisionConfig;
    private btCollisionDispatcher dispatcher;

    /**
     * Constructs a new 3D Board instance with the specified model.
     * @param model  Model object of the Board
     */
    public Table3D(ModelInstance model) {
        this.model = model;
        this.hitBoxes = new ArrayList<>();
        this.modelInstances = new ArrayList<>();
    }

    /**
     * Sets up the bounding borders for the table.
     */
    public void setUpBoundingBorders() {
        // set up collision dispatcher
        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        // set up bounding borders
        ModelBuilder mb = new ModelBuilder();

        ModelInstance southInstance = new ModelInstance(mb.createBox(10f, 10f, 0.5f,
                new Material(ColorAttribute.createDiffuse(Color.WHITE)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal));
        modelInstances.add(southInstance);
        btCollisionObject southObject = new btCollisionObject();
        btCollisionShape southCollisionShape = new btBoxShape(new Vector3(10f, 10f, 0.1f));
        southObject.setCollisionShape(southCollisionShape);
        southInstance.transform.translate(new Vector3(0, 0, 1.45f));
        southObject.setWorldTransform(southInstance.transform);
        HitBox southHitBox = new HitBox(southCollisionShape, southObject);
        hitBoxes.add(southHitBox);

        ModelInstance northInstance = new ModelInstance(mb.createBox(10f, 10f, 0.5f,
                new Material(ColorAttribute.createDiffuse(Color.WHITE)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal));
        modelInstances.add(northInstance);
        btCollisionObject northObject = new btCollisionObject();
        btCollisionShape northCollisionShape = new btBoxShape(new Vector3(10f, 10f, 0.1f));
        northObject.setCollisionShape(northCollisionShape);
        northInstance.transform.translate(new Vector3(0, 0, -1.45f));
        northObject.setWorldTransform(northInstance.transform);
        HitBox northHitBox = new HitBox(northCollisionShape, northObject);
        hitBoxes.add(northHitBox);

        ModelInstance westInstance = new ModelInstance(mb.createBox(10f, 10f, 0.5f,
                new Material(ColorAttribute.createDiffuse(Color.WHITE)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal));
        modelInstances.add(westInstance);
        btCollisionObject westObject = new btCollisionObject();
        btCollisionShape westCollisionShape = new btBoxShape(new Vector3(0.1f, 10f, 10f));
        westObject.setCollisionShape(westCollisionShape);
        westInstance.transform.translate(new Vector3(-3.05f, 0, 0));
        westObject.setWorldTransform(westInstance.transform);
        HitBox westHitBox = new HitBox(westCollisionShape, westObject);
        hitBoxes.add(westHitBox);

        ModelInstance eastInstance = new ModelInstance(mb.createBox(10f, 10f, 0.5f,
                new Material(ColorAttribute.createDiffuse(Color.WHITE)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal));
        modelInstances.add(eastInstance);
        btCollisionObject eastObject = new btCollisionObject();
        btCollisionShape eastCollisionShape = new btBoxShape(new Vector3(0.1f, 10f, 10f));
        eastObject.setCollisionShape(eastCollisionShape);
        eastInstance.transform.translate(new Vector3(3.05f, 0, 0));
        eastObject.setWorldTransform(eastInstance.transform);
        HitBox eastHitBox = new HitBox(eastCollisionShape, eastObject);
        hitBoxes.add(eastHitBox);
    }

    public ModelInstance getModel() {
        return model;
    }

    /**
     * Checks whether a ball has collided with any of the hit boxes of the table.
     * @param ball Ball that we check collisions with.
     * @return whether the ball collided with the table.
     */
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis") // Might be unsuppressed later.
    public boolean checkCollision(Ball3D ball) {
        for (HitBox hitBox: hitBoxes) {
            if (checkHitBoxCollision(hitBox, ball)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks for a single hit box whether it collided with the ball.
     * @param hitBox HitBox of the table.
     * @param ball Ball object.
     * @return whether the ball collided with the table.
     */
    public boolean checkHitBoxCollision(HitBox hitBox, Ball3D ball) {
        // Copy pasted right now, should work
        btCollisionObject hitBoxObject = hitBox.getObject();
        btCollisionObject ballObject = ball.getHitBox().getObject();
        CollisionObjectWrapper co0 = new CollisionObjectWrapper(ballObject);
        CollisionObjectWrapper co1 = new CollisionObjectWrapper(hitBoxObject);

        btCollisionAlgorithmConstructionInfo ci = new btCollisionAlgorithmConstructionInfo();
        ci.setDispatcher1(dispatcher);
        btCollisionAlgorithm algorithm = new btSphereBoxCollisionAlgorithm(null, ci,
                co0.wrapper, co1.wrapper, false);

        btDispatcherInfo info = new btDispatcherInfo();
        btManifoldResult result = new btManifoldResult(co0.wrapper, co1.wrapper);

        algorithm.processCollision(co0.wrapper, co1.wrapper, info, result);

        final boolean r = result.getPersistentManifold().getNumContacts() > 0;
        result.dispose();
        info.dispose();
        algorithm.dispose();
        ci.dispose();
        co1.dispose();
        co0.dispose();
        return r;
    }
}

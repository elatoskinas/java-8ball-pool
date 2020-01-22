package com.sem.pool.scene;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.CollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btCollisionAlgorithm;
import com.badlogic.gdx.physics.bullet.collision.btCollisionAlgorithmConstructionInfo;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcherInfo;
import com.badlogic.gdx.physics.bullet.collision.btManifoldResult;
import com.badlogic.gdx.physics.bullet.collision.btSphereBoxCollisionAlgorithm;

/**
 * Class that handles the collisions which can be passed to a class
 * such as Table3D to detect collisions on which the class can decide which action to take.
 */
public class CollisionHandler {

    private transient btDefaultCollisionConfiguration collisionConfig;
    private transient btCollisionDispatcher dispatcher;
    private transient btCollisionAlgorithmConstructionInfo constructionInfo;
    private transient btDispatcherInfo dispatcherInfo;

    /**
     * Constructor for a collision handler.
     * @param configuration collision configuration.
     * @param dispatcher dispatcher for collisions.
     * @param constructionInfo algorithm construction info for collision algorithm.
     * @param dispatcherInfo dispatcher information.
     */
    public CollisionHandler(btDefaultCollisionConfiguration configuration,
                            btCollisionDispatcher dispatcher,
                            btCollisionAlgorithmConstructionInfo constructionInfo,
                            btDispatcherInfo dispatcherInfo) {
        this.collisionConfig = configuration;
        this.dispatcher = dispatcher;
        this.constructionInfo = constructionInfo;
        this.constructionInfo.setDispatcher1(dispatcher);
        this.dispatcherInfo = dispatcherInfo;

    }

    public btDefaultCollisionConfiguration getCollisionConfig() {
        return collisionConfig;
    }

    public btCollisionDispatcher getDispatcher() {
        return dispatcher;
    }

    public btCollisionAlgorithmConstructionInfo getConstructionInfo() {
        return constructionInfo;
    }

    public btDispatcherInfo getDispatcherInfo() {
        return dispatcherInfo;
    }

    /**
     * Checks for a single hit box whether it collided with the ball.
     * @param obj1 first object.
     * @param obj2 second object.
     * @return whether the ball collided with the table.
     */
    public boolean checkHitBoxCollision(HitBox obj1, HitBox obj2) {
        btCollisionObject collisionObject = obj1.getObject();
        btCollisionObject collisionObject1 = obj2.getObject();
        CollisionObjectWrapper co0 = new CollisionObjectWrapper(collisionObject);
        CollisionObjectWrapper co1 = new CollisionObjectWrapper(collisionObject1);

        // construct algorithm
        btSphereBoxCollisionAlgorithm algorithm =
                new btSphereBoxCollisionAlgorithm(null, this.constructionInfo,
                co0.wrapper, co1.wrapper, false);

        // create result using object wrappers
        btManifoldResult result = new btManifoldResult(co0.wrapper, co1.wrapper);

        return checkCollisionAlgorithm(algorithm, co0, co1, result);
    }

    /**
     * Returns whether two objects passed as CollisionObjectWrappers
     * collide.
     * @param algorithm collision algorithm.
     * @param co0 first object.
     * @param co1 second object.
     * @param result bt manifest result, used to determine collision.
     * @return whether there was a collision between the two objects according to the algorithm.
     */
    public boolean checkCollisionAlgorithm(btCollisionAlgorithm algorithm,
                                           CollisionObjectWrapper co0,
                                           CollisionObjectWrapper co1,
                                           btManifoldResult result) {
        algorithm.processCollision(co0.wrapper, co1.wrapper, this.dispatcherInfo, result);

        final boolean r = result.getPersistentManifold().getNumContacts() > 0;
        result.dispose();
        algorithm.dispose();
        co1.dispose();
        co0.dispose();
        return r;
    }
}

package com.sem.pool.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
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
    /**
     * Constructs a new 3D Board instance with the specified model.
     * @param model  Model object of the Board
     */
    public Table3D(ModelInstance model) {
        this.model = model;
        this.hitBoxes = new ArrayList<>();
        this.modelInstances = new ArrayList<>();
        // for now just add the "floor"
        ModelBuilder mb = new ModelBuilder();
        ModelInstance floorInstance = new ModelInstance(mb.createBox(2.5f, 2.5f, 2.5f,
                new Material(ColorAttribute.createDiffuse(Color.WHITE)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal));

        modelInstances.add(floorInstance);
        btCollisionObject floorObject = new btCollisionObject();
        floorObject.setCollisionShape(new btBoxShape(new Vector3(2.5f, 0.5f, 2.5f)));
        floorObject.setWorldTransform(floorInstance.transform);
        HitBox floorHitBox = new HitBox(new btBoxShape(new Vector3(2.5f, 0.5f, 2.5f)), floorObject);
        hitBoxes.add(floorHitBox);
        // testing with wall D
        ModelInstance eInstance = new ModelInstance(mb.createBox(10f, 10f, 0.5f,
                new Material(ColorAttribute.createDiffuse(Color.WHITE)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal));
        modelInstances.add(eInstance);
        btCollisionObject eObject = new btCollisionObject();
        eObject.setCollisionShape(new btBoxShape(new Vector3(10f, 10f, 0.5f)));
        eInstance.transform.translate(new Vector3(0, 0, 1.45f));
        eObject.setWorldTransform(eInstance.transform.translate(new Vector3(0,0, 1.45f)));
        HitBox eHitBox = new HitBox(new btBoxShape(new Vector3(10f, 100f, 0.5f)), eObject);
        hitBoxes.add(eHitBox);
    }

    public ModelInstance getModel() {
        return model;
    }


    /**
     * Checks whether a ball has collided with any of the hit boxes of the table.
     * @param ball
     * @return whether the ball collided with the table.
     */
    public boolean checkCollision(Ball3D ball){
        for (HitBox hitBox: hitBoxes){
            if (checkHitBoxCollision(hitBox, ball)){
                //System.out.println(hitBox.getObject().getWorldTransform());
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
    public boolean checkHitBoxCollision(HitBox hitBox, Ball3D ball){
        btDefaultCollisionConfiguration collisionConfig = new btDefaultCollisionConfiguration();
        btCollisionDispatcher dispatcher = new btCollisionDispatcher(collisionConfig);

        // Copy pasted right now, should work
        btCollisionObject hitBoxObject = hitBox.getObject();
        btCollisionObject ballObject = ball.getHitBox().getObject();
        CollisionObjectWrapper co0 = new CollisionObjectWrapper(ballObject);
        CollisionObjectWrapper co1 = new CollisionObjectWrapper(hitBoxObject);

        btCollisionAlgorithmConstructionInfo ci = new btCollisionAlgorithmConstructionInfo();
        ci.setDispatcher1(dispatcher);
        btCollisionAlgorithm algorithm = new btSphereBoxCollisionAlgorithm(null, ci, co0.wrapper, co1.wrapper, false);

        btDispatcherInfo info = new btDispatcherInfo();
        btManifoldResult result = new btManifoldResult(co0.wrapper, co1.wrapper);

        algorithm.processCollision(co0.wrapper, co1.wrapper, info, result);

        boolean r = result.getPersistentManifold().getNumContacts() > 0;

        result.dispose();
        info.dispose();
        algorithm.dispose();
        ci.dispose();
        co1.dispose();
        co0.dispose();

        return r;
    }
}

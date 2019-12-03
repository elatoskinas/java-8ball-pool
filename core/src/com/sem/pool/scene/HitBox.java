package com.sem.pool.scene;

import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;

/**
 * Class used to manage the hitbox of an object.
 * This simplifies the usage of the Bullet wrapper a lot.
 */
public class HitBox {
    
    private transient btCollisionShape shape;
    private transient btCollisionObject object;

    /**
     * Constructs a new HitBox object with the specified Shape and Collision Object.
     * @param shape Collision Shape of the HitBox.
     * @param object Collision Object of the HitBox.
     */
    public HitBox(btCollisionShape shape, btCollisionObject object) {
        this.shape = shape;
        this.object = object;
    }

    public btCollisionShape getShape() {
        return shape;
    }

    public btCollisionObject getObject() {
        return object;
    }
    
}

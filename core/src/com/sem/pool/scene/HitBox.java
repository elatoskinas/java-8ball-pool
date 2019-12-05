package com.sem.pool.scene;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;

/**
 * Class used to manage the hitbox of an object.
 * This simplifies the usage of the Bullet wrapper a lot.
 */
public class HitBox {
    
    private transient btCollisionShape shape;
    private transient btCollisionObject object;
    private transient Vector3 normal;

    public Vector3 getNormal() {
        return normal;
    }

    public void setNormal(Vector3 normal) {
        this.normal = normal;
    }

    /**
     * Constructs a new HitBox object with the specified Shape and Collision Object.
     * @param shape Collision Shape of the HitBox.
     * @param object Collision Object of the HitBox.
     */
    public HitBox(btCollisionShape shape, btCollisionObject object) {
        this.shape = shape;
        this.object = object;
    }

    /**
     * Returns whether another Object is equal to this HitBox.
     * @param obj other object.
     * @return whether they are equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof HitBox) {
            HitBox hb = (HitBox) obj;
            return  hb.getObject().equals(this.getObject())
                    && hb.getShape().equals(this.getShape());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public btCollisionShape getShape() {
        return shape;
    }

    public btCollisionObject getObject() {
        return object;
    }
}

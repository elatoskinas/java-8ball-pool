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
    
}

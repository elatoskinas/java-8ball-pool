package com.sem.pool.scene;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;

public class HitBox {
    btCollisionShape collisionShape;
    btCollisionObject collisionObject;
    Vector3 center;
    Vector3 normal;

    public HitBox(Vector3 center, Vector3 normal) {
        this.center = center;
        this.normal = normal;
    }

    public btCollisionShape getCollisionShape() {
        return collisionShape;
    }

    public void setCollisionShape(btCollisionShape collisionShape) {
        this.collisionShape = collisionShape;
    }

    public btCollisionObject getCollisionObject() {
        return collisionObject;
    }

    public void setCollisionObject(btCollisionObject collisionObject) {
        this.collisionObject = collisionObject;
    }
}

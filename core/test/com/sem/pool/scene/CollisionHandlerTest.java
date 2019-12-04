package com.sem.pool.scene;

import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.CollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btCollisionAlgorithm;
import com.badlogic.gdx.physics.bullet.collision.btCollisionAlgorithmConstructionInfo;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcherInfo;
import com.badlogic.gdx.physics.bullet.collision.btManifoldResult;
import com.badlogic.gdx.physics.bullet.collision.btPersistentManifold;
import com.badlogic.gdx.physics.bullet.collision.btSphereBoxCollisionAlgorithm;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CollisionHandlerTest {

    @Test
    public void testConstructor() {
        Bullet.init();
        btDefaultCollisionConfiguration configuration = new btDefaultCollisionConfiguration();
        btCollisionDispatcher dispatcher = new btCollisionDispatcher(configuration);
        btCollisionAlgorithmConstructionInfo constructionInfo = new btCollisionAlgorithmConstructionInfo();
        btDispatcherInfo dispatcherInfo = new btDispatcherInfo();
        CollisionHandler collisionHandler = new CollisionHandler(configuration, dispatcher, constructionInfo, dispatcherInfo);
        assertEquals(collisionHandler.getCollisionConfig(), configuration);
        assertEquals(collisionHandler.getDispatcher(), dispatcher);
        assertEquals(collisionHandler.getConstructionInfo(), constructionInfo);
        assertEquals(collisionHandler.getDispatcherInfo(), dispatcherInfo);
    }

    @Test
    public void testCollisionAlgorithm() {
        Bullet.init();
        btDefaultCollisionConfiguration configuration = new btDefaultCollisionConfiguration();
        btCollisionDispatcher dispatcher = new btCollisionDispatcher(configuration);
        btCollisionAlgorithmConstructionInfo constructionInfo = new btCollisionAlgorithmConstructionInfo();
        btDispatcherInfo dispatcherInfo = new btDispatcherInfo();
        CollisionHandler collisionHandler = new CollisionHandler(configuration, dispatcher, constructionInfo, dispatcherInfo);

        CollisionObjectWrapper mockedCO0 = Mockito.mock(CollisionObjectWrapper.class);
        CollisionObjectWrapper mockedCO1 = Mockito.mock(CollisionObjectWrapper.class);
        btCollisionAlgorithm mockedAlgorithm = Mockito.mock(btCollisionAlgorithm.class);
        btManifoldResult mockedResult = Mockito.mock(btManifoldResult.class);
        btPersistentManifold mockedPersistentManifold = Mockito.mock(btPersistentManifold.class);
        mockedResult.setPersistentManifold(mockedPersistentManifold);

        Mockito.doNothing().when(mockedAlgorithm).processCollision(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.when(mockedResult.getPersistentManifold()).thenReturn(mockedPersistentManifold);
        Mockito.when(mockedPersistentManifold.getNumContacts()).thenReturn(1);
        assertTrue(collisionHandler.checkCollisionAlgorithm(mockedAlgorithm, mockedCO0, mockedCO1, mockedResult));


    }
}

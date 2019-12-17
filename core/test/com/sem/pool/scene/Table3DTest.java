package com.sem.pool.scene;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;



/**
 * Test class containing unit tests for the Table3D class.
 */
class Table3DTest {

    private transient Table3D board;

    /**
     * For every test, set up a fresh Table3D and potHitBoxes list.
     */
    @BeforeEach
    public void setUp() {
        ModelInstance model = Mockito.mock(ModelInstance.class);
        this.board = new Table3D(model);
        Table3D.potHitBoxes = new ArrayList<>();
    }

    /**
     * Test case to verify that the constructor of Table3D
     * properly sets the model of the Table.
     */
    @Test
    public void testConstructor() {
        ModelInstance model = Mockito.mock(ModelInstance.class);
        Table3D board = new Table3D(model);

        assertEquals(model, board.getModel());
    }

    /**
     * Tests the getters and setters of the hitBoxes field.
     */
    @Test
    public void testGetHitBoxes() {
        ArrayList<HitBox> hitBoxes = new ArrayList<>();
        assertEquals(board.getHitBoxes(), hitBoxes);

        HitBox mockHitBox = Mockito.mock(HitBox.class);
        hitBoxes.add(mockHitBox);
        board.addHitBox(mockHitBox);
        assertEquals(board.getHitBoxes(), hitBoxes);

        ArrayList<HitBox> potHitBoxes = new ArrayList<>();
        assertEquals(board.getPotHitBoxes(), potHitBoxes);
        HitBox mockedPot = Mockito.mock(HitBox.class);
        potHitBoxes.add(mockedPot);
        board.addPotHitBox(mockedPot);
        assertEquals(board.getPotHitBoxes(), potHitBoxes);

        CollisionHandler mockedHandler = Mockito.mock(CollisionHandler.class);
        board.setCollisionHandler(mockedHandler);
        assertEquals(mockedHandler, board.getCollisionHandler());
    }

    /**
     * Tests the getters and setters of the potHitBoxes field.
     */
    @Test
    public void testGetPotHitBoxes() {
        ArrayList<HitBox> potHitBoxes = new ArrayList<>();
        assertEquals(board.getPotHitBoxes(), potHitBoxes);
        HitBox mockedPot = Mockito.mock(HitBox.class);
        potHitBoxes.add(mockedPot);
        board.addPotHitBox(mockedPot);
        assertEquals(board.getPotHitBoxes(), potHitBoxes);
    }

    /**
     * Tests the getters and setters of the collisionHandler field.
     */
    @Test
    public void testGetHandler() {
        CollisionHandler mockedHandler = Mockito.mock(CollisionHandler.class);
        board.setCollisionHandler(mockedHandler);
        assertEquals(mockedHandler, board.getCollisionHandler());
    }

    /**
     * Test if the handler is called by the table on collision check.
     */
    @Test
    public void testCollisions() {
        //set up ball that collides.
        Ball3D mockedBall = Mockito.mock(Ball3D.class);
        Mockito.when(mockedBall.getHitBox()).thenReturn(Mockito.mock(HitBox.class));
        Vector3 direction = new Vector3(1,0,0);
        Mockito.when(mockedBall.getDirection()).thenReturn(direction);

        // set up hitbox for the table.
        HitBox mockedHitBox = Mockito.mock(HitBox.class);
        Vector3 normal = new Vector3(0,1,0);
        Mockito.when(mockedHitBox.getNormal()).thenReturn(normal);

        board.addHitBox(mockedHitBox);

        // set up handler.
        CollisionHandler mockedHandler = Mockito.mock(CollisionHandler.class);
        Mockito.when(mockedHandler.checkHitBoxCollision(Mockito.any(),
                Mockito.any())).thenReturn(true);
        // set handler.
        board.setCollisionHandler(mockedHandler);
        // assert that the method returns true if the handler returns true.
        assertTrue(board.checkCollision(mockedBall));

        // verify that the handler is called once.
        Mockito.verify(mockedHandler, Mockito.times(1))
                .checkHitBoxCollision(Mockito.any(HitBox.class), Mockito.any(HitBox.class));

        // verify that the ball gets a new direction which is equal to reflectedVector
        Vector3 reflectedVector = direction.add(normal.scl(-2 * direction.dot(normal)));
        Mockito.verify(mockedBall, Mockito.times(1)).setDirection(reflectedVector);
    }

    /**
     * Test if the handler is called by the table on collision check.
     * If the handler returns true the ball is potted.
     */
    @Test
    public void testPotCollisions() {
        Ball3D mockedBall = Mockito.mock(Ball3D.class);

        HitBox mockedPot = Mockito.mock(HitBox.class);
        board.addPotHitBox(mockedPot);
        CollisionHandler mockedHandler = Mockito.mock(CollisionHandler.class);
        Mockito.when(mockedBall.getHitBox()).thenReturn(Mockito.mock(HitBox.class));
        Mockito.when(mockedHandler.checkHitBoxCollision(Mockito.any(),
                Mockito.any())).thenReturn(true);
        board.setCollisionHandler(mockedHandler);
        board.checkIfPot(mockedBall);
        Mockito.verify(mockedHandler, Mockito.times(1))
                .checkHitBoxCollision(Mockito.any(HitBox.class), Mockito.any(HitBox.class));
        //Mockito.verify(mockedBall, Mockito.times(1)).pot();
    }

    /**
     * Test if the handler is called by the table on collision check.
     * If the handler returns false no ball is potted.
     */
    @Test
    public void testNoPotCollisions() {
        Ball3D mockedBall = Mockito.mock(Ball3D.class);
        HitBox mockedPot = Mockito.mock(HitBox.class);
        board.addPotHitBox(mockedPot);
        CollisionHandler mockedHandler = Mockito.mock(CollisionHandler.class);
        Mockito.when(mockedBall.getHitBox()).thenReturn(Mockito.mock(HitBox.class));
        Mockito.when(mockedHandler.checkHitBoxCollision(Mockito.any(),
                Mockito.any())).thenReturn(false);
        board.setCollisionHandler(mockedHandler);
        board.checkIfPot(mockedBall);
        Mockito.verify(mockedHandler, Mockito.times(1))
                .checkHitBoxCollision(Mockito.any(HitBox.class), Mockito.any(HitBox.class));
        Mockito.verify(mockedBall, Mockito.times(0)).pot();
    }

}
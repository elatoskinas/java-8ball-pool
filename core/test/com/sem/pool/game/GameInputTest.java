package com.sem.pool.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.sem.pool.scene.Ball3D;
import com.sem.pool.scene.Cue3D;
import com.sem.pool.scene.CueBall3D;

import com.sem.pool.scene.SoundPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Base class for extracting common functionality for testing
 * the Game class.
 */
class GameInputTest extends GameBaseTest {
    protected transient Cue3D cue;

    /**
     * Handles setting up the test fixture by
     * creating mocks of all the required dependencies
     * for creating a Cue object instance.
     */
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        CueBall3D ball = makeCueBall();
        Mockito.when(scene.getCueBall()).thenReturn(ball);

        ModelInstance model = Mockito.mock(ModelInstance.class);
        model.transform = Mockito.mock(Matrix4.class);
        Material mat = new Material();
        Mockito.when(model.getMaterial("CueMaterial")).thenReturn(mat);
        cue = Mockito.spy(new Cue3D(model));

        Mockito.when(scene.getCue()).thenReturn(cue);
        Mockito.when(scene.getUnprojectedMousePosition()).thenReturn(new Vector3(0, 0,0));

        gameState = new GameState(players, poolBalls);
        game = new Game(scene, input, gameState);
    }

    /**
     * Test case to verify that the cue rotates when it is set to a new position.
     */
    @Test
    public void testRotateCueIsCalled() {

        CueBall3D ball = makeCueBall();
        Cue3D cueSpy = Mockito.spy(cue);
        Mockito.doNothing().when(cueSpy).rotateCue(ball);
        cueSpy.toPosition(new Vector3(1, 0, 0), ball);

        Mockito.verify(cueSpy, Mockito.times(1)).rotateCue(ball);

    }

    /**
     * Test case to verify that the cue rotates when it is set to a new position.
     */
    @Test
    public void testRotateCueIsCalledInDrag() {
        Matrix4 mockMatrix = Mockito.mock(Matrix4.class);
        cue.getModel().transform = mockMatrix;
        Cue3D cueSpy = Mockito.spy(cue);
        cue.getModel().transform = mockMatrix;
        Mockito.when(mockMatrix.getTranslation(Mockito.any())).thenReturn(new Vector3());
        cueSpy.getModel().transform = mockMatrix;
        CueBall3D ball = makeCueBall();
        Mockito.doNothing().when(cueSpy).rotateCue(ball);
        Mockito.when(cueSpy.getCoordinates()).thenReturn(new Vector3());
        cueSpy.toDragPosition(new Vector3(1, 0, 0), ball);

        Mockito.verify(cueSpy, Mockito.times(1)).rotateCue(ball);
    }

    /**
     * Test case to verify that the cue processes the input when it goes from Hidden to Rotating.
     */
    @Test
    public void testProcessInputWhenHidden() {

        Mockito.doNothing().when(cue).toPosition(any(Vector3.class), any(CueBall3D.class));

        cue.setState(Cue3D.State.Hidden);
        game.processCueInput();
        assertEquals(Cue3D.State.Rotating, cue.getState());

        Mockito.verify(cue, Mockito.times(1)).toPosition(any(Vector3.class), any(Ball3D.class));
    }

    /**
     * Test case to verify that the cue processes the input when it goes from
     * Dragging to Hidden and that the cue shoots the cueball.
     */
    @Test
    public void testProcessInputWhenDraggingShot() {

        // to avoid nullptr
        SoundPlayer soundPlayer = Mockito.mock(SoundPlayer.class);
        Mockito.doNothing().when(soundPlayer).playCueSound();
        Mockito.when(scene.getSoundPlayer()).thenReturn(soundPlayer);

        Mockito.doNothing().when(cue).toDragPosition(any(Vector3.class), any(CueBall3D.class));

        cue.setState(Cue3D.State.Dragging);
        cue.setCurrentForce(1f);

        game.processCueInput();
        assertEquals(Cue3D.State.Hidden, cue.getState());

        Mockito.verify(cue, Mockito.times(1)).shoot(any(Ball3D.class));
        Mockito.verify(cue, Mockito.never()).toPosition(any(Vector3.class), any(Ball3D.class));
    }

    /**
     * Test case to verify that the cue cancel the shot and goes
     * back to rotating when the force of the shot was 0.
     */
    @Test
    public void testProcessInputWhenDraggingCancelShot() {

        Mockito.doNothing().when(cue).toPosition(any(Vector3.class), any(CueBall3D.class));

        cue.setState(Cue3D.State.Dragging);
        cue.setCurrentForce(0f);
        game.processCueInput();
        assertEquals(Cue3D.State.Rotating, cue.getState());

        Mockito.verify(cue, Mockito.never()).shoot(any(Ball3D.class));
        Mockito.verify(cue, Mockito.times(1)).toPosition(any(Vector3.class), any(Ball3D.class));
    }

    /**
     * Test case to verify that the cue processes the input when it goes from Dragging to Hidden.
     */
    @Test
    public void testProcessInputOnLeftClick() {

        Mockito.when(input.isButtonPressed(Input.Buttons.LEFT)).thenReturn(true);

        Mockito.doNothing().when(cue).toDragPosition(any(Vector3.class), any(CueBall3D.class));

        cue.setState(Cue3D.State.Rotating);
        Matrix4 mockMatrix = Mockito.mock(Matrix4.class);
        Mockito.when(mockMatrix.getTranslation(Mockito.any())).thenReturn(new Vector3());
        cue.getModel().transform = mockMatrix;
        game.processCueInput();
        assertEquals(Cue3D.State.Dragging, cue.getState());

        Mockito.verify(cue, Mockito.times(1))
                .toDragPosition(any(Vector3.class), any(CueBall3D.class));
        Mockito.verify(cue, Mockito.never()).toPosition(any(Vector3.class), any(Ball3D.class));
    }

    /**
     * Test case to verify that the cue rotates when rotateCue is called.
     */
    @Test
    public void testRotateCue() {
        Matrix4 mockMatrix = Mockito.mock(Matrix4.class);
        Mockito.when(mockMatrix.getTranslation(Mockito.any())).thenReturn(new Vector3());
        cue.getModel().transform = mockMatrix;
        CueBall3D cueBall = makeCueBall();
        Mockito.when(cue.getCoordinates()).thenReturn(new Vector3(0, 0,0));
        cue.rotateCue(cueBall);
        Mockito.verify(cue.getModel().transform).rotateRad(any(Vector3.class), any(float.class));
    }


    /**
     * Helper method that makes a cue ball.
     * @return CueBall3D cue ball.
     */
    public CueBall3D makeCueBall() {
        ModelInstance ballModel = Mockito.mock(ModelInstance.class);
        ballModel.transform = new Matrix4();

        // Setup expected bounding box of size 4 in each axis
        BoundingBox box = new BoundingBox();
        box.ext(4, 4, 4);

        // Make the mock model's calculate bounding box method return
        // the constructed box
        Mockito.when(ballModel.calculateBoundingBox(any(BoundingBox.class)))
                .thenReturn(box);

        return new CueBall3D(GameConstants.CUEBALL_ID, ballModel);
    }

}
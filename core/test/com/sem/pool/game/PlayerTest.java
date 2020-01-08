package com.sem.pool.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sem.pool.scene.Ball3D;
import com.sem.pool.scene.RegularBall3D;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

class PlayerTest {
    /**
     * Test case to verify that constructing a Player with the
     * specified ID sets the ID properly, and instantiates a
     * Player with no potted balls.
     */
    @Test
    public void testConstructor() {
        final int id = 1;
        Player player = new Player(id);

        assertEquals(id, player.getId());
        assertNotNull(player.getPottedBalls());
        assertEquals(0, player.getPottedBalls().size());
        // TODO: Test for unassigned ball type
    }

    /**
     * Test case to verify that making the Player pot a ball
     * of their own type adds the Ball to their potted balls set.
     * For simplicity, both types (for Player and ball) is
     * the unassigned type.
     */
    @Test
    public void testPotBallSameType() {
        final int id = 0;
        final RegularBall3D.Type type = RegularBall3D.Type.STRIPED;

        RegularBall3D ball = Mockito.mock(RegularBall3D.class);
        Mockito.when(ball.getType()).thenReturn(type);

        Player player = new Player(id);
        player.assignBallType(type);
        player.potBall(ball);

        assertTrue(player.getPottedBalls().contains(ball));
    }

    /**
     * Test case to verify assigning of ball types.
     */
    @Test
    public void testAssignBallType() {
        final int id = 0;
        Player player = new Player(id);

        player.assignBallType(RegularBall3D.Type.FULL);
        assertEquals(player.getBallType(), RegularBall3D.Type.FULL);

        player.assignBallType(RegularBall3D.Type.STRIPED);
        assertEquals(player.getBallType(), RegularBall3D.Type.STRIPED);
    }

    /**
     * Test case to verify that when a Player is checked
     * whether all of their balls are potted, and they have
     * a count of 0, but have not a ball type assigned,
     * then the result is false.
     */
    @Test
    public void testCheckAllBallsPottedUnassignedType() {
        final int id = 0;
        final Set<Ball3D> unpotted = new HashSet<>();
        Player player = new Player(id);

        assertEquals(RegularBall3D.Type.UNASSIGNED, player.getBallType());
        assertFalse(player.allBallsPotted(unpotted));
    }

    /**
     * Test case to verify that when a Player has potted all of
     * their balls, and have a ball type assigned, then
     * the result of checking if all balls are potted is true.
     */
    @Test
    public void testCheckAllBallsPottedAssignedType() {
        final int id = 0;
        final Set<Ball3D> unpotted = new HashSet<>();
        Player player = new Player(id);


        player.assignBallType(RegularBall3D.Type.FULL);
        assertTrue(player.allBallsPotted(unpotted));
    }

    /**
     * Test case to verify that when a check is performed
     * on whether the Player has potted all their balls
     * and they still have some balls remaining (while
     * type is assigned), then false is returned.
     */
    @Test
    public void testCheckAllBallsNotPotted() {
        final int id = 0;
        final RegularBall3D.Type type = RegularBall3D.Type.FULL;
        final Set<Ball3D> unpotted = new HashSet<>();
        final RegularBall3D ball = Mockito.mock(RegularBall3D.class);

        Mockito.when(ball.getType()).thenReturn(type);
        unpotted.add(ball);

        Player player = new Player(id);

        player.assignBallType(type);
        assertFalse(player.allBallsPotted(unpotted));
    }

    /**
     * Test case to verify that making the Player pot a ball
     * that is not of their assigned type does not pot the ball.
     */
    @Test
    public void testPotBallDifferentType() {
        final int id = 0;
        Player player = new Player(id);

        RegularBall3D ball = Mockito.mock(RegularBall3D.class);
        Mockito.when(ball.getType()).thenReturn(RegularBall3D.Type.STRIPED);

        player.potBall(ball);

        assertFalse(player.getPottedBalls().contains(ball));
    }
}
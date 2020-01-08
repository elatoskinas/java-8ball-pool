package com.sem.pool.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sem.pool.scene.RegularBall3D;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
        Player player = new Player(id);

        RegularBall3D ball = Mockito.mock(RegularBall3D.class);
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
        Player player = new Player(id);

        player.updateBallsLeft(0);
        assertEquals(RegularBall3D.Type.UNASSIGNED, player.getBallType());
        assertFalse(player.allBallsPotted());
    }

    /**
     * Test case to verify that when a Player has potted all of
     * their balls, and have a ball type assigned, then
     * the result of checking if all balls are potted is true.
     */
    @Test
    public void testCheckAllBallsPottedAssignedType() {
        final int id = 0;
        Player player = new Player(id);

        player.assignBallType(RegularBall3D.Type.FULL);
        player.updateBallsLeft(0);
        assertTrue(player.allBallsPotted());
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
        Player player = new Player(id);

        player.assignBallType(RegularBall3D.Type.FULL);
        player.updateBallsLeft(1);
        assertFalse(player.allBallsPotted());
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

    /**
     * Test case to verify that when the pot method for Player
     * is called with a ball of type not equal to theirs,
     * then the count of potted balls is not decreased.
     * (Here, we rely on checking if all balls are potted with count 1 before)
     */
    @Test
    public void testPotBallDifferentTypeCountSame() {
        final int id = 0;
        final RegularBall3D.Type type1 = RegularBall3D.Type.FULL;
        final RegularBall3D.Type type2 = RegularBall3D.Type.STRIPED;

        Player player = new Player(id);
        player.assignBallType(type1);
        player.updateBallsLeft(1);

        RegularBall3D ball = Mockito.mock(RegularBall3D.class);
        Mockito.when(ball.getType()).thenReturn(type2);

        player.potBall(ball);

        assertFalse(player.allBallsPotted());
    }

    /**
     * Test case to verify that when the pot method for Player
     * is called with a ball of type equals theirs,
     * then the count of potted balls is decreased.
     * (Here, we rely on checking if all balls are potted with count 1 before)
     */
    @Test
    public void testPotBallSameTypeChangeCount() {
        final int id = 0;
        final RegularBall3D.Type type = RegularBall3D.Type.FULL;

        Player player = new Player(id);
        player.assignBallType(type);
        player.updateBallsLeft(1);

        RegularBall3D ball = Mockito.mock(RegularBall3D.class);
        Mockito.when(ball.getType()).thenReturn(type);

        player.potBall(ball);

        assertTrue(player.allBallsPotted());
    }
}
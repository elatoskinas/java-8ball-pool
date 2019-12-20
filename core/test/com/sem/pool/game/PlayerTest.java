package com.sem.pool.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
     * adds the Ball to their potted balls set.
     */
    @Test
    public void testPotBall() {
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
}
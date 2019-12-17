package com.sem.pool.game;

import com.sem.pool.scene.Ball3D;
import com.sem.pool.scene.RegularBall3D;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

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

        assertTrue(player.pottedBalls.contains(ball));
    }
}
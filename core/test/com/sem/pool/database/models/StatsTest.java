package com.sem.pool.database.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

public class StatsTest {
    /**
     * Test the constructor.
     */
    @Test
    void constructor() {
        User user = new User("Hello", "world");
        Stats stats = new Stats(user);

        assertEquals(0, stats.getGameCount());
        assertEquals(user, stats.getUser());
        assertEquals(0, stats.getWinLossRatio());
    }

    /**
     * Test insertion of results.
     */
    @Test
    void insertResult() {
        User user0 = new User("hi", "#$%^^$");
        User user1 = new User("there", ".....");
        Result result0 = new Result(42, user1, user0);
        Stats stats = new Stats(user0);
        stats.addResult(result0);

        assertEquals(user0, stats.getUser());
        assertEquals(1, stats.getGameCount());
        assertEquals(0.f, stats.getWinLossRatio());

        Result result1 = new Result(42, user0, user1);
        stats.addResult(result1);

        assertEquals(2, stats.getGameCount());
        assertEquals(0.5f, stats.getWinLossRatio());
    }

    /**
     * Test the equals method.
     */
    @Test
    void equalsTest() {
        User user = new User("Hello", "world");
        User otherUser = new User("Other", "User!");
        Result result = new Result(42, user, user);
        Stats stats0 = new Stats(user);
        Stats stats1 = new Stats(otherUser);

        assertNotEquals(stats0, "NO");
        stats0.addResult(result);
        assertNotEquals(stats1, stats0);
        stats1.addResult(result);
        assertNotEquals(stats1, stats0);

        Stats stats2 = new Stats(user);
        stats2.addResult(result);
        assertEquals(stats2, stats0);
        assertEquals(stats2.hashCode(), stats0.hashCode());
    }
}

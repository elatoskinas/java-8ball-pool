package com.sem.pool.database.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ResultTest {
    @Test
    void simple() {
        User user = new User("testPassword", "test");

        Result result = new Result(42, user, user);
        assertEquals(42, result.getGameID());
        assertEquals(user, result.getWinner());
        assertEquals(user, result.getLoser());
    }
}

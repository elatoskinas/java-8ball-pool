package com.sem.pool.database.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sem.pool.database.Database;
import com.sem.pool.database.models.Result;
import com.sem.pool.database.models.Stats;
import com.sem.pool.database.models.User;
import com.sem.pool.database.tables.ResultTable;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class StatsControllerTest {
    /**
     * Test if the top statistics calculator returns the correct results.
     * @throws SQLException SQL error, should not occur.
     */
    @Test
    public void getTopTest() throws SQLException {
        ArrayList<Result> results = new ArrayList<>();
        User user0 = new User(42, "Hello", "hashedpassword");
        User user1 = new User(666, "World", "password");
        User user2 = new User(69, "!", "!@#$%^%&$##$%$#");
        User user3 = new User(111, "?", "--..--.--..-..--..-");
        results.add(new Result(42, user0, user1));
        results.add(new Result(42 / 2, user2, user0));
        results.add(new Result(42 * 2, user2, user1));
        results.add(new Result(42 * 2, user2, user3));

        Database db = Mockito.mock(Database.class);
        ResultTable table = Mockito.mock(ResultTable.class);

        Mockito.when(db.table(Mockito.anyString())).thenReturn(table);
        Mockito.when(table.getAll()).thenReturn(results);

        StatsController statsController = new StatsController(db);
        ArrayList<Stats> result = statsController.getTop();

        assertEquals(4, result.size());
        assertEquals(user2, result.get(0).getUser());
        assertEquals(1f, result.get(0).getWinLossRatio());
        assertEquals(3, result.get(0).getGameCount());
        assertEquals(user0, result.get(1).getUser());
        assertEquals(.5f, result.get(1).getWinLossRatio());
        assertEquals(2, result.get(1).getGameCount());
        assertEquals(user1, result.get(2).getUser());
        assertEquals(0f, result.get(2).getWinLossRatio());
        assertEquals(2, result.get(2).getGameCount());
        assertEquals(user3, result.get(3).getUser());
        assertEquals(0f, result.get(3).getWinLossRatio());
        assertEquals(1, result.get(3).getGameCount());
    }
}

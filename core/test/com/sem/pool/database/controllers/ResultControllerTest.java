package com.sem.pool.database.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.sem.pool.database.Database;
import com.sem.pool.database.models.Result;
import com.sem.pool.database.models.User;
import com.sem.pool.database.tables.ResultTable;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ResultControllerTest {
    private transient ResultController resultController;

    @BeforeEach
    public void setUp() {
        Database.setTestMode();
        this.resultController = new ResultController(Database.getInstance());
    }

    /**
     * Test failing to save the result.
     */
    @Test
    public void failedSaving() throws SQLException {
        Database db = Mockito.mock(Database.class);
        ResultTable table = Mockito.mock(ResultTable.class);

        Mockito.when(db.table(Mockito.anyString())).thenReturn(table);
        Mockito.when(table.save(Mockito.any(Result.class))).thenThrow(SQLException.class);

        ResultController result = new ResultController(db);
        User player = Mockito.mock(User.class);

        assertFalse(result.createResult(player, player));
    }

    /**
     * Test failing to get all result.
     */
    @Test
    public void failedGetting() throws SQLException {
        Database db = Mockito.mock(Database.class);
        ResultTable table = Mockito.mock(ResultTable.class);

        Mockito.when(db.table(Mockito.anyString())).thenReturn(table);
        Mockito.when(table.getAll()).thenThrow(SQLException.class);

        ResultController result = new ResultController(db);

        assertEquals(new ArrayList<Result>(), result.getAll());
    }
}

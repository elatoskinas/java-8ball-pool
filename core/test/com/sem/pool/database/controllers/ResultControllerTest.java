package com.sem.pool.database.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sem.pool.database.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ResultControllerTest {
    private transient ResultController resultController;

    @BeforeEach
    public void setUp() {
        Database.setTestMode();
        this.resultController = new ResultController(Database.getInstance());
    }

    @Test
    public void exists() {
        assertEquals(this.resultController, this.resultController);
    }
}

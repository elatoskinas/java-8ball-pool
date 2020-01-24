package com.sem.pool.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sem.pool.database.Database;
import com.sem.pool.scene.Ball3D;
import com.sem.pool.scene.CueBall3D;
import com.sem.pool.scene.EightBall3D;
import com.sem.pool.scene.RegularBall3D;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class GameStateTest {
    transient GameState gameState;
    transient List<Player> players;
    transient List<Ball3D> balls;

    @BeforeEach
    public void setUp() {
        Database.setTestMode();

        Player player1 = new Player(0);
        Player player2 = new Player(1);
        players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        balls = constructBallsList(true, true, 2, 2);

        gameState = new GameState(players, this.balls);
    }

    /**
     * Helper method to construct a List of Ball3D objects from the specified configuraion.
     *
     * @param cueBall   True to add the cue ball to resulting list
     * @param eightBall True to add he eight ball to resulting list
     * @param solid     Number of solid balls to add to list
     * @param striped   Number of striped balls to add to list
     * @return Resulting list of balls
     */
    private List<Ball3D> constructBallsList(boolean cueBall, boolean eightBall,
                                            int solid, int striped) {
        List<Ball3D> result = new ArrayList<>();

        if (cueBall) {
            CueBall3D cueBall3D = Mockito.mock(CueBall3D.class);
            result.add(cueBall3D);
        }

        if (eightBall) {
            EightBall3D eightBall3D = Mockito.mock(EightBall3D.class);
            result.add(eightBall3D);
        }

        for (int i = 0; i < solid; ++i) {
            RegularBall3D ball = Mockito.mock(RegularBall3D.class);
            Mockito.when(ball.getType()).thenReturn(RegularBall3D.Type.FULL);
            result.add(ball);
        }

        for (int i = 0; i < striped; ++i) {
            RegularBall3D ball = Mockito.mock(RegularBall3D.class);
            Mockito.when(ball.getType()).thenReturn(RegularBall3D.Type.STRIPED);
            result.add(ball);
        }

        return result;
    }

    /**
     * Test case to verify that the constructor of the GameState properly instantiates
     * the GameState with the specified Players list, and adds all the balls
     * except the cue ball to the remaining balls list.
     */
    @Test
    void testConstructor() {
        final int solidBalls = 3;
        final int stripedBalls = 2;
        balls = constructBallsList(true, true, solidBalls, stripedBalls);

        // We do not expect the cue ball to be in remaining balls list
        final int expectedBalls = solidBalls + stripedBalls + 1;

        GameState gameState2 = new GameState(players, balls);

        assertEquals(players, gameState2.getTurnHandler().getPlayers());
        assertEquals(expectedBalls, gameState2.getGameBallState().getRemainingBalls().size());
    }


    /**
     * Tests if the number of the random
     * starting player is valid.
     */
    @Test
    void testInitStartingPlayer() {
        for (int i = 0; i < 10; i++) {
            gameState.getTurnHandler().initializeStartingPlayer();
            int playerTurn = gameState.getTurnHandler().getPlayerTurn();
            assertTrue(playerTurn == 1 || playerTurn == 0);
        }
    }

    /**
     * Tests case to verify that the player turn
     * is set to the next player.
     */
    @Test
    void testAdvancePlayerTurn() {
        int playerTurn = gameState.getTurnHandler().getPlayerTurn();
        gameState.advanceTurn();
        int newPlayerTurn = gameState.getTurnHandler().getPlayerTurn();

        assertNotEquals(playerTurn, newPlayerTurn);
        assertEquals(Math.abs(playerTurn - newPlayerTurn), 1);

        gameState.advanceTurn();
        assertEquals(playerTurn, gameState.getTurnHandler().getPlayerTurn());
        assertFalse(gameState.getGameBallState().isCueBallPotted());
    }

    /**
     * Tests the transition of starting the game, which
     * transitions from a newly created (stopped) game state
     * to a started game state.
     */
    @Test
    void testStartGame() {
        assertFalse(gameState.isStarted());

        gameState.onGameStarted();

        assertTrue(gameState.isStarted());
    }

    /**
     * Test case to verify that potting a ball via the
     * onBallPotted call in GameState removes the Ball
     * from the remaining pool balls set.
     */
    @Test
    void testBallPottedRemovedFromRemaining() {
        // Get first ball (constructed in setUp)
        // NOTE: Id 0 won't work since it's a cue ball
        // NOTE: Id 1 won't work since it's a eight ball

        RegularBall3D ball = (RegularBall3D) balls.get(2);

        // Verify that ball is contained in remaining set (handled
        // in constructor)
        assertTrue(gameState.getGameBallState().getRemainingBalls().contains(ball));

        // Pot the ball
        gameState.onBallPotted(ball);
        gameState.advanceTurn();

        // Assert ball is no longer contained in remaining ball set
        assertFalse(gameState.getGameBallState().getRemainingBalls().contains(ball));
    }

    /**
     * Test case to verify that upon a ball being potted
     * in the Game State, the active Player (which is assumed
     * to be player with id 0 by default) pots the ball.
     * Because it is the first regular ball that is potted,
     * both players get a ball type assigned.
     * TODO: Update test case for break shot or unvalid pot.
     */
    @Test
    void testBallPottedPlayerInteraction() {
        assertEquals(players.get(0).getBallType(), RegularBall3D.Type.UNASSIGNED);
        assertEquals(players.get(1).getBallType(), RegularBall3D.Type.UNASSIGNED);

        RegularBall3D ball = (RegularBall3D) balls.get(2);
        assertEquals(ball.getType(), RegularBall3D.Type.FULL);

        // Pot a full regular ball
        gameState.onBallPotted(ball);
        gameState.advanceTurn();

        // Verify the active player (which is the first Player by default
        // after constructing GameState object) pots the ball
        assertTrue(gameState.getGameBallState().getAllPottedBalls().contains(ball));

        // Ensure that both player types are not updated
        // since the ball was potted in the first turn (break shot)
        assertEquals(players.get(0).getBallType(), RegularBall3D.Type.UNASSIGNED);
        assertEquals(players.get(1).getBallType(), RegularBall3D.Type.UNASSIGNED);
    }

    /**
     * Test case to verify that upon a full regular ball
     * being potted in the Game State, the active player and
     * the other player get the right ball type assigned.
     */
    @Test
    void ballTypeAssignmentPlayersFullBall() {
        // Create List of 2 mocked players
        Player player1 = Mockito.mock(Player.class);
        Player player2 = Mockito.mock(Player.class);
        players.clear();
        players.add(player1);
        players.add(player2);

        Mockito.when(player1.getBallType()).thenReturn(RegularBall3D.Type.UNASSIGNED);
        Mockito.when(player2.getBallType()).thenReturn(RegularBall3D.Type.UNASSIGNED);

        // Re-create game state with mocked players
        gameState = new GameState(players, balls);

        RegularBall3D ball = (RegularBall3D) balls.get(2);
        assertEquals(ball.getType(), RegularBall3D.Type.FULL);

        // Pot a full regular ball
        gameState.advanceTurn();
        gameState.onBallPotted(ball);
        gameState.advanceTurn();
        Mockito.verify(player1).assignBallType(RegularBall3D.Type.STRIPED);
        Mockito.verify(player2).assignBallType(RegularBall3D.Type.FULL);
    }

    /**
     * Test case to verify that upon a striped regular ball
     * being potted in the Game State, the active player and
     * the other player get the right ball type assigned.
     */
    @Test
    void ballTypeAssignmentPlayersStripedBall() {
        // Create List of 2 mocked players
        Player player1 = Mockito.mock(Player.class);
        Player player2 = Mockito.mock(Player.class);
        players.clear();
        players.add(player1);
        players.add(player2);

        Mockito.when(player1.getBallType()).thenReturn(RegularBall3D.Type.UNASSIGNED);
        Mockito.when(player2.getBallType()).thenReturn(RegularBall3D.Type.UNASSIGNED);

        // Re-create game state with mocked players
        gameState = new GameState(players, balls);

        RegularBall3D ball = (RegularBall3D) balls.get(4);
        assertEquals(ball.getType(), RegularBall3D.Type.STRIPED);

        // Pot a full regular ball
        gameState.advanceTurn();
        gameState.onBallPotted(ball);
        gameState.advanceTurn();
        Mockito.verify(player1).assignBallType(RegularBall3D.Type.FULL);
        Mockito.verify(player2).assignBallType(RegularBall3D.Type.STRIPED);
    }


    /**
     * Test case to verify that the game is correctly set to running.
     */
    @Test
    void testGameStateIsRunning() {
        assertFalse(gameState.isInMotion());
        gameState.onMotion();
        assertTrue(gameState.isInMotion());
    }

    /**
     * Test case to verify the get active player.
     */
    @Test
    void testGetActivePlayer() {
        int activePlayerIndex = gameState.getTurnHandler().getPlayerTurn();
        assertEquals(players.get(activePlayerIndex), gameState.getTurnHandler().getActivePlayer());
    }

    /**
     * Test case to verify the get inactive player.
     */
    @Test
    void testGetInactivePlayer() {
        int inactivePlayerIndex = gameState.getTurnHandler().getPlayerTurn() + 1;
        assertEquals(players.get(inactivePlayerIndex),
                gameState.getTurnHandler().getNextInactivePlayer());
    }


    /**
     * Test case to verify that the next inactive player
     * becomes the active player when turn advances.
     */
    @Test
    void testGetNextActivePlayer() {
        Player inactivePlayer = gameState.getTurnHandler().getNextInactivePlayer();
        gameState.advanceTurn();
        assertEquals(gameState.getTurnHandler().getActivePlayer(), inactivePlayer);
    }

    /**
     * Test case to verify that the current potted balls
     * List is initialized to empty List initially (after constructcion).
     */
    @Test
    void testGetCurrentPottedBalls() {
        List<Ball3D> currentPotted = gameState.getGameBallState().getCurrentPottedBalls();

        assertNotNull(currentPotted);
        assertEquals(0, currentPotted.size());
    }

    /**
     * Test case to verify that advancing the turn in Game State
     * clears the potted balls.
     */
    @Test
    void testAdvanceTurnClearPottedBalls() {
        final int potCount = 4;

        // Pot balls
        for (int i = 0; i < potCount; ++i) {
            RegularBall3D ball = Mockito.mock(RegularBall3D.class);
            gameState.onBallPotted(ball);
        }

        assertEquals(potCount, gameState.getGameBallState().getCurrentPottedBalls().size());

        gameState.advanceTurn();
        assertEquals(0, gameState.getGameBallState().getCurrentPottedBalls().size());
    }

    /**
     * Test case to verify that upon potting ball in Game State, the
     * current potted balls List is updated accordingly.
     */
    @Test
    void testPotBallsAddToPotted() {
        assertEquals(0, gameState.getGameBallState().getCurrentPottedBalls().size());

        final int potCount = 2;

        // Pot balls
        for (int i = 0; i < potCount; ++i) {
            RegularBall3D ball = Mockito.mock(RegularBall3D.class);
            gameState.onBallPotted(ball);
        }

        assertEquals(potCount, gameState.getGameBallState().getCurrentPottedBalls().size());
    }

    /**
     * Test case to verify that potting multiple balls of
     * the same type as the active player's assigned type
     * properly pots the balls for that Player.
     */
    @Test
    void testHandleBallPottingMultipleSame() {
        final RegularBall3D.Type type = RegularBall3D.Type.FULL;
        final int potCount = 3;

        Player player = gameState.getTurnHandler().getActivePlayer();
        player.assignBallType(type);
        gameState.getBallPottingHandler().setTypesAssigned(true);

        for (int i = 0; i < potCount; ++i) {
            RegularBall3D ball = Mockito.mock(RegularBall3D.class);
            Mockito.when(ball.getType()).thenReturn(type);
            gameState.onBallPotted(ball);
        }

        gameState.advanceTurn();

        assertEquals(potCount, player.getPottedBalls().size());
    }

    /**
     * Test case to verify that potting multiple balls of
     * the same type as the active player's assigned type
     * properly pots the balls for that Player.
     */
    @Test
    void testHandleBallPottingMultipleSDiffering() {
        final RegularBall3D.Type type = RegularBall3D.Type.FULL;
        Player player = gameState.getTurnHandler().getActivePlayer();
        gameState.getBallPottingHandler().setTypesAssigned(true);
        player.assignBallType(type);

        RegularBall3D ball1 = Mockito.mock(RegularBall3D.class);
        RegularBall3D ball2 = Mockito.mock(RegularBall3D.class);

        Mockito.when(ball1.getType()).thenReturn(RegularBall3D.Type.FULL);
        Mockito.when(ball2.getType()).thenReturn(RegularBall3D.Type.STRIPED);

        gameState.getBallPottingHandler().potRegularBall(ball1, gameState.getTurnHandler());
        gameState.getBallPottingHandler().potRegularBall(ball2, gameState.getTurnHandler());

        gameState.advanceTurn();
        assertEquals(1, player.getPottedBalls().size());
    }

    /**
     * Test case to verify that when an eight ball is potted
     * when the Player has not yet potted all of their balls,
     * then the Player loses the game.
     */
    @Test
    void testPotEightBallLossNotAllPotted() {
        balls = constructBallsList(true, true, 2, 2);
        Ball3D eightBall = balls.get(1);

        gameState = new GameState(players, balls);

        // Assign ball type to Player
        gameState.getTurnHandler().getActivePlayer().assignBallType(RegularBall3D.Type.FULL);
        
        // Pot eight ball for current Player
        gameState.onBallPotted(eightBall);
        gameState.advanceTurn();

        Player expectedWinner = gameState.getTurnHandler().getNextInactivePlayer();
        Optional<Player> winner = gameState.getWinningPlayer();

        assertTrue(winner.isPresent());
        assertEquals(expectedWinner, winner.get());
    }

    /**
     * Test case to verify that when all the balls
     * of the Player's assigned type are potted
     * during the current turn together with the 8-ball,
     * a loss occurs.
     */
    @Test
    void testPotEightBallLossPotAll() {
        balls = constructBallsList(true, true, 2, 1);

        gameState = new GameState(players, balls);

        // Assign ball type to Player
        gameState.getTurnHandler().getActivePlayer().assignBallType(RegularBall3D.Type.FULL);

        // Pot balls for current Player
        gameState.onBallPotted(balls.get(2));
        gameState.onBallPotted(balls.get(3));
        gameState.onBallPotted(balls.get(1));
        gameState.advanceTurn();

        Player expectedWinner = gameState.getTurnHandler().getNextInactivePlayer();
        Optional<Player> winner = gameState.getWinningPlayer();

        assertTrue(winner.isPresent());
        assertEquals(expectedWinner, winner.get());
    }


    /**
     * Test case to verify that when -ONLY- an eight ball is potted
     * when the Player has potted all of their balls, then
     * the Player wins the game.
     */
    @Test
    void testPotOnlyEightBallWin() {
        balls = constructBallsList(true, true, 0, 0);

        gameState = new GameState(players, balls);
        gameState.getTurnHandler().getActivePlayer().assignBallType(RegularBall3D.Type.FULL);

        // Pot eight ball for current Player
        gameState.onBallPotted(balls.get(1));
        gameState.advanceTurn();

        Player expectedWinner = gameState.getTurnHandler().getActivePlayer();
        Optional<Player> winner = gameState.getWinningPlayer();

        assertTrue(winner.isPresent());
        assertEquals(expectedWinner, winner.get());
    }

    /**
     * Test case to verify that when the active Player
     * has already potted all of their balls, and they
     * pot the 8-ball, but accidentally pot balls which
     * are not of their type, they still win the game.
     */
    @Test
    void testPotEightBallAndOthersWin() {
        balls = constructBallsList(true, true, 0, 3);

        gameState = new GameState(players, balls);
        gameState.getTurnHandler().getActivePlayer().assignBallType(RegularBall3D.Type.FULL);

        // Pot eight ball for current Player
        gameState.onBallPotted(balls.get(3));
        gameState.onBallPotted(balls.get(1));
        gameState.advanceTurn();

        Player expectedWinner = gameState.getTurnHandler().getActivePlayer();
        Optional<Player> winner = gameState.getWinningPlayer();

        assertTrue(winner.isPresent());
        assertEquals(expectedWinner, winner.get());
    }

    /**
     * Test case to verify that triggering the end game event
     * causes the inner Game State to be tracked as stopped.
     */
    @Test
    void testEndGame() {
        Player winner = Mockito.mock(Player.class);
        Player loser = Mockito.mock(Player.class);

        ArrayList<Player> players = new ArrayList<>();
        players.add(winner);
        players.add(loser);

        gameState.onGameEnded(winner, players);
        assertTrue(gameState.isStopped());
    }

    //    /**
    //     * Test case to verify that when retrieving a winner
    //     * when a winner is not yet determined returns an
    //     * empty optional object.
    //     */
    //    @Test
    //    void testGetWinnerNoWinner() {
    //        Optional<Player> winner = gameState.getWinningPlayer();
    //        assertFalse(winner.isPresent());
    //    }
    //
    //    /**
    //     * Test case to verify that retrieving the ball count
    //     * for solid balls returns the appropriate count.
    //     */
    //    @Test
    //    void testGetCountSolid() {
    //        final int solid = 4;
    //        final int striped = 5;
    //        final RegularBall3D.Type type = RegularBall3D.Type.FULL;
    //
    //        balls = constructBallsList(true, true, solid, striped);
    //        gameState = new GameState(players, balls);
    //
    //        int result = gameState.getRemainingBallCount(type);
    //
    //        assertEquals(result, solid);
    //    }
    //
    //    /**
    //     * Test case to verify that retrieving the ball count
    //     * for striped balls returns the appropriate count.
    //     */
    //    @Test
    //    void testGetCountStriped() {
    //        final int solid = 6;
    //        final int striped = 7;
    //        final RegularBall3D.Type type = RegularBall3D.Type.STRIPED;
    //
    //        balls = constructBallsList(true, true, solid, striped);
    //        gameState = new GameState(players, balls);
    //
    //        int result = gameState.getRemainingBallCount(type);
    //
    //        assertEquals(striped, result);
    //    }

    /**
     * Simple test case to test that at the second turn ball types are assigned as they should.
     */
    @Test
    void testAssignmentSimple() {
        balls = constructBallsList(true, true, 3, 3);
        gameState = new GameState(players, balls);

        gameState.advanceTurn();
        // Now it's player 2's turn
        gameState.onBallPotted(balls.get(2));
        gameState.advanceTurn();
        assertEquals(players.get(1).getBallType(), RegularBall3D.Type.FULL);
        assertEquals(players.get(0).getBallType(), RegularBall3D.Type.STRIPED);
    }

    /**
     * Test case to test that after assignment of types,
     * the assignment does not change.
     */
    @Test
    void testAssignmentDouble() {
        balls = constructBallsList(true, true, 3, 3);
        gameState = new GameState(players, balls);

        gameState.advanceTurn();
        // Now it's player 2's turn
        gameState.onBallPotted(balls.get(2));
        gameState.advanceTurn(); // handle turn events
        assertEquals(players.get(1).getBallType(), RegularBall3D.Type.FULL);
        assertEquals(players.get(0).getBallType(), RegularBall3D.Type.STRIPED);
        gameState.advanceTurn();
        gameState.onBallPotted(balls.get(7));
        assertEquals(players.get(1).getBallType(), RegularBall3D.Type.FULL);
        assertEquals(players.get(0).getBallType(), RegularBall3D.Type.STRIPED);
    }


    /**
     * Test case to test that after assignment of types,
     * the list of all potted balls is set to null.
     */
    @Test
    void testAllPottedBalls() {
        balls = constructBallsList(true, true, 3, 3);
        gameState = new GameState(players, balls);
        gameState.onBallPotted(balls.get(2));
        gameState.onBallPotted(balls.get(0));
        gameState.advanceTurn(); // handle turn events
        assertTrue(gameState.getGameBallState().getAllPottedBalls().contains(balls.get(2)));
        assertFalse(gameState.getGameBallState().getAllPottedBalls().contains(balls.get(0)));
    }

    /**
     * Test case to test that balls potted during
     * the break shot do not impact the ball assignment.
     */
    @Test
    void testAssignmentBreakShot() {
        balls = constructBallsList(true, true, 3, 3);
        gameState = new GameState(players, balls);

        // stillAtBreakShot
        gameState.onBallPotted(balls.get(2));
        gameState.advanceTurn();
        assertEquals(players.get(1).getBallType(), RegularBall3D.Type.UNASSIGNED);
        assertEquals(players.get(0).getBallType(), RegularBall3D.Type.UNASSIGNED);
    }

    /**
     * Test case to test that when the cue ball
     * is potted, no ball types are assigned.
     */
    @Test
    void testAssignmentCuePot() {
        balls = constructBallsList(true, true, 3, 3);
        gameState = new GameState(players, balls);

        gameState.advanceTurn();
        // not at breakshot but since cue ball is potted no type should be assigned
        gameState.onBallPotted(balls.get(0));
        gameState.advanceTurn();
        assertEquals(players.get(1).getBallType(), RegularBall3D.Type.UNASSIGNED);
        assertEquals(players.get(0).getBallType(), RegularBall3D.Type.UNASSIGNED);
    }

    /**
     * Test case to test that balls potted during
     * the break shot are added to a players potted balls,
     * if the player gets assigned a ball type.
     */
    @Test
    void testAssignmentBreakShotPot() {
        balls = constructBallsList(true, true, 3, 3);
        gameState = new GameState(players, balls);

        // pot a two balls during breakshot
        gameState.onBallPotted(balls.get(5));
        gameState.onBallPotted(balls.get(3));
        gameState.advanceTurn(); // handle turn, this results in player 0 keeping their turn
        // Player 0 pots full ball
        gameState.onBallPotted(balls.get(2));
        gameState.advanceTurn(); // handle turn
        assertEquals(players.get(0).getBallType(), RegularBall3D.Type.FULL);
        assertEquals(players.get(1).getBallType(), RegularBall3D.Type.STRIPED);
        assertTrue(players.get(0).getPottedBalls().contains(balls.get(2)));
        assertTrue(players.get(0).getPottedBalls().contains(balls.get(3)));
        assertTrue(players.get(1).getPottedBalls().contains(balls.get(5)));
    }

    /**
     * Test case to assert that the 8-Ball is not added to a players potted balls.
     */
    @Test
    void testAddPottedBallsWithEightBall() {
        balls = constructBallsList(true, true, 3, 3);
        gameState = new GameState(players, balls);

        // pot a two balls during breakshot
        gameState.onBallPotted(balls.get(5));
        gameState.onBallPotted(balls.get(3));
        // if this happens the game should immediately end.
        gameState.advanceTurn();
        // Player 1 pots full ball
        gameState.onBallPotted(balls.get(2));
        gameState.advanceTurn();
        assertEquals(players.get(1).getBallType(), RegularBall3D.Type.STRIPED);
        assertEquals(players.get(0).getBallType(), RegularBall3D.Type.FULL);
        assertTrue(players.get(0).getPottedBalls().contains(balls.get(2)));
        assertTrue(players.get(0).getPottedBalls().contains(balls.get(3)));
        assertTrue(players.get(1).getPottedBalls().contains(balls.get(5)));
        assertFalse(players.get(0).getPottedBalls().contains(balls.get(1)));
        assertFalse(players.get(1).getPottedBalls().contains(balls.get(1)));
    }


    /**
     * Test case to assert that a ball is added to the right player.
     */
    @Test
    void testAddPottedBallToRightPlayer() {
        balls = constructBallsList(true, true, 3, 3);
        gameState = new GameState(players, balls);

        // pot a two balls during breakshot
        gameState.onBallPotted(balls.get(5));
        gameState.onBallPotted(balls.get(3));
        // if this happens the game should immediately end.
        gameState.advanceTurn(); // player 0 keeps the turn here,
        // as a ball was potted during the break shot
        // Player 2 pots full ball
        gameState.onBallPotted(balls.get(2));
        gameState.advanceTurn();
        assertEquals(players.get(0).getBallType(), RegularBall3D.Type.FULL);
        assertEquals(players.get(1).getBallType(), RegularBall3D.Type.STRIPED);
        // Player 1 pots a full ball, should be added to player 2's potted balls
        gameState.onBallPotted(balls.get(3));
        assertTrue(players.get(0).getPottedBalls().contains(balls.get(3)));
    }

    /**
     * Test case to assert that a ball is not in any player's list if
     * players have not been assigned a type yet.
     */
    @Test
    void testAddPottedBallToNoPlayer() {
        balls = constructBallsList(true, true, 3, 3);
        gameState = new GameState(players, balls);

        // pot a ball during breakshot
        gameState.onBallPotted(balls.get(5));
        // if this happens the game should immediately end.
        gameState.advanceTurn();
        assertFalse(players.get(0).getPottedBalls().contains(balls.get(5)));
        assertFalse(players.get(1).getPottedBalls().contains(balls.get(5)));
    }

    /**
     * Test case to verify that potting both the cue ball and the
     * 8-ball for a Player who has already potted all of their balls
     * results in a loss. The scenario is tested when the Player
     * pots the 8-ball, and then the cue ball after.
     */
    @Test
    void testPotCueAndEightBallEightFirst() {
        balls = constructBallsList(true, true, 0, 3);

        gameState = new GameState(players, balls);
        gameState.getTurnHandler().getActivePlayer().assignBallType(RegularBall3D.Type.FULL);

        // Pot eight ball for current Player
        gameState.onBallPotted(balls.get(1));
        gameState.onBallPotted(balls.get(0));
        gameState.advanceTurn();

        // Next Player is the winner
        Player expectedWinner = gameState.getTurnHandler().getNextInactivePlayer();
        Optional<Player> winner = gameState.getWinningPlayer();

        assertTrue(winner.isPresent());
        assertEquals(expectedWinner, winner.get());
    }

    /**
     * Test case to verify that potting both the cue ball and the
     * 8-ball for a Player who has already potted all of their balls
     * results in a loss. The scenario is tested where the Player
     * pots the 8-ball, and then the cue ball right after.
     */
    @Test
    void testPotCueAndEightBallCueBallFirst() {
        balls = constructBallsList(true, true, 4, 0);

        gameState = new GameState(players, balls);
        gameState.getTurnHandler().getActivePlayer().assignBallType(RegularBall3D.Type.STRIPED);

        // Pot eight ball for current Player
        gameState.onBallPotted(balls.get(0));
        gameState.onBallPotted(balls.get(1));
        gameState.advanceTurn();

        // Next Player is the winner
        Player expectedWinner = gameState.getTurnHandler().getNextInactivePlayer();
        Optional<Player> winner = gameState.getWinningPlayer();

        assertTrue(winner.isPresent());
        assertEquals(expectedWinner, winner.get());
    }

    /**
     * Test case to verify that a player keeps their turn if they pot a correct ball
     * while ball types are not yet assigned.
     */
    @Test
    void testKeepTurnAfterBallPotUnassigned() {
        balls = constructBallsList(true, true, 2, 2);

        gameState = new GameState(players, balls);

        // Keep track of current player and pot a ball
        Player current = gameState.getTurnHandler().getActivePlayer();
        gameState.onBallPotted(balls.get(2));
        gameState.onMotionStop(balls.get(2)); // Advances turn

        assertEquals(current, gameState.getTurnHandler().getActivePlayer());
    }

    /**
     * Test case to verify that a player keeps its turn when it pots a ball
     * while ball types are already assigned.
     */
    @Test
    void testKeepTurnAfterBallPotAssigned() {
        balls = constructBallsList(true, true, 2, 2);

        gameState = new GameState(players, balls);

        // Skip break shot
        gameState.advanceTurn();
        // pot a ball to assign types
        gameState.onBallPotted(balls.get(2));
        // This should result in the player keeping their turn (tested in previous test case)
        gameState.onMotionStop(balls.get(2));
        // Keep track of current player and pot another ball
        Player current = gameState.getTurnHandler().getActivePlayer();
        gameState.onBallPotted(balls.get(3));
        gameState.onMotionStop(balls.get(3));
        
        // This should result in the player keeping its turn again
        assertEquals(current, gameState.getTurnHandler().getActivePlayer());
    }

    /**
     * Test case to verify that a player keeps its turn when it pots a ball during the breakshot.
     */
    @Test
    void testKeepTurnAfterBallPotBreakShot() {
        balls = constructBallsList(true, true, 2, 2);

        gameState = new GameState(players, balls);
        
        // Break shot -> pot a ball while keeping track of the player
        Player current = gameState.getTurnHandler().getActivePlayer();
        gameState.onBallPotted(balls.get(2));
        gameState.onMotionStop(balls.get(2));
        
        assertEquals(current, gameState.getTurnHandler().getActivePlayer());
        assertFalse(gameState.getTypesAssigned());
    }

    /**
     * Test case to verify that a player loses their turn if they pot a correct ball,
     * but touch a wrong ball first.
     */
    @Test
    void loseTurnAfterWrongFirstTouch() {
        balls = constructBallsList(true, true, 2, 2);

        gameState = new GameState(players, balls);

        // Break shot
        gameState.advanceTurn();
        // Keep track of current player and pot a ball
        Player current = gameState.getTurnHandler().getActivePlayer();
        gameState.onBallPotted(balls.get(2));
        // However, first touch the 8 ball.
        gameState.onMotionStop(balls.get(0));

        assertNotEquals(current, gameState.getTurnHandler().getActivePlayer());
    }

    /**
     * Test case to verify that a player loses their turn if they pot a correct ball,
     * but pot the cue ball as well.
     */
    @Test
    void loseTurnWhenCueBallPotted() {
        balls = constructBallsList(true, true, 2, 2);

        gameState = new GameState(players, balls);

        // Break shot
        gameState.advanceTurn();
        // Keep track of current player and pot a ball
        gameState.onBallPotted(balls.get(2));
        // Pot the cue ball
        gameState.onBallPotted(balls.get(0));
        Player current = gameState.getTurnHandler().getActivePlayer();

        gameState.onMotionStop(balls.get(2));

        assertNotEquals(current, gameState.getTurnHandler().getActivePlayer());
    }

    /**
     * Test case to verify that a player loses their turn when
     * they only pot a ball of the wrong type.
     */
    @Test
    void loseTurnWhenOnlyWrongBallPotted() {
        balls = constructBallsList(true, true, 2, 2);

        gameState = new GameState(players, balls);

        // Break shot
        gameState.advanceTurn();
        // Keep track of current player and pot a wrong ball
        gameState.getTurnHandler().getActivePlayer().assignBallType(RegularBall3D.Type.STRIPED);
        Player current = gameState.getTurnHandler().getActivePlayer();
        gameState.onBallPotted(balls.get(2));

        gameState.onMotionStop(balls.get(5));

        assertNotEquals(current, gameState.getTurnHandler().getActivePlayer());
    }

    /**
     * Test case to verify that a player loses its turn when it pots the cue ball 
     * as well as a regular ball during the break shot.
     */
    @Test
    void loseTurnWhenCueBallPottedBreakShot() {
        balls = constructBallsList(true, true, 2, 2);

        gameState = new GameState(players, balls);

        // Pot a regular ball and cue ball during breakshot while keeping track of the player
        gameState.onBallPotted(balls.get(2));
        gameState.onBallPotted(balls.get(0));
        Player current = gameState.getTurnHandler().getActivePlayer();
        gameState.onMotionStop(balls.get(2));
        
        assertNotEquals(current, gameState.getTurnHandler().getActivePlayer());
    }

    /**
     * Test case to verify that the player cannot place the cue ball 
     * when the state is not yet idle. 
     */
    @Test
    void placeCueBallNotIdle() {
        GameState spyGameState = Mockito.spy(gameState);
        Mockito.doReturn(false).when(spyGameState).isIdle();
        
        // Pot the cue ball
        spyGameState.onBallPotted(balls.get(0));
        spyGameState.onMotionStop(balls.get(2));
        
        assertFalse(spyGameState.canPlaceCueBall());
    }

    /**
     * Test case to verify that the cue ball cannot be placed when it is not potted.
     */
    @Test
    void placeCueBallNotPotted() {
        GameState spyGameState = Mockito.spy(gameState);
        Mockito.doReturn(true).when(spyGameState).isIdle();
        
        assertFalse(spyGameState.canPlaceCueBall());
    }

    /**
     * Test case to verify that the cue ball can be placed when 
     * it is both potted and the state is idle.
     */
    @Test
    void canPlaceCueBall() {
        GameState spyGameState = Mockito.spy(gameState);
        Mockito.doReturn(true).when(spyGameState).isIdle();

        // Pot the cue ball
        spyGameState.onBallPotted(balls.get(0));
        spyGameState.onMotionStop(balls.get(2));

        assertTrue(spyGameState.canPlaceCueBall());
    }
}
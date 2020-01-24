package com.sem.pool.game;

import java.util.List;

public class TurnHandler {
    private transient List<Player> players;
    private transient int playerTurn;
    private transient int turnCount;

    public TurnHandler(List<Player> players) {
        this.players = players;
        this.turnCount = 0;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    /**
     * Gets the active player.
     * @return active player
     */
    public Player getActivePlayer() {
        return players.get(playerTurn);
    }

    /**
     * Gets the next inactive player.
     * @return an inactive player
     */
    public Player getNextInactivePlayer() {
        return players.get((playerTurn + 1) % players.size());
    }

    /**
     * Initializes the starting player at random.
     */
    public void initializeStartingPlayer() {
        playerTurn = (int) Math.round(Math.random());
    }

    public int getTurnCount() {
        return this.turnCount;
    }

    public void advanceTurn(boolean shouldLoseTurn) {
        Player activePlayer = getActivePlayer();

        // Advance turn to the next Player if the current
        // player should lose their turn.
        if (shouldLoseTurn) {
            loseTurn();
        }

        // Reset temporary variable
        activePlayer.setPottedCorrectBall(false);

        // Increment the turn counter
        turnCount += 1;
    }

    /**
     * Handles the game logic when a player loses its turn.
     */
    protected void loseTurn() {
        // Increment player turn and wrap turn ID around
        // players size to keep it within bounds
        playerTurn = (playerTurn + 1) % players.size();
    }
}

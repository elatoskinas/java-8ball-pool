package com.sem.pool.game;

import java.util.List;

/**
 * Class responsible for handling the turns in a Pool game.
 * Keeps track of the Players and the current turn, as well
 * as the overall turn count.
 */
public class TurnHandler {
    private transient List<Player> players;
    private transient int playerTurn;
    private transient int turnCount;

    /**
     * Creates a new Turn Handler with the specified players.
     * @param players  Players of the Game
     */
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

    /**
     * Advances to the next turn. The end result might be
     * that the player still keeps their turn.
     * @param gameBallState  State of the pool balls
     */
    public void advanceTurn(GameBallState gameBallState) {
        Player activePlayer = getActivePlayer();

        // Advance turn to the next Player if the current
        // player should lose their turn.
        if (doesPlayerLoseTurn(gameBallState)) {
            loseTurn();
        }

        // Reset temporary variable
        activePlayer.setPottedCorrectBall(false);

        // Increment the turn counter
        turnCount += 1;
    }

    /**
     * Determines whether the active Player should lose their current turn.
     * Check for four criteria:
     * - Did the player touch the right type of ball first
     * - Did the player not pot the cue ball
     * - Did the player pot a ball of the wrong type
     * - Did the player pot a ball of the correct type
     *  Special case: if any ball is potted during the break shot, keep the turn
     * @return  True if the active Player should lose their turn.
     */
    private boolean doesPlayerLoseTurn(GameBallState gameBallState) {
        // Not all criteria were satisfied -> player loses the turn
        return !isPlayerRegularPottingValid(gameBallState) || gameBallState.isCueBallPotted();
    }

    /**
     * Checks whether the Player satisfies all conditions for regular
     * ball potting to gain the next turn.
     * @return  True if the Player has not violated any potting rules
     *          for regular (non-cue) ball potting.
     */
    private boolean isPlayerRegularPottingValid(GameBallState gameBallState) {
        boolean isValid = gameBallState.isBallContactValid(getActivePlayer());;
        
        // If break shot, we only care if the Player potted
        // any balls at all
        if (getTurnCount() == 0) {
            return gameBallState.existsPottedPreassignedBall();
        } else {
            // If not break shot, we have to verify
            // that the Player touched & potted the
            // correct ball type.
            return isValid;
        }
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

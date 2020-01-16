package com.sem.pool.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sem.pool.game.Game;

/**
 * GameUI that adds and updates all UI elements.
 */
public class GameUI {
    private transient Label playerTurnLabel;
    private transient Label cueForceLabel;
    private transient Label ballTypePlayerOneLabel;
    private transient Label ballTypePlayerTwoLabel;
    private transient Button restartButton;
    private transient Stage stage;
    private transient Skin skin;

    /**
     * Class representing the game UI.
     */
    public GameUI() {
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        TextureAtlas atlas = new TextureAtlas("uiskin.atlas");
        skin = new Skin(Gdx.files.internal("config/skin/uiskin.json"), atlas);

    }

    /**
     * Add player turn label.
     */
    private void addPlayerTurnLabel() {
        playerTurnLabel = new Label("", skin);
        playerTurnLabel.setPosition(100, Gdx.graphics.getHeight() - 100);
    }

    /**
     * Add cue force label.
     */
    private void addCueForceLabel() {
        cueForceLabel = new Label("", skin);
        cueForceLabel.setPosition(Gdx.graphics.getWidth() - 200,Gdx.graphics.getHeight() - 100);
    }

    /**
     * Add restart button.
     * @param game Game to restart.
     */
    private void addRestartButton(Game game) {
        restartButton = new TextButton("Restart", skin);
        restartButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                game.restartGame();
            }
        });
        restartButton.setPosition(Gdx.graphics.getWidth() - 150, Gdx.graphics.getHeight() - 50);
    }

    /**
     * Add ball type label for players.
     */
    private void addBallTypeLabels() {
        ballTypePlayerOneLabel = new Label("Player 1: No ball type", skin);
        ballTypePlayerOneLabel.setPosition(100,100);

        ballTypePlayerTwoLabel = new Label("Player 2: No ball type", skin);
        ballTypePlayerTwoLabel.setPosition(100,75);
    }

    /**
     * Add all actors/UI-elements to the stage.
     */
    private void addActors() {
        stage.addActor(playerTurnLabel);
        stage.addActor(ballTypePlayerOneLabel);
        stage.addActor(ballTypePlayerTwoLabel);
        stage.addActor(cueForceLabel);
        stage.addActor(restartButton);
    }

    /**
     * Updates the force label.
     * @param scene the scene to get the force of the cue.
     */
    public void updateForceLabel(Scene3D scene) {
        float forcePercentage = scene.getCue().getRelativeForcePercentage();
        cueForceLabel.setText("Force: " + forcePercentage + "%");
    }

    /**
     * Updates the player turn label.
     * @param game the game to get the player turn.
     */
    public void updatePlayerTurnLabel(Game game) {
        playerTurnLabel.setText("Player turn: " + game.getState().getPlayerTurn());
    }

    /**
     * Updates the ball type labels.
     * @param game the game to get the player turn.
     */
    public void updateBallTypeLabels(Game game) {
        if (game.getState().getTypesAssigned()) {
            String ballTypePlayerOne = game.getState().getPlayers().get(0).getBallType().toString();
            ballTypePlayerOneLabel.setText("Player 1: " + ballTypePlayerOne);

            String ballTypePlayerTwo = game.getState().getPlayers().get(1).getBallType().toString();
            ballTypePlayerTwoLabel.setText("Player 2: " + ballTypePlayerTwo);
        }
    }

    /**
     * Renders the UI.
     */
    public void render() {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    /**
     * Create all UI elements and add them to the stage.
     * @param game the game which is needed to create the restart button.
     */
    public void createUI(Game game) {
        addPlayerTurnLabel();
        addCueForceLabel();
        addRestartButton(game);
        addBallTypeLabels();
        addActors();
    }
}

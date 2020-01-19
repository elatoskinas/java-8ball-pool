package com.sem.pool.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sem.pool.game.Game;

/**
 * GameUI that adds and updates all UI elements.
 */
public class GameUI {
    private transient Label playerTurnLabel;
    private transient Label cueForceLabel;
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
        playerTurnLabel.setSize(200,50);
        playerTurnLabel.setPosition(80, Gdx.graphics.getHeight() - 100);
    }

    /**
     * Add cue force label.
     */
    private void addCueForceLabel() {
        cueForceLabel = new Label("", skin);
        cueForceLabel.setSize(200,50);
        cueForceLabel.setPosition(Gdx.graphics.getWidth() - 200,Gdx.graphics.getHeight() - 100);
    }

    /**
     * Add all actors/UI-elements to the stage.
     */
    private void addActors() {
        stage.addActor(playerTurnLabel);
        stage.addActor(cueForceLabel);
        //  stage.addActor(restartButton);
    }

    /**
     * Updates the force label.
     * @param scene the scene to get the force of the cue.
     */
    public void updateForceLabel(Scene3D scene) {
        int forcePercentage = scene.getCue().getRelativeForcePercentage();
        cueForceLabel.setText("Force: " + forcePercentage + "%");
    }

    /**
     * Updates the player turn label.
     * @param game the game to get the player turn.
     */
    public void updatePlayerTurnLabel(Game game) {
        // + 1 to have player 1 and 2 instead of player 0 and 1
        int playerTurn = game.getState().getPlayerTurn() + 1;
        playerTurnLabel.setText("Turn: Player " + playerTurn);
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
     */
    public void createUI() {
        addPlayerTurnLabel();
        addCueForceLabel();

        addActors();
    }
}

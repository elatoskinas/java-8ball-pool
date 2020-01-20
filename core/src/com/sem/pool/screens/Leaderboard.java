package com.sem.pool.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sem.pool.database.Database;
import com.sem.pool.database.controllers.ResultController;
import com.sem.pool.database.controllers.StatsController;
import com.sem.pool.database.controllers.UserController;
import com.sem.pool.database.models.Stats;
import com.sem.pool.database.models.User;

import java.util.ArrayList;

public class Leaderboard extends UiScreen implements Screen {
    private transient StatsController statsController;

    public Leaderboard(MainGame game) {
        super(game);
        this.statsController = new StatsController(Database.getInstance());
    }

    /**
     * Show the screen.
     * This actually renders the screen.
     */
    @Override
    public void show() {
        // Set up the screen.
        this.stage = new Stage(new FitViewport(1000, 1000));
        Gdx.input.setInputProcessor(stage);
        this.atlas = new TextureAtlas("uiskin.atlas");
        this.skin = new Skin(Gdx.files.internal("config/skin/uiskin.json"), this.atlas);

        // Render the elements.
        Table table = new Table();
        table.setFillParent(true);
        table.setPosition(0, 0);
        table.defaults().spaceBottom(10);
        table.row().fill().expandX().row();
        this.showHeader(table);
        this.showWinner(table);
        this.showTop(table);
        this.showReturn(table);

        // Push it out.
        table.pack();
        stage.addActor(table);
        stage.act();
        stage.draw();
    }

    /**
     * Show the title of the page.
     * @param table The table to add to.
     */
    private void showHeader(Table table) {
        Label out = new Label("Leaderboard", this.skin);
        out.setFontScale(1.5f);
        table.add(out).row();
    }

    /**
     * Show the title of the page.
     * @param table The table to add to.
     */
    private void showWinner(Table table) {
        String text = this.game.getWinner().getUsername() + " has won the game!";
        Label out = new Label(text, this.skin);
        out.setColor(0, 1, 0, 1);
        table.add(out).row();
    }

    /**
     * Show the title of the page.
     * @param table The table to add to.
     */
    private void showTop(Table table) {
        Label placeTitle = new Label("#", this.skin);
        placeTitle.setColor(1, 1, 1, 0.7f);
        table.add(placeTitle);

        Label usernameTitle = new Label("Player", this.skin);
        usernameTitle.setColor(1, 1, 1, 0.7f);
        table.add(usernameTitle);

        Label winLossTitle = new Label("W/L", this.skin);
        winLossTitle.setColor(1, 1, 1, 0.7f);
        table.add(winLossTitle);

        Label gameCountTitle = new Label("Total Games", this.skin);
        gameCountTitle.setColor(1, 1, 1, 0.7f);
        table.add(gameCountTitle);

        table.row();

        int index = 1;
        for(Stats stat : this.statsController.getTop()) {
            Label place = new Label(Integer.toString(index), this.skin);
            table.add(place);

            Label username = new Label(stat.getUser().getUsername(), this.skin);
            table.add(username);

            float wl = (float) Math.floor(stat.getWL() * 100) / 100;
            Label winLoss = new Label(Float.toString(wl), this.skin);
            table.add(winLoss);

            Label gameCount = new Label(Integer.toString(stat.getGameCount()), this.skin);
            table.add(gameCount);

            table.row();
            index++;
        }
    }

    /**
     * Show the return button, which sends the user back to the login screen.
     * @param table The table to add to.
     */
    private void showReturn(Table table) {
        Leaderboard screen = this;
        Button ret = new TextButton("Return", this.skin);

        ret.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                screen.handleReturn();
            }
        });

        table.add(ret).colspan(2);
    }

    /**
     * Handle the return button click.
     */
    private void handleReturn() {
        this.game.setScreen(new Login(this.game));
        this.game.reset();
    }

    /**
     * Render the screen.
     * @param delta Delta time in seconds between the last render
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.04f, 0.42f, 0.01f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }
}

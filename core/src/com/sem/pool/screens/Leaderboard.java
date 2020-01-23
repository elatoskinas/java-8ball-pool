package com.sem.pool.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sem.pool.database.Database;
import com.sem.pool.database.controllers.StatsController;
import com.sem.pool.database.models.Stats;

/**
 * Leaderboard view.
 * Shows the winner of the game, and the top players.
 */
public class Leaderboard extends UiScreen {
    private transient StatsController statsController;

    /**
     * Create the new leaderboard screen.
     * @param game The game to pull game state from.
     */
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
        super.show();

        // Render the elements.
        this.showHeader();
        this.showWinner();
        this.showTop();
        this.showReturn();

        // Push it out.
        this.table.pack();
        this.stage.addActor(this.table);
        this.stage.act();
        this.stage.draw();
    }

    /**
     * Show the title of the page.
     */
    private void showHeader() {
        Label out = new Label("Leaderboard", this.skin);
        out.setFontScale(1.5f);
        table.add(out).row();
    }

    /**
     * Show the title of the page.
     */
    private void showWinner() {
        String text = this.game.getWinner().getUsername() + " has won the game!";
        Label out = new Label(text, this.skin);
        out.setColor(0, 1, 0, 1);
        table.add(out).row();
    }

    /**
     * Show the actual leaderboard.
     */
    private void showTop() {
        Label placeTitle = new Label("#", this.skin);
        placeTitle.setColor(1, 1, 1, 0.7f);
        this.table.add(placeTitle);

        Label usernameTitle = new Label("Player", this.skin);
        usernameTitle.setColor(1, 1, 1, 0.7f);
        this.table.add(usernameTitle);

        Label winLossTitle = new Label("W/L", this.skin);
        winLossTitle.setColor(1, 1, 1, 0.7f);
        this.table.add(winLossTitle);

        Label gameCountTitle = new Label("Total Games", this.skin);
        gameCountTitle.setColor(1, 1, 1, 0.7f);
        this.table.add(gameCountTitle);

        this.table.row();

        this.showTopList();
    }

    /**
     * Render the top players on the screen.
     * Warnings suppressed as this is an known bug within PMD.
     */
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    private void showTopList() {
        int index = 1;

        for (Stats stat : this.statsController.getTop()) {
            Label place = new Label(Integer.toString(index), this.skin);
            this.table.add(place);

            Label username = new Label(stat.getUser().getUsername(), this.skin);
            this.table.add(username);

            float wl = (float) Math.floor(stat.getWinLossRatio() * 100) / 100;
            Label winLoss = new Label(Float.toString(wl), this.skin);
            this.table.add(winLoss);

            Label gameCount = new Label(Integer.toString(stat.getGameCount()), this.skin);
            this.table.add(gameCount);

            this.table.row();
            index++;
        }
    }

    /**
     * Show the return button, which sends the user back to the login screen.
     */
    private void showReturn() {
        Leaderboard screen = this;
        TextButton ret = new TextButton("Return", this.skin);

        ret.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                screen.handleReturn();
            }
        });

        this.table.add(ret).colspan(2);
    }

    /**
     * Handle the return button click.
     */
    private void handleReturn() {
        this.game.setScreen(new Login(this.game));
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

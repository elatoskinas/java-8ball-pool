package com.sem.pool.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sem.pool.database.Database;
import com.sem.pool.database.controllers.UserController;
import com.sem.pool.database.models.User;

import java.util.ArrayList;

/**
 * Opponent selection view.
 */
public class SelectOpponent extends UiScreen {
    private transient List<User> list;
    private transient UserController userController;

    /**
     * Create the opponent selection view.
     * @param game The game to put the game state in.
     */
    public SelectOpponent(MainGame game) {
        super(game);
        this.userController = new UserController(Database.getInstance());
    }

    /**
     * Show the screen.
     * This actually renders the screen.
     */
    @Override
    public void show() {
        super.show();

        this.showHeader();
        this.list = this.showList();
        this.showSubmit();

        // Push it out.
        this.table.pack();
        this.stage.addActor(this.table);
        this.stage.act();
        this.stage.draw();
    }

    /**
     * Show the output label on the screen.
     */
    private void showHeader() {
        Label out = new Label("Select an opponent", this.skin);
        out.setFontScale(1.5f);
        this.table.add(out).row();
    }

    /**
     * Show the list of opponents.
     * @return The list added.
     */
    private List<User> showList() {
        List<User> out = new List<>(this.skin);

        ArrayList<User> userList = this.userController.getUsers();
        userList.remove(this.game.getPlayer());
        User[] users = userList.toArray(new User[0]);
        out.setItems(users);

        if (userList.size() == 0) {
            this.showEmpty();
        }

        this.table.add(out).row();
        return out;
    }

    /**
     * Show a message that there are no available opponents.
     */
    private void showEmpty() {
        Label out = new Label("There are no opponents to choose from :(", this.skin);
        out.setColor(1, 0, 0, 1);
        this.table.add(out).row();
    }

    /**
     * Show the submit button.
     */
    private void showSubmit() {
        SelectOpponent screen = this;
        TextButton submit = new TextButton("Begin Game", this.skin);

        submit.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                screen.handleSubmit();
            }
        });

        this.table.add(submit).colspan(2);
    }

    /**
     * Handle a submission.
     * This will start the game.
     */
    private void handleSubmit() {
        if (this.list.getSelected() == null) {
            return;
        }

        this.game.setOpponent(this.list.getSelected());
        this.game.setScreen(new Pool(this.game));
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

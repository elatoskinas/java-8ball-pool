package com.sem.pool.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sem.pool.database.Database;
import com.sem.pool.database.controllers.UserController;
import com.sem.pool.database.models.User;

import java.util.ArrayList;

public class SelectOpponent extends UiScreen implements Screen {
    private transient List<User> list;
    private transient UserController userController;

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
        this.list = this.showList(table);
        this.showSubmit(table);

        // Push it out.
        table.pack();
        stage.addActor(table);
        stage.act();
        stage.draw();
    }

    /**
     * Show the output label on the screen.
     *
     * @param table The table to add to.
     * @return The label to change when there is a message.
     */
    private Label showHeader(Table table) {
        Label out = new Label("Select an opponent", this.skin);
        out.setFontScale(1.5f);
        table.add(out).row();

        return out;
    }

    /**
     * Show the list of opponents.
     * @param table The table to add it to.
     * @return The list added.
     */
    private List<User> showList(Table table) {
        List<User> out = new List<>(this.skin);

        ArrayList<User> userList = this.userController.getUsers();
        userList.remove(this.game.getPlayer());
        User[] users = userList.toArray(new User[0]);
        out.setItems(users);

        if (userList.size() == 0) {
            this.showEmpty(table);
        }

        table.add(out).row();
        return out;
    }

    /**
     * Show a message that there are no available opponents.
     * @param table The table to insert to.
     * @return The new message.
     */
    private Label showEmpty(Table table) {
        Label out = new Label("There are no opponents to choose from :(", this.skin);
        out.setColor(1, 0, 0, 1);
        table.add(out).row();

        return out;
    }

    /**
     * Show the submit button.
     * @param table The table to add it to.
     * @return The button added.
     */
    private TextButton showSubmit(Table table) {
        SelectOpponent screen = this;
        TextButton submit = new TextButton("Begin Game", this.skin);

        submit.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                screen.handleSubmit();
            }
        });

        table.add(submit).colspan(2);

        return submit;
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

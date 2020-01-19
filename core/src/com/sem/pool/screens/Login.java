package com.sem.pool.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sem.pool.database.Database;
import com.sem.pool.database.controllers.UserController;
import com.sem.pool.database.models.User;

import java.util.ArrayList;

/**
 * This class implements the Login screen.
 */
public class Login implements Screen {
    private transient MainGame game;
    private transient Stage stage;
    private transient Skin skin;
    private transient TextureAtlas atlas;
    private transient TextField userfield;
    private transient TextField passfield;
    private transient Label outLabel;
    private transient UserController userController;

    public Login(MainGame game) {
        this.game = game;
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
        Table table = new Table();
        table.setFillParent(true);
        this.atlas = new TextureAtlas("uiskin.atlas");
        this.skin = new Skin(Gdx.files.internal("config/skin/uiskin.json"), this.atlas);

        // Render the elements.
        table.setPosition(0, 0);
        table.defaults().spaceBottom(10);
        table.row().fill().expandX();
        this.outLabel = this.showOutput(table);
        this.userfield = this.showUsername(table);
        this.passfield = this.showPassword(table);
        this.showButtons(table);

        // Push it out.
        table.pack();
        stage.addActor(table);
        stage.act();
        stage.draw();
    }

    /**
     * Render the screen.
     *
     * @param delta Delta time in seconds between the last render
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.04f, 0.42f, 0.01f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    /**
     * Resize event of the screen.
     *
     * @param w The new width
     * @param h The new height
     */
    @Override
    public void resize(int w, int h) {
        stage.getViewport().update(w, h);
    }

    /**
     * Pausing of the screen.
     * This is currently not needed, so it is empty.
     */
    @Override
    public void pause() {
    }

    /**
     * Resuming of the paused screen.
     * It's currently not possible to pause, so you can't resume either.
     */
    @Override
    public void resume() {
    }

    /**
     * Hide the screen.
     * This is currently just killing the screen.
     */
    @Override
    public void hide() {
        this.dispose();
    }

    /**
     * Dispose of the screen.
     * Cleaning up any old objects.
     */
    @Override
    public void dispose() {
        this.stage.dispose();
        this.skin.dispose();
        this.atlas.dispose();
    }

    /**
     * Show the username field.
     *
     * @param table The table to add the username to.
     * @return The text field of the username.
     */
    private TextField showUsername(Table table) {
        Label usernameLabel = new Label("Username: ", this.skin);
        TextField username = new TextField("", this.skin);
        username.setMessageText("john.doe");

        table.add(usernameLabel).colspan(2);
        table.add(username).minWidth(100).expandX().fillX().colspan(2);
        table.row();

        return username;
    }

    /**
     * Show the password field.
     *
     * @param table The table to add the password to.
     * @return The text field of the password
     */
    private TextField showPassword(Table table) {
        TextField password = new TextField("", this.skin);
        password.setMessageText("P@ssWord");
        password.setPasswordCharacter('*');
        password.setPasswordMode(true);
        Label passwordLabel = new Label("Password: ", this.skin);

        table.add(passwordLabel).colspan(2);
        table.add(password).minWidth(100).expandX().fillX().colspan(2);
        table.row();

        return password;
    }

    /**
     * Show the output label on the screen.
     *
     * @param table The table to add to.
     * @return The label to change when there is a message.
     */
    private Label showOutput(Table table) {
        Label out = new Label("", this.skin);
        table.add(out);
        table.row();

        return out;
    }

    /**
     * Show the login & register buttons.
     *
     * @param table The table to add the buttons to.
     */
    private void showButtons(Table table) {
        Login screen = this;
        Button login = new TextButton("Login", this.skin);
        Button register = new TextButton("Register", this.skin);

        login.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                screen.handleLogin();
            }
        });

        register.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                screen.handleRegister();
            }
        });

        table.add(login).colspan(2);
        table.add(register).colspan(2);
    }

    /**
     * Handle login button press.
     */
    private void handleLogin() {
        String username = this.userfield.getText();
        String password = this.passfield.getText();

        if (username.length() <= 0 || password.length() <= 0) {
            this.outLabel.setText("Please fill in both username and password!");
            return;
        }

        User user = this.userController.login(username, password);

        if (user == null) {
            this.outLabel.setText("Invalid username/password!");
            return;
        }

        this.startGame(user);
    }

    /**
     * Handle registration button press.
     */
    private void handleRegister() {
        String username = this.userfield.getText();
        String password = this.passfield.getText();

        if (username.length() <= 0 || password.length() <= 0) {
            this.outLabel.setText("Please fill in both username and password!");
            return;
        }

        User user = this.userController.register(username, password);

        if (user == null) {
            this.outLabel.setText("User already exists!");
            return;
        }

        this.startGame(user);
    }

    // The system.exit() call is an exceptional case, where the program can't recover from.
    // Therefor it should close the program.
    @SuppressWarnings({"PMD.DoNotCallSystemExit"})
    private void startGame(User user) {
        // TODO: Let the user select there opponent and handle this more gracefully.
        User opponent = this.userController.getUser(0);
        if (opponent == null) {
            System.out.println("Failed to launch game, does player `0` exists?");
            System.exit(1);
        }

        ArrayList<User> users = new ArrayList<>();
        users.add(user);
        users.add(opponent);

        // TODO: Send the users list to the game.
        this.game.startPool();
    }
}
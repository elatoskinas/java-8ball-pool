package com.sem.pool.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sem.pool.database.Database;
import com.sem.pool.database.controllers.UserController;
import com.sem.pool.database.models.User;

/**
 * This class implements the Login screen.
 */
public class Login extends UiScreen {
    private transient TextField userfield;
    private transient TextField passfield;
    private transient Label outLabel;

    public Login(MainGame game) {
        super(game);
    }

    /**
     * Show the screen.
     * This actually renders the screen.
     */
    @Override
    public void show() {
        super.show();

        // Render the elements.
        this.outLabel = this.showOutput();
        this.userfield = this.showUsername();
        this.passfield = this.showPassword();
        this.showButtons();

        // Push it out.
        this.table.pack();
        this.stage.addActor(this.table);
        this.stage.act();
        this.stage.draw();
    }

    /**
     * Render the screen.
     * @param delta Delta time in seconds between the last render
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.04f, 0.42f, 0.01f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.stage.act(Gdx.graphics.getDeltaTime());
        this.stage.draw();
    }

    /**
     * Show the username field.
     *
     * @return The text field of the username.
     */
    private TextField showUsername() {
        Label usernameLabel = new Label("Username: ", this.skin);
        TextField username = new TextField("", this.skin);
        username.setMessageText("john.doe");

        this.table.add(usernameLabel).colspan(2);
        this.table.add(username).minWidth(100).expandX().fillX().colspan(2);
        this.table.row();

        return username;
    }

    /**
     * Show the password field.
     * @return The text field of the password
     */
    private TextField showPassword() {
        TextField password = new TextField("", this.skin);
        password.setMessageText("P@ssWord");
        password.setPasswordCharacter('*');
        password.setPasswordMode(true);
        Label passwordLabel = new Label("Password: ", this.skin);

        this.table.add(passwordLabel).colspan(2);
        this.table.add(password).minWidth(100).expandX().fillX().colspan(2);
        this.table.row();

        return password;
    }

    /**
     * Show the output label on the screen.
     * @return The label to change when there is a message.
     */
    private Label showOutput() {
        Label out = new Label("", this.skin);
        this.table.add(out);
        this.table.row();

        return out;
    }

    /**
     * Show the login & register buttons.
     */
    private void showButtons() {
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

        this.table.add(login).colspan(2);
        this.table.add(register).colspan(2);
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

        UserController userController = new UserController(Database.getInstance());
        User user = userController.login(username, password);

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

        UserController userController = new UserController(Database.getInstance());
        User user = userController.register(username, password);

        if (user == null) {
            this.outLabel.setText("User already exists!");
            return;
        }

        this.startGame(user);
    }

    /**
     * Start the game.
     * @param user The user to play as.
     */
    private void startGame(User user) {
        this.game.setPlayer(user);
        this.game.setScreen(new SelectOpponent(this.game));
    }
}
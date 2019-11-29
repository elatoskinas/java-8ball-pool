package com.sem.pool;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Splash extends ApplicationAdapter {
    private Stage stage;
    @Override
    public void create () {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);

        Skin uiSkin = new Skin(Gdx.files.internal("config/skin/uiskin.json"));

        Button login = new TextButton("Login", uiSkin);

        Button register = new TextButton("Register", uiSkin);
        register.addListener(new ClickListener() {
            public void clicked () {
                System.out.println("Changed!");
            }
        });
        table.add(register);

        Label usernameLabel = new Label("Username: ", uiSkin);
        TextField username = new TextField("JohnDoe", uiSkin);

        Label passwordLabel = new Label("Password: ", uiSkin);
        TextField password = new TextField("password123", uiSkin);
        password.setMessageText("password");
        password.setPasswordCharacter('*');
        password.setPasswordMode(true);

        password.setTextFieldListener(new TextField.TextFieldListener() {
            public void keyTyped (TextField textField, char key) {
                if (key == '\n') textField.getOnscreenKeyboard().show(false);
            }
        });

        table.setPosition(0, 0);
        table.defaults().spaceBottom(10);
        table.row().fill().expandX();
        table.add(usernameLabel).colspan(2);
        table.add(username).minWidth(100).expandX().fillX().colspan(2);
        table.row();
        table.add(passwordLabel).colspan(2);
        table.add(password).minWidth(100).expandX().fillX().colspan(2);
        table.row();
        table.add(login).colspan(2);
        table.add(register).colspan(2);
        table.pack();
        stage.addActor(table);
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }
}
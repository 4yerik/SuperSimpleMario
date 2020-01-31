package com.example.yeri.supersimplemario.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.example.yeri.supersimplemario.Scenes.Hud;
import com.example.yeri.supersimplemario.SuperSimpleMario;

/**
 * Created by Yeri on 2017-01-04.
 */

public class LevelCompleteScreen implements Screen {
    private Viewport viewport;
    private Stage stage;

    private Game game;

    public LevelCompleteScreen(Game game)
    {
        this.game = game;
        viewport = new FitViewport(SuperSimpleMario.V_WIDTH, SuperSimpleMario.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((SuperSimpleMario) game).batch);

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE); // white font

        Table table = new Table();
        table.center();
        table.setFillParent(true); // takes up all the stage space

        Label levelCompleteLabel = new Label("LEVEL COMPLETE!", font);
        Label playNextLabel = new Label("Click to play the next level...", font);

        table.add(levelCompleteLabel).expandX();
        table.row();
        table.add(playNextLabel).expandX().padTop(10f);



        stage.addActor(table);
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (Gdx.input.justTouched()) // if clicked, the user will advance to the next level
        {
            SuperSimpleMario.level++;
            game.setScreen(new PlayScreen((SuperSimpleMario)game));
            Hud.noTime = false;
            dispose();

        }
        Gdx.gl.glClearColor(1, 0, 1, 0); // pink background
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();

    }
}

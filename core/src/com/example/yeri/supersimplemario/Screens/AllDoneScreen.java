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
import com.example.yeri.supersimplemario.SuperSimpleMario;

/**
 * Created by Yeri on 2017-01-06.
 */

public class AllDoneScreen implements Screen {

    private Viewport viewport;
    private Stage stage;

    private Game game;

    public AllDoneScreen(Game game) {
        this.game = game;
        viewport = new FitViewport(SuperSimpleMario.V_WIDTH, SuperSimpleMario.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((SuperSimpleMario) game).batch);

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        Table table = new Table();
        table.center(); // centers the table
        table.setFillParent(true); // table fills the space that is available (given space by stage)

        Label levelCompleteLabel = new Label("ALL 3 LEVELS ARE COMPLETE!", font); // labels
        Label clickToRestart = new Label("Click to restart", font); // labels
        Label totalScoreLabel = new Label("Your total score for all 3 levels is " + Integer.toString(SuperSimpleMario.totalScore), font); // labels
        Label totalTimeLabel = new Label("Your total time for all 3 levels is " + Integer.toString(SuperSimpleMario.totalTime) + " seconds", font); // labels

        table.add(levelCompleteLabel).expandX(); // adding label to the table

        table.row();
        table.add(clickToRestart).expandX().padTop(20f); // adding the label to the table

        table.row();
        table.add(totalScoreLabel).expandX().padTop(40f);

        table.row();
        table.add(totalTimeLabel).expandX().padTop(20f);

        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        if (Gdx.input.justTouched()) // if the AllDoneScreen is clicked, it will go back to level one
        {
            SuperSimpleMario.level = 1;
            game.setScreen(new PlayScreen((SuperSimpleMario)game));
            dispose();
        }
        Gdx.gl.glClearColor(0, 0, 0, 0); // sets the clear screen colour to blue
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // clears the screen
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

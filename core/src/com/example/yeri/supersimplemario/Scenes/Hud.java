package com.example.yeri.supersimplemario.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.example.yeri.supersimplemario.Sprites.Mario;
import com.example.yeri.supersimplemario.SuperSimpleMario;


/**
 * Created by Yeri on 2016-12-26.
 */

public class Hud implements Disposable{
    public static boolean noTime; // a boolean that will be set to true if worldTimer reaches 0

    public Stage stage;
    private Viewport viewport;

    // variables for the time and score trackers
    private static Integer worldTimer;
    private float timeCount;
    private static Integer score;

    // variables for the labels
    private Label countdownLabel;
    private Label timeLabel;
    private Label levelLabel;
    private Label worldLabel;
    private Label marioLabel;
    private static Label scoreLabel;

    public Hud (SpriteBatch sb)
    {
        worldTimer = 300; // timer starts with 300 seconds
        timeCount = 0; //
        score = 0; // the score that the user collects

        viewport = new FitViewport(SuperSimpleMario.V_WIDTH, SuperSimpleMario.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table(); // make a new table to organize the labels in
        table.top(); // at the top of the table
        table.setFillParent(true); // the table fills all the space it is given

        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        levelLabel = new Label("1-" + Integer.toString(SuperSimpleMario.level), new Label.LabelStyle(new BitmapFont(), Color.WHITE));


        worldLabel = new Label("WORLD", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        marioLabel = new Label("SCORE", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(marioLabel).expandX().padTop(10); // applies the labels with spacing to separate it from the top of the screen
        table.add(worldLabel).expandX().padTop(10); // expandX, has it stretched horizontally, shares the row with 2 other labels
        table.add(timeLabel).expandX().padTop(10);

        table.row(); // new row
        table.add(scoreLabel).expandX(); // the other labels are than added with no padding
        table.add(levelLabel).expandX();
        table.add(countdownLabel).expandX();

        stage.addActor(table);

    }

    public void update(float dt)
    {
        timeCount += dt;
        if (timeCount >= 1 && !Mario.levelComplete) // if one second has pased and the level is not complete
        {
            if (worldTimer != 0 && !Mario.marioIsDead) // if there is still time left and mario is not dead,
            {
                worldTimer--; // time will be taken away
                countdownLabel.setText(String.format("%03d", worldTimer)); // and labels will be updated
                timeCount = 0; // reset 1-second timer to 0
            }
            else// there must be no time left which should mean mario dies
            {
                noTime= true; // no time left
            }

        }
    }
    public static void addScore(int value)
    {
        score += value; // value is taken in as parameters to add to the total score
        scoreLabel.setText(String.format("%06d", score)); // label is updated with new text
    }

    public static void totalAdder () // for the all done screen (last screen) to display culminative score and time
    {
        SuperSimpleMario.totalScore += score;
        SuperSimpleMario.totalTime += 300-worldTimer;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}


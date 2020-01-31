package com.example.yeri.supersimplemario.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.example.yeri.supersimplemario.Items.Item;
import com.example.yeri.supersimplemario.Items.ItemDef;
import com.example.yeri.supersimplemario.Items.Mushroom;
import com.example.yeri.supersimplemario.Scenes.Hud;
import com.example.yeri.supersimplemario.Sprites.Enemy;
import com.example.yeri.supersimplemario.Sprites.Mario;
import com.example.yeri.supersimplemario.SuperSimpleMario;
import com.example.yeri.supersimplemario.Tools.B2WorldCreator;
import com.example.yeri.supersimplemario.Tools.WorldContactListener;


import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Yeri on 2016-12-22.
 */

public class PlayScreen implements Screen {
    private SuperSimpleMario game;
    private TextureAtlas atlas;

    // playscreen variables
    private OrthographicCamera gamecam; // projects using a viewport
    private Viewport gamePort;
    private Hud hud; // the score, level and time tracker

    // variables for the tiled map
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    // Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    // sprites
    private Mario player;

    // music
    private Music music;

    // items
    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;

    // if mario is locked than it will ignore all the input keys that typically determine mario's movements
    public static boolean marioLocked;

    public PlayScreen(SuperSimpleMario game){
        marioLocked = false;
        Mario.levelComplete = false;
        Mario.atCastle = false;
        Mario.marioIsDead = false;

        // it will use graphics from the image package called "Mario_and_Enemies" which is found in the asests folder
        atlas = new TextureAtlas("Mario_and_Enemies.pack");

        this.game = game;
        gamecam = new OrthographicCamera(); // camera to follow mario

        // maintain aspect ratio
        gamePort = new FitViewport(SuperSimpleMario.V_WIDTH/SuperSimpleMario.PPM, SuperSimpleMario.V_HEIGHT/SuperSimpleMario.PPM, gamecam);

        // make hud keep scores, timers and level info
        hud = new Hud(game.batch);

        // load map and set up the map renderer
        mapLoader = new TmxMapLoader();

        // determines which tmx file (tile map) it needs to load
        if (SuperSimpleMario.level == 1)
        {
            map = mapLoader.load("levelOne.tmx");
        }
        else if (SuperSimpleMario.level == 2)
        {
            map = mapLoader.load("levelTwo.tmx");
        }
        else if (SuperSimpleMario.level == 3)
        {
            map = mapLoader.load("levelThree.tmx");
        }

        // sets up the renderer to the map which has a pixel value of 1/pixels per meter
        renderer = new OrthogonalTiledMapRenderer(map, 1/SuperSimpleMario.PPM);

        // set game cam to be centered at the start
        gamecam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);

        // makes the Box2D world which has no gravity in the x axis but -10 in the y axis, it allows bodies in the world to sleep
        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        // creater to sort out repetitive needed creations of objects in the world
        creator = new B2WorldCreator(this);

        // creates a mario in the world
        player = new Mario(this);

        // world handles collisions through the WorldContactListener
        world.setContactListener(new WorldContactListener());

        // the theme music starts and loops
        music = SuperSimpleMario.manager.get("sounds/theme.mp3", Music.class);
        music.setLooping(true);
        music.play();

        // items (mushrooms) will be added to a queue and than added to be avaiable to come out of their boxes
        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingQueue<ItemDef>();

    }

    public void spawnItem(ItemDef idef)
    {
        itemsToSpawn.add(idef);
    }

    public void handleSpawningItems()
    {
        if(!itemsToSpawn.isEmpty())
        {
            ItemDef idef = itemsToSpawn.poll(); // like pop on a queue
            if(idef.type== Mushroom.class)
            {
                // create mushroom
                items.add(new Mushroom(this, idef.position.x, idef.position.y));
            }
        }
    }

    public TextureAtlas getAtlas()
    {
        return atlas;
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt)
    {
        //if((player.currentState!= Mario.State.DEAD) && (player.currentState!=Mario.State.CONTROLLED))
        if (!marioLocked)
        {
            if ((Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.justTouched()) && player.b2body.getLinearVelocity().y == 0)
            {
                player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
                SuperSimpleMario.manager.get("sounds/jump.wav", Sound.class).play();
            }

            if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.getAccelerometerY() > 0 ) && player.b2body.getLinearVelocity().x <= 2) // so mario has a limited speed
            {
                player.b2body.applyLinearImpulse(new Vector2(0.05f, 0f), player.b2body.getWorldCenter(), true);
            }

            if ((Gdx.input.isKeyPressed(Input.Keys.LEFT)|| Gdx.input.getAccelerometerY() < 0) && player.b2body.getLinearVelocity().x >= -2 && player.b2body.getPosition().x > 200/SuperSimpleMario.PPM)
            {
                player.b2body.applyLinearImpulse(new Vector2(-0.05f, 0f), player.b2body.getWorldCenter(), true);
            }
        }
    }

    public void update(float dt)
    {
        // check for user input
        handleInput(dt);
        handleSpawningItems();

        world.step(1/60f, 6, 2);

        player.update(dt); // updates mario


        // for enemies inside creator.getGoombas()
        for (Enemy enemy : creator.getEnemies())
        {
            enemy.update(dt);
            if (enemy.getX() < player.getX() + 224/SuperSimpleMario.PPM)
            {
                enemy.b2body.setActive(true);
            }
        }

        // for items in item
        for(Item item : items)
        {
            item.update(dt);
        }

        hud.update(dt);

        // attach gamecam to players x coordinate
        if(player.currentState != Mario.State.DEAD)
        {
            gamecam.position.x = player.b2body.getPosition().x;
        }

        // camera gets the new coordinates
        gamecam.update();

        // notify renderer to only draw what is in the game world
        renderer.setView(gamecam);


    }

    @Override
    public void render(float delta) {

        if (Mario.atCastle && !SuperSimpleMario.manager.get("sounds/level_complete.wav", Music.class).isPlaying()) // if mario is at the castle and the level complete music is finished playing
        {
            dispose(); // dispose of the current playscreen

            if (SuperSimpleMario.level != 3) // if all the levels are not completed,
            {
                game.setScreen(new LevelCompleteScreen(game)); // put up a new level complete screen (transition to the next level)
            }
            else // otherwise
            {
                game.setScreen(new AllDoneScreen((game))); // congratulate the user for completing and offer a replay
            }
            return; // don't bother going through the rest of the render function
        }

        // leave update logic seperated from render
        update(delta);
        // clears the screen (black)
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // render the game map
        renderer.render();

        // renderer the Box2DDebugLines
        b2dr.render(world, gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin(); // opens the box/application

        if (!Mario.atCastle) // as long as mario is not at the doorway of the end point castle,
        {
            player.draw(game.batch); // mario is drawn
        }

        for (Enemy enemy : creator.getEnemies()) // all the enemies are drawn
        {
            enemy.draw(game.batch);
        }
        for(Item item : items) // all the items are drawn
        {
            item.draw(game.batch);
        }

        game.batch.end(); // close the box

        // draw what the Hud camera sees
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        if(gameOver()) // if the user died
        {
            game.setScreen(new GameOverScreen(game)); // put up a new game over screen
            dispose(); // dispose of current screen
        }
    }

    public boolean gameOver()
    {
        if(player.currentState == Mario.State.DEAD && player.getStateTimer() > 3) // dead and 3 seconds has passed
        {
            return true; // game over screen can now pop up
        }
        return false; // otherwise, game over screen will have to wait
    }

    @Override
    public void resize(int width, int height)
    {
        gamePort.update(width, height); // adjust the game size (on screen)
    }

    public TiledMap getMap()
    {
        return map;
    } // offers public access to the map
    public World getWorld()
    {
        return world;
    } // offers public access to the world

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
        // dispose to avoid memory leaks
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();


    }
}

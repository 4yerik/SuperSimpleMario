package com.example.yeri.supersimplemario.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.example.yeri.supersimplemario.Screens.PlayScreen;
import com.example.yeri.supersimplemario.Sprites.Brick;
import com.example.yeri.supersimplemario.Sprites.Coin;
import com.example.yeri.supersimplemario.Sprites.Enemy;
import com.example.yeri.supersimplemario.Sprites.Goomba;
import com.example.yeri.supersimplemario.Sprites.Mario;
import com.example.yeri.supersimplemario.Sprites.Turtle;
import com.example.yeri.supersimplemario.SuperSimpleMario;

/**
 * Created by Yeri on 2016-12-27.
 */

public class B2WorldCreator {

    private Array <Goomba> goombas;
    private Array <Turtle> turtles;

    public B2WorldCreator(PlayScreen screen)
    {
        World world = screen.getWorld();
        TiledMap map= screen.getMap();

        BodyDef bdef = new BodyDef(); // MAY NEED TO CHANGE NAME
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef(); //MAY NEED TO CHANGE NAME
        Body body;

        // ground properties (bodies/fixtures)
        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) // pipe layer is 2 in Tile Map Maker
        {
            Rectangle rect = ((RectangleMapObject)object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX()+rect.getWidth()/2)/ SuperSimpleMario.PPM, (rect.getY() + rect.getHeight()/2)/SuperSimpleMario.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth()/2/SuperSimpleMario.PPM, rect.getHeight()/2/SuperSimpleMario.PPM);
            fdef.shape= shape;
            fdef.filter.categoryBits = SuperSimpleMario.GROUND_BIT;
            body.createFixture(fdef);
        }

        // pipe properties (bodies/fixtures)
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject)object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX()+rect.getWidth()/2)/SuperSimpleMario.PPM, (rect.getY() + rect.getHeight()/2)/SuperSimpleMario.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth()/2/SuperSimpleMario.PPM, rect.getHeight()/2/SuperSimpleMario.PPM);
            fdef.shape= shape;
            fdef.filter.categoryBits = SuperSimpleMario.OBJECT_BIT; // so the goomba turns around when he hits a pipe
            body.createFixture(fdef);
        }

        // coin properties
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class))
        {
            //Rectangle rect = ((RectangleMapObject)object).getRectangle();

            new Coin(screen, object);
        }

        // brick properties
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class))
        {
            //Rectangle rect = ((RectangleMapObject)object).getRectangle();

            new Brick(screen, object);
        }

        // create goombas
        goombas = new Array<Goomba>();
        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            goombas.add(new Goomba(screen, rect.getX()/SuperSimpleMario.PPM, rect.getY()/SuperSimpleMario.PPM));
        }

        // create turtles
        turtles = new Array<Turtle>();
        for (MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            turtles.add(new Turtle(screen, rect.getX()/SuperSimpleMario.PPM, rect.getY()/SuperSimpleMario.PPM));
        }


        // the end flag
        for (MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject)object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX()+rect.getWidth()/2)/SuperSimpleMario.PPM, (rect.getY() + rect.getHeight()/2)/SuperSimpleMario.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth()/2/SuperSimpleMario.PPM, rect.getHeight()/2/SuperSimpleMario.PPM);
            fdef.shape= shape;
            fdef.filter.categoryBits = SuperSimpleMario.FLAG_BIT; // it is classified as an flag bit, if mario touches, level is complete
            body.createFixture(fdef);
        }
    }

    public Array<Enemy> getEnemies()
    {
        Array<Enemy> enemies = new Array<Enemy>();
        enemies.addAll(goombas);
        enemies.addAll(turtles);

        return enemies;
    }
}

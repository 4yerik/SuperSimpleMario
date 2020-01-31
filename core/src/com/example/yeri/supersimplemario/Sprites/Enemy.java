package com.example.yeri.supersimplemario.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.example.yeri.supersimplemario.Screens.PlayScreen;

/**
 * Created by Yeri on 2016-12-29.
 */

public abstract class Enemy extends Sprite {

    protected World world;
    protected PlayScreen screen;
    public Body b2body;
    public Vector2 velocity;

    public Enemy(PlayScreen screen, float x, float y)
    {
        // what each enemy (children classes) will go through
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x,y); // dropped off at wherever it was initialized
        defineEnemy(); // defines properties that specialized to the type of enemy
        velocity = new Vector2(-1, -2); // x velocity - goes left, y velocity - more realistic moving look if they try to dig into the ground a little
        b2body.setActive(false); // body is frozen because it is not active
    }

    protected abstract void defineEnemy();
    public abstract void update(float dt);
    public abstract void hitOnHead(Mario mario);
    public abstract void onEnemyHit(Enemy enemy);

    public void reverseVelocity(boolean x, boolean y)
    {
        if (x)
        {
            velocity.x = -velocity.x; // if it was going left (negative x velocity), it goes right (positive x velocity) and vice verse

        }
        if (y)
        {
            velocity.y = -velocity.y; // positive y velocity means it's going up, negative y velocity means it's going down
        }
    }


}

package com.example.yeri.supersimplemario.Items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.example.yeri.supersimplemario.Screens.PlayScreen;
import com.example.yeri.supersimplemario.Sprites.Mario;
import com.example.yeri.supersimplemario.SuperSimpleMario;

/**
 * Created by Yeri on 2016-12-30.
 */

public abstract class Item extends Sprite{
    protected PlayScreen screen;
    protected World world;
    protected Vector2 velocity;
    protected boolean toDestroy;
    protected boolean destroyed;
    protected Body body;

    public Item(PlayScreen screen, float x, float y)
    {
        this.screen = screen; // saves the screen to use it
        this.world = screen.getWorld(); // retrieves the world and saves it to use
        setPosition(x, y); // the mushroom's "body" will be created in the location just above the coin box it came from
        setBounds(getX(), getY(), 16/ SuperSimpleMario.PPM, 16/SuperSimpleMario.PPM); // it's little area it exists in, 16 pixels by 16 pixels

        defineItem(); // whenever an item is created, it will be defined to the specifications of the function that overrides the abstract function

        toDestroy = false; // mushroom is not used up to begin with
        destroyed = false; // mushroom is not destroyed to begin with
    }
    public abstract void defineItem(); // each of the children class (in this case only one, mushroom) will need to define their properties
    public abstract void use(Mario mario); // and what happens when they come in contact with mario / "used" by Mario

    public void update(float dt)
    {
        if(toDestroy && !destroyed) // if the mushroom was eaten but was not destroyed yet
        {
            world.destroyBody(body); // ceases to exist in the world
            destroyed = true; // boolean that will allow program to know the mushroom has already been removed
        }
    }

    public void draw (Batch batch)
    {
        if (!destroyed)
        {
            super.draw(batch); // will continue to appear in the game as long as it is not used/destroyed
        }
    }

    public void destroy()
    {
        toDestroy = true;
    }

    public void reverseVelocity(boolean x, boolean y)
    {
        if (x) // reverse the x velocity by multiplying -1 to it
        {
            velocity.x = -velocity.x;

        }
        if (y) // reverse the y velocity by multiplying it by -1
        {
            velocity.y = -velocity.y;
        }
    }

}

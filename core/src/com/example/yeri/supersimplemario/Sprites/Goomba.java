package com.example.yeri.supersimplemario.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.example.yeri.supersimplemario.Screens.PlayScreen;
import com.example.yeri.supersimplemario.SuperSimpleMario;

/**
 * Created by Yeri on 2016-12-29.
 */

public class Goomba extends Enemy {

    private float stateTime;
    private Animation walkAnimation;
    private Array <TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;

    public Goomba(PlayScreen screen, float x, float y)
    {
        super(screen, x, y);
        frames = new Array<TextureRegion>(); // to store sprite animations

        // for the walking animation
        for (int i = 0; i<2; i++)
        {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i*16, 0, 16, 16));
        }
        walkAnimation = new Animation(0.4f, frames);
        stateTime = 0;

        setBounds(getX(), getY(), 16/SuperSimpleMario.PPM, 16/SuperSimpleMario.PPM); // the boundaries

        setToDestroy = false; // let the goomba live
        destroyed = false; // goomba is not destroyed when first created
    }

    public void update(float dt)
    {
        stateTime += dt;

        if (setToDestroy && !destroyed) // if it has been instructed to destroy and the goomba has not been destroyed yet
        {
            world.destroyBody(b2body); // banish it from the world
            destroyed= true; // goomba is destroyed
            setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"), 32, 0, 16, 16)); // the second picture in the goomba picture space
            stateTime = 0;
        }
        else if (!destroyed)
        {
            // if the goomba is still living
            b2body.setLinearVelocity(velocity); // moves according to the velocity
            setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight()/2); // position
            setRegion((TextureRegion) walkAnimation.getKeyFrame(stateTime, true));
        }

    }

    @Override
    protected void defineEnemy()
    {

        // makes a new body definition and set's it location and type
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/SuperSimpleMario.PPM);

        // what the body of the goomba is (category) and what it collides with (mask)
        fdef.filter.categoryBits = SuperSimpleMario.ENEMY_BIT;
        //fdef.filter.maskBits = SuperSimpleMario.GROUND_BIT | SuperSimpleMario.COIN_BIT | SuperSimpleMario.BRICK_BIT |SuperSimpleMario.ENEMY_BIT | SuperSimpleMario.OBJECT_BIT | SuperSimpleMario.MARIO_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        // make the head of the goomba
        PolygonShape head = new PolygonShape();
        Vector2[] vertex = new Vector2[4];
        vertex[0] = new Vector2(-5, 8).scl(1/SuperSimpleMario.PPM);
        vertex[1] = new Vector2(5, 8).scl(1/SuperSimpleMario.PPM);
        vertex[2] = new Vector2(-5, 3).scl(1/SuperSimpleMario.PPM);
        vertex[3] = new Vector2(5, 3).scl(1/SuperSimpleMario.PPM);

        head.set(vertex);

        fdef.shape = head;
        fdef.restitution = 0.5f; // has a bounce to it
        fdef.filter.categoryBits = SuperSimpleMario.ENEMY_HEAD_BIT; // set the head shape and organize it as an enemy_head_bit
        fdef.filter.maskBits = SuperSimpleMario.MARIO_FEET_BIT; // can collide with mario's feet
        b2body.createFixture(fdef).setUserData(this); // saves the data to the object's body in the world
    }

    public void onEnemyHit(Enemy enemy)
    {
        if (enemy instanceof Turtle && ((Turtle) enemy).currentState == Turtle.State.MOVING_SHELL)
        {
            setToDestroy = true; // if a goomba collides with a moving shell, it dies
        }
        else
        {
            reverseVelocity(true, false); // goomba reverses its x velocity if the shell is not moving
        }
    }

    public void draw (Batch batch)
    {
        if (!destroyed || stateTime < 0.5)
        {
            super.draw(batch); // only drawn if not destroyed or one second has not passed
        }
    }
    @Override
    public void hitOnHead(Mario mario)
    {
        // what happens when goomba is stomped on by mario
        setToDestroy = true; // will be commanded to be destroyed
        SuperSimpleMario.manager.get("sounds/stomp.wav", Sound.class).play(); // squished goomba sound effect
    }
}

package com.example.yeri.supersimplemario.Sprites;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.example.yeri.supersimplemario.Scenes.Hud;
import com.example.yeri.supersimplemario.Screens.PlayScreen;
import com.example.yeri.supersimplemario.SuperSimpleMario;

/**
 * Created by Yeri on 2016-12-27.
 */

public class Mario extends Sprite {


    public enum State {FALLING, JUMPING, STANDING, RUNNING, GROWING, SHRINKING, DEAD, SLIDING, CONTROLLED}; // states mario can be in
    public State currentState;
    public State previousState;

    public World world; // lives here
    public Body b2body; // is mario

    // for mario's appearance
    private TextureRegion marioStand;
    private Animation marioRun;
    private Animation marioSlide;
    private TextureRegion marioJump;
    private TextureRegion marioDead;
    private TextureRegion bigMarioStand;
    private TextureRegion bigMarioJump;
    private Animation bigMarioRun;
    private Animation bigMarioSlide;
    private Animation growMario;
    private Animation shrinkMario;

    // how long mario is in a certain state for (ex. 2.3 seconds where mario is dead)
    private float stateTimer;

    // boolean variables for mario
    private boolean runningRight;
    private boolean marioIsBig;
    private boolean runGrowAnimation;
    private boolean runShrinkAnimation;
    private boolean timeToDefineBigMario;
    private boolean timeToRedefineMario;
    private boolean slidDown;

    // boolean mario variables for all to access
    public static boolean levelComplete;
    public static boolean atCastle;
    public static boolean marioIsDead;

    public Mario (PlayScreen screen)
    {
        this.world=screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        // run animation - little mario
        for (int i = 1; i<4; i++)
        {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"), i*16, 0, 16, 16)); // CHANGE 0 TO 11???
        }
        marioRun = new Animation(0.1f, frames);
        frames.clear();
        // run animation - big mario
        for (int i = 1; i<4; i++)
        {
            // CHANGE 0 TO 11???
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), i*16, 0, 16, 32));
        }
        bigMarioRun = new Animation(0.1f, frames);
        frames.clear(); // so the next animation sequence can run

        // slide - little mario
        for (int i = 7; i<9; i++)
        {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"), i*16, 0, 16, 16));
        }
        marioSlide = new Animation(0.1f, frames);
        frames.clear();
        // slide - big mario
        for (int i = 7; i<9; i++)
        {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), i*16, 0, 16, 32));
        }
        bigMarioSlide = new Animation(0.1f, frames);
        frames.clear();

        // animation frames for when mario eats a mushroom ////////// change into for loop if you want but has a repeat so....
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        growMario = new Animation(0.2f, frames);
        frames.clear();

        // animation frames for when mario is downgrades
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        shrinkMario = new Animation(0.2f, frames);
        frames.clear();

        // jump animation
        marioJump = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 80, 0, 16, 16);
        bigMarioJump = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 80, 0, 16, 32);

        // mario standing texture region
        marioStand = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 1, 0, 16, 16);
        bigMarioStand = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32);

        // mario dead (little)
        marioDead = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 96, 0, 16, 16);

        // set Mario in the 2d box
        defineMario();

        // mario location and starts standing
        setBounds(0,0,16/SuperSimpleMario.PPM, 16/SuperSimpleMario.PPM);
        setRegion(marioStand);
    }

    public void update(float dt)
    {

        // if mario is living AND there is no time left or he has fallen into a pit
        if ((b2body.getPosition().y <0 || Hud.noTime) && !marioIsDead)
        {
            mariofuneral();
        }

        else
        {
            if (levelComplete && b2body.getPosition().y <= 64/SuperSimpleMario.PPM && !atCastle) // hopefully 64 is the number when his feet touch the block
            {
                if (b2body.getPosition().x < 3300/SuperSimpleMario.PPM) // keep running until mario gets to the castle doors
                {
                    slidDown = true;
                    b2body.applyLinearImpulse(new Vector2(0.1f, 0), b2body.getWorldCenter(), true);
                }
                else
                {
                    Hud.totalAdder(); // add to the culminating score and time

                    world.destroyBody(b2body); // secretly get rid of mario
                    atCastle = true; // mario is at castle

                }
            }

            // make sprite alter itself based on position of body
            if(marioIsBig || (getState() == State.SHRINKING))
            {
                setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight()/2 - 6/SuperSimpleMario.PPM);
            }
            else
            {
                setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight()/2);
            }

            // make sprite reflect what mario is doing based on key controls
            if (!atCastle)
            {
                setRegion(getFrame(dt));

                // check if any new fixture definitions are needed because mario has gone through a change in size
                if(timeToDefineBigMario)
                {
                    defineBigMario();
                }
                if(timeToRedefineMario)
                {
                    redefineMario();
                }
            }
        }
    }

    public TextureRegion getFrame(float dt)
    {
        currentState = getState();

        TextureRegion region;

        switch (currentState)
        {
            case DEAD:
                region = marioDead;
                break;

            case GROWING:
                region = (TextureRegion)growMario.getKeyFrame(stateTimer);
                if(growMario.isAnimationFinished(stateTimer))
                {
                    runGrowAnimation = false; // when it is finished this state is not true
                }
                break;

            case SHRINKING:
                region = (TextureRegion)shrinkMario.getKeyFrame(stateTimer);

                if (shrinkMario.isAnimationFinished(stateTimer))
                {
                    runShrinkAnimation = false;
                    timeToRedefineMario = true;
                }
                break;

            case JUMPING:
                region = marioIsBig? bigMarioJump : marioJump; // is mario big? than set his appearance to the big mario jump texture region, otherwise just the little mario jump texture region
                break;

            case RUNNING:
            case CONTROLLED:
                region = marioIsBig? (TextureRegion) bigMarioRun.getKeyFrame(stateTimer,true) : (TextureRegion) marioRun.getKeyFrame(stateTimer,true);
                break;

            case SLIDING:
                region = marioIsBig? (TextureRegion) bigMarioSlide.getKeyFrame(stateTimer, true) : (TextureRegion) marioSlide.getKeyFrame(stateTimer, true);
                break;

            case FALLING:
            case STANDING:
            default:
                region = marioIsBig? bigMarioStand : marioStand;
                break;
        }

        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX())
        {
            region.flip(true, false);
            runningRight = false;
        }
        else if ((b2body.getLinearVelocity().x>0 || runningRight) && region.isFlipX())
        {
            region.flip(true, false);
            runningRight = true;
        }

        // set state timer, does the current state equal the previous state - add dt else (:) it equals 0, new state
        stateTimer = currentState == previousState? stateTimer + dt : 0;
        previousState = currentState;

        return region;
    }

    public State getState() // a function that determines what state mario is in
    {
        if (levelComplete && !slidDown) // mario must still be sliding down the flagpole
        {
            return State.SLIDING;
        }
        else if (marioIsDead) // mario must've died
        {
            return State.DEAD;
        }
        else if (levelComplete && slidDown) // mario is running to the castle, once he reaches the castle he will die
        {
            return State.CONTROLLED;
        }
        else if (runShrinkAnimation) // mario is shrinking
        {
            return State.SHRINKING;
        }
        else if(runGrowAnimation) // mario must be growing
        {
            return State.GROWING;
        }
        else if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)) // mario must be jumping into the air or coming down from a jump
        {
            return State.JUMPING;
        }
        else if(b2body.getLinearVelocity().y < 0) // mario must be falling
        {
            return State.FALLING;
        }
        else if(b2body.getLinearVelocity().x != 0) // mario must be running
        {
            return State.RUNNING;
        }
        else // default is mario is standing, if he is not in these special scenarios
        {
            return State.STANDING;
        }
    }

    public void grow()
    {
        if (marioIsBig == false) // if mario can still grow
        {
            runGrowAnimation=true;
            marioIsBig = true;
            timeToDefineBigMario = true;
            SuperSimpleMario.manager.get("sounds/powerup.wav", Sound.class).play();
        }
        else // if he is already grown up
        {
            Hud.addScore(500);
            SuperSimpleMario.manager.get("sounds/powerup.wav", Sound.class).play();
        }

    }

    public boolean isDead()
    {
        return marioIsDead;
    }
    public float getStateTimer()
    {
        return stateTimer;
    }

    public void defineBigMario()
    {
        Vector2 currentPosition = b2body.getPosition();
        world.destroyBody(b2body); // destroy the small mario

        // the existence of mario in the world
        BodyDef bdef = new BodyDef();
        bdef.position.set(currentPosition.add(0, 10/SuperSimpleMario.PPM));
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        // mario's body
        FixtureDef fdef = new FixtureDef();
        CircleShape body = new CircleShape();
        body.setRadius(6/SuperSimpleMario.PPM); // circle with a radius of 6 units

        setBits(fdef); // so collisions for the body can be determined

        fdef.shape = body;
        b2body.createFixture(fdef).setUserData(this);

        body.setPosition(new Vector2(0, -14/SuperSimpleMario.PPM));
        b2body.createFixture(fdef).setUserData(this);

        // mario's head
        EdgeShape head = new EdgeShape(); // a line
        head.set(new Vector2(-2/SuperSimpleMario.PPM, 6/ SuperSimpleMario.PPM), new Vector2(2/SuperSimpleMario.PPM, 6/ SuperSimpleMario.PPM));
        fdef.filter.categoryBits = SuperSimpleMario.MARIO_HEAD_BIT; // is Mario's hairline ... or should I say hat line
        fdef.shape = head;
        fdef.isSensor = true; // collects collision information but does not have a response such as a restution

        b2body.createFixture(fdef).setUserData(this); // user to mario himself

        // Mario's feet
        EdgeShape feet = new EdgeShape();
        feet.set (new Vector2(-5/SuperSimpleMario.PPM, -22/SuperSimpleMario.PPM), new Vector2(5/SuperSimpleMario.PPM, -22/SuperSimpleMario.PPM));

        fdef.shape = feet;
        fdef.filter.categoryBits = SuperSimpleMario.MARIO_FEET_BIT;
        fdef.filter.maskBits = SuperSimpleMario.ENEMY_HEAD_BIT;

        b2body.createFixture(fdef).setUserData(this); // user to mario himself

        timeToDefineBigMario = false; // now big mario is defined

        setBounds(getX(), getY(), getWidth(), getHeight() * 2); // the boundaries of the image now have twice the vertical space
    }

    public void defineMario()
    {
        BodyDef bdef = new BodyDef();
        bdef.position.set(200/ SuperSimpleMario.PPM,32/SuperSimpleMario.PPM); // starting position of mario
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape body = new CircleShape();
        body.setRadius(6/SuperSimpleMario.PPM);

        setBits(fdef);

        fdef.shape = body;
        b2body.createFixture(fdef).setUserData(this);

        // mario's head
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2/SuperSimpleMario.PPM, 6/ SuperSimpleMario.PPM), new Vector2(2/SuperSimpleMario.PPM, 6/ SuperSimpleMario.PPM));
        fdef.filter.categoryBits = SuperSimpleMario.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);

        // Mario's feet
        EdgeShape feet = new EdgeShape();
        feet.set (new Vector2(-5/SuperSimpleMario.PPM, -8/SuperSimpleMario.PPM), new Vector2(5/SuperSimpleMario.PPM, -8/SuperSimpleMario.PPM));

        fdef.shape = feet;
        fdef.filter.categoryBits = SuperSimpleMario.MARIO_FEET_BIT;
        fdef.filter.maskBits = SuperSimpleMario.ENEMY_HEAD_BIT;

        b2body.createFixture(fdef).setUserData(this); // user to mario himself
    }

    public void redefineMario()
    {
        Vector2 position = b2body.getPosition();

        // destroy big mario
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape body = new CircleShape();
        body.setRadius(6/SuperSimpleMario.PPM);

        setBits(fdef);

        fdef.shape = body;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2/SuperSimpleMario.PPM, 6/ SuperSimpleMario.PPM), new Vector2(2/SuperSimpleMario.PPM, 6/ SuperSimpleMario.PPM));
        fdef.filter.categoryBits = SuperSimpleMario.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        fdef.shape = head;

        b2body.createFixture(fdef).setUserData(this); // user to mario himself

        // Mario's feet
        EdgeShape feet = new EdgeShape();
        feet.set (new Vector2(-5/SuperSimpleMario.PPM, -8/SuperSimpleMario.PPM), new Vector2(5/SuperSimpleMario.PPM, -8/SuperSimpleMario.PPM));

        fdef.shape = feet;
        fdef.filter.categoryBits = SuperSimpleMario.MARIO_FEET_BIT;
        fdef.filter.maskBits = SuperSimpleMario.ENEMY_HEAD_BIT;

        b2body.createFixture(fdef).setUserData(this); // user to mario himself

        setBounds(getX(), getY(), getWidth(), getHeight() /2); // have mario be stre
        timeToRedefineMario = false;
    }

    public boolean isBig()
    {
        return marioIsBig;
    }

    public void hit(Enemy enemy)
    {
        if (enemy instanceof  Turtle && ((Turtle) enemy).getCurrentState() == Turtle.State.STANDING_SHELL) // if the turtle was a harmless turtle shell
        {
            ((Turtle) enemy).kick(this.getX() <= enemy.getX() ? Turtle.KICK_RIGHT_SPEED : Turtle.KICK_LEFT_SPEED); // if mario collided with the left half of the shell, it will go right and vice versa
            SuperSimpleMario.manager.get("sounds/kick.wav", Sound.class).play(); // the shell is kicked
        }

        else if (getState() != State.SHRINKING)
        {
            if (marioIsBig)
            {
                SuperSimpleMario.manager.get("sounds/powerdown.wav", Sound.class).play();

                runShrinkAnimation = true;
                marioIsBig = false;

            }
            else
            {
                mariofuneral();
            }
        }
    }

    private void setBits(FixtureDef fdef)
    {
        // what mario is and what he can collide with
        fdef.filter.categoryBits = SuperSimpleMario.MARIO_BIT;
        fdef.filter.maskBits = SuperSimpleMario.GROUND_BIT | SuperSimpleMario.COIN_BIT | SuperSimpleMario.BRICK_BIT
                | SuperSimpleMario.ENEMY_BIT | SuperSimpleMario.OBJECT_BIT | SuperSimpleMario.ENEMY_HEAD_BIT
                | SuperSimpleMario.ITEM_BIT | SuperSimpleMario.FLAG_BIT;

    }

    public void levelComplete()
    {
        levelComplete = true;

        SuperSimpleMario.manager.get("sounds/theme.mp3", Music.class).stop();
        SuperSimpleMario.manager.get("sounds/flagpole.wav", Sound.class).play(); // will start just before he slides down

        Filter filterCastle = new Filter();
        filterCastle.categoryBits = SuperSimpleMario.MARIO_BIT;
        filterCastle.maskBits = SuperSimpleMario.GROUND_BIT;

        //mario only collides with the ground now
        for (Fixture fixture:b2body.getFixtureList()) {
            fixture.setFilterData(filterCastle);
        }

        // the level complete music does not repeat
        SuperSimpleMario.manager.get("sounds/level_complete.wav", Music.class).play();
        SuperSimpleMario.manager.get("sounds/level_complete.wav", Music.class).setLooping(false);
    }

    private void mariofuneral() /// things that happan when mario dies
    {
        SuperSimpleMario.manager.get("sounds/theme.mp3", Music.class).stop(); // theme song stops playing
        SuperSimpleMario.manager.get("sounds/mario_die.wav", Sound.class).play(); // game over sound plays

        marioIsDead = true;
        PlayScreen.marioLocked = true;
        Filter filter = new Filter();
        filter.maskBits = SuperSimpleMario.NOTHING_BIT; // fixtures in the world will soon have this mask bit
        // for every fixture, nothing can collide with anything in the 2d world
        for (Fixture fixture:b2body.getFixtureList())
        {
            fixture.setFilterData(filter);
        }

        b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true); // go 4 upwards (impulse) in center of his mass, true/false doesn't matter in this case because mario does not sleep
    }


}

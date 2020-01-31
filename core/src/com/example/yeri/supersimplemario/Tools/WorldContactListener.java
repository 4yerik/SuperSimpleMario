package com.example.yeri.supersimplemario.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.example.yeri.supersimplemario.Items.Item;
import com.example.yeri.supersimplemario.Screens.PlayScreen;
import com.example.yeri.supersimplemario.Sprites.Enemy;
import com.example.yeri.supersimplemario.Sprites.InteractiveTileObject;
import com.example.yeri.supersimplemario.Sprites.Mario;
import com.example.yeri.supersimplemario.SuperSimpleMario;
import com.example.yeri.supersimplemario.Sprites.Goomba;


/**
 * Created by Yeri on 2016-12-27.
 */

public class WorldContactListener implements ContactListener
{
    @Override
    public void beginContact(Contact contact)
    {
        Fixture fixA = contact.getFixtureA(); // what one of the contact objects were
        Fixture fixB = contact.getFixtureB(); // what it collided with

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits; // the type of object it collides with

        switch (cDef)
        {
            // | means with
            case SuperSimpleMario.MARIO_HEAD_BIT | SuperSimpleMario.BRICK_BIT:
            case SuperSimpleMario.MARIO_HEAD_BIT | SuperSimpleMario.COIN_BIT:
                if (fixA.getFilterData().categoryBits == SuperSimpleMario.MARIO_HEAD_BIT)
                {
                    ((InteractiveTileObject)fixB.getUserData()).onHeadHit((Mario)fixA.getUserData());
                }
                else
                {
                    ((InteractiveTileObject)fixA.getUserData()).onHeadHit((Mario)fixB.getUserData());
                }
                break;
            case SuperSimpleMario.ENEMY_HEAD_BIT | SuperSimpleMario.MARIO_FEET_BIT:
                Gdx.app.log("mario", "murdered");
                if (fixA.getFilterData().categoryBits == SuperSimpleMario.ENEMY_HEAD_BIT)
                {
                    ((Enemy)fixA.getUserData()).hitOnHead((Mario)fixB.getUserData());
                }
                else
                {
                    ((Enemy)fixB.getUserData()).hitOnHead((Mario)fixA.getUserData());
                }
                break;

            case SuperSimpleMario.ENEMY_BIT | SuperSimpleMario.OBJECT_BIT:
            //case SuperSimpleMario.ENEMY_HEAD_BIT | SuperSimpleMario.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == SuperSimpleMario.OBJECT_BIT)
                {
                    ((Enemy)fixB.getUserData()).reverseVelocity(true, false);
                }
                else
                {
                    ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                }
                break;

            case SuperSimpleMario.MARIO_BIT | SuperSimpleMario.ENEMY_BIT:
                if (fixA.getFilterData().categoryBits == SuperSimpleMario.MARIO_BIT)
                {
                    ((Mario)fixA.getUserData()).hit((Enemy)fixB.getUserData());
                }
                else
                {
                    ((Mario)fixB.getUserData()).hit((Enemy)fixA.getUserData());
                }
                break;

            //case SuperSimpleMario.ENEMY_HEAD_BIT | SuperSimpleMario.ENEMY_HEAD_BIT:
            case SuperSimpleMario.ENEMY_BIT | SuperSimpleMario.ENEMY_BIT:
                ((Enemy)fixA.getUserData()).onEnemyHit((Enemy)fixB.getUserData());
                ((Enemy)fixB.getUserData()).onEnemyHit((Enemy)fixA.getUserData());
                break;

            case SuperSimpleMario.ITEM_BIT | SuperSimpleMario.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == SuperSimpleMario.ITEM_BIT)
                {
                    ((Item)fixA.getUserData()).reverseVelocity(true, false); // item isnt showing up as anything!!!!
                }
                else
                {
                    ((Item)fixB.getUserData()).reverseVelocity(true, false);
                }
                break;

            case SuperSimpleMario.ITEM_BIT | SuperSimpleMario.MARIO_BIT:
                if (fixA.getFilterData().categoryBits == SuperSimpleMario.ITEM_BIT) // is fixture A the item used on on Mario(b)
                {
                    ((Item)fixA.getUserData()).use((Mario) fixB.getUserData());
                }
                else // fixture B is the item and use that on mario
                {
                    ((Item)fixB.getUserData()).use((Mario) fixA.getUserData());
                }
                break;

            case SuperSimpleMario.MARIO_BIT | SuperSimpleMario.FLAG_BIT:
                if (fixA.getFilterData().categoryBits == SuperSimpleMario.MARIO_BIT) // to take action on mario, we have to find which fixture is mario
                {
                    ((Mario)fixA.getUserData()).levelComplete();
                    PlayScreen.marioLocked = true;
                }
                else
                {
                    ((Mario)fixB.getUserData()).levelComplete();
                    PlayScreen.marioLocked = true;
                }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}

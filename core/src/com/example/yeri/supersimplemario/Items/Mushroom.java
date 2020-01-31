package com.example.yeri.supersimplemario.Items;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.example.yeri.supersimplemario.Screens.PlayScreen;
import com.example.yeri.supersimplemario.Sprites.Mario;
import com.example.yeri.supersimplemario.SuperSimpleMario;

/**
 * Created by Yeri on 2016-12-30.
 */

public class Mushroom extends Item {
    public Mushroom(PlayScreen screen, float x, float y) {
        super(screen, x, y); //
        setRegion(screen.getAtlas().findRegion("mushroom"), 0, 0, 16, 16); // looks for mushroom properties in the tiled map
        velocity = new Vector2(0.7f,0); // the mushroom has a velocity of 0.7f (70cm/second in the world)
    }

    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/ SuperSimpleMario.PPM);
        fdef.filter.categoryBits = SuperSimpleMario.ITEM_BIT; // the object is classified as an "ITEM_BIT"
        fdef.filter.maskBits = SuperSimpleMario.MARIO_BIT | SuperSimpleMario.OBJECT_BIT | SuperSimpleMario.GROUND_BIT | SuperSimpleMario.COIN_BIT | SuperSimpleMario.BRICK_BIT; // can collide with these 5 objects

        fdef.shape = shape; // associates the fixtures to the circle shape
        body.createFixture(fdef).setUserData(this); // makes the mushroom
    }

    @Override
    public void use(Mario mario) {
        destroy(); // calls the destroy method that is included in it's parent class
        mario.grow(); // calls the grow method on the mario class, which will make mario appear to have grown
    }

    @Override
    public void update(float dt) {
        super.update(dt); // it will be updated in the super class
        setPosition(body.getPosition().x-getWidth()/2, body.getPosition().y - getHeight()/2); // the position will be reflected
        velocity.y = body.getLinearVelocity().y; // not changing the y velocity
        body.setLinearVelocity(velocity); // makes the body follow the linear velocity
    }
}

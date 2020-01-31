package com.example.yeri.supersimplemario.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.example.yeri.supersimplemario.Scenes.Hud;
import com.example.yeri.supersimplemario.Screens.PlayScreen;
import com.example.yeri.supersimplemario.SuperSimpleMario;

/**
 * Created by Yeri on 2016-12-27.
 */

public class Brick extends InteractiveTileObject {
    public Brick(PlayScreen screen, MapObject object)
    {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(SuperSimpleMario.BRICK_BIT); // when a brick object is created, it's category filter is set to a brick_bit so others know what it is during collision
    }

    @Override
    public void onHeadHit(Mario mario)
    {
        if (mario.isBig()) // the brick can only be smashed if mario is big
        {
            setCategoryFilter(SuperSimpleMario.DESTROYED_BIT); // sets the brick to destroyed which disables it's collision with mario
            getCell().setTile(null); // removes the graphics so it looks like the background
            Hud.addScore(10); // adds 10 to the score
            SuperSimpleMario.manager.get("sounds/block_smash.wav", Sound.class).play(); // sound effect
        }
        else
        {
            SuperSimpleMario.manager.get("sounds/bump.wav", Sound.class).play(); // to notify user they did hit the brick but can not break it because they are currently small mario
        }

    }
}

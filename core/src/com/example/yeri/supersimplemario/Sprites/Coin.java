package com.example.yeri.supersimplemario.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import com.example.yeri.supersimplemario.Items.ItemDef;
import com.example.yeri.supersimplemario.Items.Mushroom;
import com.example.yeri.supersimplemario.Scenes.Hud;
import com.example.yeri.supersimplemario.Screens.PlayScreen;
import com.example.yeri.supersimplemario.SuperSimpleMario;

/**
 * Created by Yeri on 2016-12-27.
 */

public class Coin extends InteractiveTileObject{
    private static TiledMapTileSet tileSet;
    //lib gdx starts count at one
    private final int BLANK_COIN = 28; // the ID of the blank coin image in the tile map resource is 27, so add one to find what libgdx knows it as

    public Coin(PlayScreen screen, MapObject object)
    {
        super(screen, object); // passes in the screen and the coin object
        tileSet = map.getTileSets().getTileSet("tileset_gutter"); // gets the resource of all the 16 by 16 images that are used to make the level tiled maps
        fixture.setUserData(this); // saves the data of the tileset
        setCategoryFilter(SuperSimpleMario.COIN_BIT); // classifies the object as a coin_bit
    }

    @Override
    public void onHeadHit(Mario mario) {
        Gdx.app.log("Coin", "Collision");

        if (getCell().getTile().getId() == BLANK_COIN) // if the coin box was already used
        {
            SuperSimpleMario.manager.get("sounds/bump.wav", Sound.class).play(); // sound effect letting the user know there is nothing to get
        }
        else // if the coin box is still available
        {
            if (object.getProperties().containsKey("mushroom")) // checks if it has a mushroom
            {
                screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16/SuperSimpleMario.PPM), Mushroom.class)); // make a new mushroom put out just above the box
                SuperSimpleMario.manager.get("sounds/powerup_available.wav", Sound.class).play(); // sound effect
            }
            else // otherwise mario gets a coin
            {
                SuperSimpleMario.manager.get("sounds/coin.wav", Sound.class).play(); // coin sound effect
                Hud.addScore(250); // 250 is added to the score
            }
        }
        getCell().setTile(tileSet.getTile(BLANK_COIN)); // the coin box is now used
    }
}

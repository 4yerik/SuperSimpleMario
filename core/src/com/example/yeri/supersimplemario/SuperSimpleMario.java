package com.example.yeri.supersimplemario;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.example.yeri.supersimplemario.Screens.PlayScreen;


// Yeri Kim
// December 2016 - January 2017

// A simplified mario game where mario can move up, left and right in order to reach the castle
// Mario must avoid enemies, Goomba and Koopa (turtle)
// He can collect points by smashing bricks and knocking coin boxes and even grow if he eats a mushroom

public class SuperSimpleMario extends Game {
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;
	public static final float PPM = 100;

	public static int level = 1;
	public static int totalScore = 0;
	public static int totalTime = 0;

	// shorts in power of 2 make it easier for processor to compare
	public static final short NOTHING_BIT = 0;
	public static final short GROUND_BIT = 1;
	public static final short MARIO_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short COIN_BIT = 8;
	public static final short DESTROYED_BIT = 16;
	public static final short OBJECT_BIT = 32;
	public static final short ENEMY_BIT = 64;
	public static final short ENEMY_HEAD_BIT=128;
	public static final short ITEM_BIT = 256;
	public static final short MARIO_HEAD_BIT = 512;
	public static final short MARIO_FEET_BIT= 1024;
	public static final short FLAG_BIT = 2048;

	public SpriteBatch batch; // used to draw objects

	public static AssetManager manager; // manager for music

	@Override
	public void create () {
		batch = new SpriteBatch();

		// make a manager and load in all the sounds in it
		manager = new AssetManager();
		manager.load("sounds/theme.mp3", Music.class);
		manager.load("sounds/level_complete.wav", Music.class);

		manager.load("sounds/coin.wav", Sound.class);
		manager.load("sounds/bump.wav", Sound.class);
		manager.load("sounds/block_smash.wav", Sound.class);
		manager.load("sounds/powerup.wav", Sound.class);
		manager.load("sounds/powerup_available.wav", Sound.class);
		manager.load("sounds/powerdown.wav", Sound.class);
		manager.load("sounds/stomp.wav", Sound.class);
		manager.load("sounds/mario_die.wav", Sound.class);
		manager.load("sounds/kick.wav", Sound.class);
		manager.load("sounds/jump.wav", Sound.class);
		manager.load("sounds/flagpole.wav", Sound.class);
		manager.finishLoading();

		setScreen(new PlayScreen(this)); // sends game information to playscreen



	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		manager.dispose();
	}
}

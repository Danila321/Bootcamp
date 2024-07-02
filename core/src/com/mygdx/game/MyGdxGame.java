package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.utility.GameSettings;

import javax.swing.Box;

public class MyGdxGame extends Game {

	public World world;

	public Vector3 touch;
	public SpriteBatch batch;
	public OrthographicCamera camera;

	public GameScreen gameScreen;

	@Override
	public void create() {
		Box2D.init();
		world = new World(new Vector2(0, 0), false);

		camera = new OrthographicCamera();
		camera.setToOrtho(true, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);
		batch = new SpriteBatch();

		gameScreen = new GameScreen(this);
		setScreen(gameScreen);
	}
}
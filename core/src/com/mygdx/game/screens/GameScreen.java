package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.GameField;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Tile;
import com.mygdx.game.utility.GameResourses;

import java.util.ArrayList;
import java.util.Random;

public class GameScreen extends ScreenAdapter {
    MyGdxGame myGdxGame;
    GameField gameField;

    public GameScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        gameField = new GameField(16, 9);

    }

    @Override
    public void show() {
        restartGame();

    }

    @Override
    public void render(float delta) {
        draw();

    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        }
    }

    private void draw() {
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        myGdxGame.batch.begin();
//        myGdxGame.batch.draw("back.png", 0, 0, 1280, 720);
        myGdxGame.batch.end();
    }

    private void restartGame() {

    }
}
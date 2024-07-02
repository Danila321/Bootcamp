package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.gamefield.GameField;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.ui.ImageView;
import com.mygdx.game.utility.GameResourses;

public class GameScreen extends ScreenAdapter {
    MyGdxGame myGdxGame;
    GameField gameField;
    ImageView bg;

    public GameScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        gameField = new GameField(16, 9);
        bg = new ImageView(0, 0, GameResourses.BACK);
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
        bg.draw(myGdxGame.batch);
        myGdxGame.batch.end();
    }

    private void restartGame() {

    }
}
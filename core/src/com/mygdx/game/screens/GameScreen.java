package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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
    TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledMapRenderer;


    public GameScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        gameField = new GameField(16, 9);
        bg = new ImageView(0, 0, GameResourses.BACK);
    }

    @Override
    public void show() {
        restartGame();

    }

    public void loadMap() {
        TmxMapLoader mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load("mapYesYes.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 2f);
    }

    @Override
    public void render(float delta) {
        draw();
        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            //myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            Vector2 touckPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            if (hasObjectCoordinates("tower", touckPos)) {
                System.out.println("click");
            }
        }
    }

    private boolean hasObjectCoordinates(String tower, Vector2 touchPos) {
        MapObjects objects = tiledMap.getLayers().get(tower).getObjects();
        if (objects != null) {
            for (RectangleMapObject object : objects.getByType(RectangleMapObject.class)) {
                if (object.getRectangle().contains(touchPos.x, 720 - touchPos.y)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void draw() {
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        myGdxGame.batch.begin();
        //bg.draw(myGdxGame.batch);
        myGdxGame.batch.end();
    }

    private void restartGame() {

    }
}
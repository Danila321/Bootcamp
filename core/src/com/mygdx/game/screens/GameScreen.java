package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Money;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.ui.TextView;
import com.mygdx.game.utility.GameSettings;
import com.mygdx.game.BaseTowerObject;
import com.mygdx.game.utility.GameResources;
import java.util.ArrayList;

public class GameScreen extends ScreenAdapter {
    MyGdxGame myGdxGame;
    //GameField gameField;
    //ImageView bg;
    Money balance;
    TextView balanceTextView;
    TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    ArrayList<BaseTowerObject> TowerArrray;
    float x_cord=0, y_cord=0;

    public GameScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        TowerArrray = new ArrayList<>();
        loadMap();

        balanceTextView = new TextView(myGdxGame.commonWhiteFont, 50, 100, "dfdf");

        balance = new Money(2000);
        //gameField = new GameField(16, 9);
        //bg = new ImageView(0, 0, GameResourses.BACK);
    }

    @Override
    public void show() {
        restartGame();

    }

    public void loadMap() {
        TmxMapLoader mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load("mapYesYes.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, GameSettings.MAP_SCALE);
    }
    private void draw() {

        for (BaseTowerObject tower : TowerArrray) tower.draw(myGdxGame.batch);
    }

    @Override
    public void render(float delta) {
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);
        myGdxGame.batch.begin();

        balanceTextView.setText(String.valueOf("567567"));

        tiledMapRenderer.setView(myGdxGame.camera);
        tiledMapRenderer.render();

        draw();

        handleInput();
        myGdxGame.batch.end();

    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            if (hasObjectCoordinates("tower", touchPos)) {
                //System.out.println("click");
                System.out.println(Gdx.input.getX());
                System.out.println(x_cord);
                BaseTowerObject baseTower = new BaseTowerObject(Gdx.input.getX(), Gdx.input.getY(),
                        33, 33, GameResources.red_square, myGdxGame.world);
                TowerArrray.add(baseTower);
            }
        }
    }

    private boolean hasObjectCoordinates(String tower, Vector2 touchPos) {
        MapObjects objects = tiledMap.getLayers().get(tower).getObjects();
        if (objects != null) {
            for (RectangleMapObject object : objects.getByType(RectangleMapObject.class)) {
                if (object.getRectangle().contains(touchPos.x / GameSettings.MAP_SCALE, touchPos.y / GameSettings.MAP_SCALE)) {
                    System.out.println(object.getRectangle().x);
                    x_cord = object.getRectangle().x / GameSettings.MAP_SCALE;
                    y_cord = object.getRectangle().y / GameSettings.MAP_SCALE;
                    return true;
                }
            }
        }
        return false;
    }


    private void restartGame() {

    }
}
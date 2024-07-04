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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.ui.ButtonView;
import com.mygdx.game.ui.ImageView;
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
    ButtonView button1, button2, button3, closeButton;
    ImageView unitMenu, tower1, tower2, tower3;
    TextView balanceTextView;
    TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    ArrayList<BaseTowerObject> TowerArrray;
    float x_cord=0, y_cord=0;
    boolean isMenuExecuted = false;

    public GameScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        TowerArrray = new ArrayList<>();
        loadMap();

        button1 = new ButtonView(1070, 100, 200, 50, myGdxGame.commonWhiteFont, GameResources.BUTTON, "500");
        button2 = new ButtonView(1070, 200, 200, 50, myGdxGame.commonWhiteFont, GameResources.BUTTON, "600");
        button3 = new ButtonView(1070, 300, 200, 50, myGdxGame.commonWhiteFont, GameResources.BUTTON, "700");
        closeButton = new ButtonView(1240, 20, 20, 20, GameResources.red_square);

        tower1 = new ImageView(1100, 50, GameResources.red_square, 50, 50);
        tower2 = new ImageView(1100, 150, GameResources.red_square, 50, 50);
        tower3 = new ImageView(1100, 250, GameResources.red_square, 50, 50);

        balanceTextView = new TextView(myGdxGame.commonWhiteFont, 50, 100, "dfdf");

        balance = new Money(2000);
        unitMenu = new ImageView(1050, 0, GameResources.WHITE, 1000, 1000);

    }

    @Override
    public void show() {
        restartGame();

    }

    public void loadMap() {
        TmxMapLoader mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load("mapq.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, GameSettings.MAP_SCALE);
    }
    private void draw() {
        for (BaseTowerObject tower : TowerArrray) tower.draw(myGdxGame.batch);

        if (isMenuExecuted){
            unitMenu.draw(myGdxGame.batch);
            button1.draw(myGdxGame.batch);
            button2.draw(myGdxGame.batch);
            button3.draw(myGdxGame.batch);
            tower1.draw(myGdxGame.batch);
            tower2.draw(myGdxGame.batch);
            tower3.draw(myGdxGame.batch);
            closeButton.draw(myGdxGame.batch);
        }

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

        handleInput();
        draw();

        myGdxGame.batch.end();

    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            if (hasObjectCoordinates("tower", touchPos)) {
                isMenuExecuted = true;

                //System.out.println("click");
                System.out.println(Gdx.input.getX());
                System.out.println(x_cord);
                BaseTowerObject baseTower = new BaseTowerObject(Gdx.input.getX(), Gdx.input.getY(),
                        33, 33, GameResources.red_square, myGdxGame.world);
                TowerArrray.add(baseTower);
            }
            if (button1.isHit(touchPos.x, touchPos.y)){
                System.out.println("500");
            }
            if (button2.isHit(touchPos.x, touchPos.y)){
                System.out.println("600");
            }
            if (button3.isHit(touchPos.x, touchPos.y)){
                System.out.println("700");
            }
            if (closeButton.isHit(touchPos.x, touchPos.y)){
                isMenuExecuted = false;
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
    @Override
    public void dispose(){
        unitMenu.dispose();
    }
}
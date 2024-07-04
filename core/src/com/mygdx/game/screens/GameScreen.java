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
import com.mygdx.game.utility.Path;
import com.mygdx.game.objects.EnemyObject;
import com.mygdx.game.ui.Money;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.ui.ButtonView;
import com.mygdx.game.ui.ImageView;
import com.mygdx.game.ui.TextView;
import com.mygdx.game.utility.GameSettings;
import com.mygdx.game.objects.BaseTowerObject;
import com.mygdx.game.utility.GameResources;
import java.util.ArrayList;

public class GameScreen extends ScreenAdapter {
    MyGdxGame myGdxGame;
    //GameField gameField;
    //ImageView bg;
    Money balance;
    ButtonView button1, button2, button3, closeButton;
    ImageView unitMenu, tower1, tower2, tower3;
    TextView balanceTextView, balanceRedTextView;
    TiledMap tiledMap;
    Path path;
    Vector2 startPos;
    ArrayList<BaseTowerObject> TowerArray;
    float x_cord = 0, y_cord = 0;
    float mapScale;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    boolean isMenuExecuted = false;
    private EnemyObject enemy;

    public GameScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        TowerArray = new ArrayList<>();
        loadMap();
        path = new Path(tiledMap);
        MapObjects objects = tiledMap.getLayers().get("enemy").getObjects();
        if (objects != null) {
            for (RectangleMapObject object : objects.getByType(RectangleMapObject.class)) {
                startPos = new Vector2(object.getRectangle().x, object.getRectangle().y);
                break;
            }
        }
        enemy = new EnemyObject("red.png", myGdxGame.world, path,
                (int)startPos.x, (int)startPos.y);




        button1 = new ButtonView(1070, 100, 200, 50, myGdxGame.commonWhiteFont,
                GameResources.BUTTON, "500");
        button2 = new ButtonView(1070, 200, 200, 50, myGdxGame.commonWhiteFont,
                GameResources.BUTTON, "600");
        button3 = new ButtonView(1070, 300, 200, 50, myGdxGame.commonWhiteFont,
                GameResources.BUTTON, "700");
        closeButton = new ButtonView(1240, 20, 20, 20, GameResources.red_square);

        tower1 = new ImageView(1100, 50, GameResources.yellow_square, 50, 50);
        tower2 = new ImageView(1100, 150, GameResources.green_square, 50, 50);
        tower3 = new ImageView(1100, 250, GameResources.blue_square, 50, 50);

        balanceTextView = new TextView(myGdxGame.commonWhiteFont, 150, 50, "dfdf");
        balanceRedTextView = new TextView(myGdxGame.commonRedFont, 150, 50, "dfdf");

        balance = new Money(10000);
        unitMenu = new ImageView(1050, 0, GameResources.WHITE, 1000, 1000);

    }

    @Override
    public void show() {
        restartGame();

    }

    public void loadMap() {
        TmxMapLoader mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load("mapq (2).tmx");
//        MapProperties properties = tiledMap.getProperties();
//        int mapHeight = properties.get("height", Integer.class);
//        int mapPixelHeight = properties.get("tileHeight", Integer.class);
//
//        mapScale = (float) GameSettings.SCREEN_HEIGHT / (mapHeight * mapPixelHeight);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, GameSettings.MAP_SCALE);
    }
    private void draw() {
        for (BaseTowerObject tower : TowerArray) tower.draw(myGdxGame.batch);
        if (haveMoney()) {
            balanceTextView.draw(myGdxGame.batch);
        }
        else {
            balanceRedTextView.draw((myGdxGame.batch));
        }


        for (BaseTowerObject tower : TowerArray) tower.draw(myGdxGame.batch);
        if (isMenuExecuted){
            drawMenu();
        }
    }

    @Override
    public void render(float delta) {
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);
        myGdxGame.batch.begin();

        balanceTextView.setText(String.valueOf("money:" + balance.getBalance()));
        balanceRedTextView.setText(String.valueOf("money:" + balance.getBalance()));

        tiledMapRenderer.setView(myGdxGame.camera);
        tiledMapRenderer.render();



        enemy.update(delta);
        enemy.draw(myGdxGame.batch);

        draw();
        handleInput();

        myGdxGame.batch.end();

    }
    private boolean haveMoney() {
        return balance.getBalance() > 500;
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            if (isMenuExecuted && button1.isHit(touchPos.x, touchPos.y)
                    && balance.getBalance() >= GameSettings.TOWER1_COST){
                balance.reduceBalance(GameSettings.TOWER1_COST);
                BaseTowerObject baseTower = new BaseTowerObject(
                        x_cord, y_cord,
                        (int) (32 * GameSettings.MAP_SCALE),
                        (int) (32 * GameSettings.MAP_SCALE),
                        GameResources.yellow_square, myGdxGame.world);
                TowerArray.add(baseTower);
                System.out.println("SPAWNED!!!!!");
                isMenuExecuted = false;
            }
            if (isMenuExecuted && button2.isHit(touchPos.x, touchPos.y)
                    && balance.getBalance() >= GameSettings.TOWER2_COST){
                balance.reduceBalance(GameSettings.TOWER2_COST);
                BaseTowerObject baseTower2 = new BaseTowerObject(
                        x_cord, y_cord,
                        (int) (32 * GameSettings.MAP_SCALE),
                        (int) (32 * GameSettings.MAP_SCALE),
                        GameResources.green_square, myGdxGame.world);
                TowerArray.add(baseTower2);
                System.out.println("SPAWNED!!!!!");
                isMenuExecuted = false;
            }
            if (isMenuExecuted && button3.isHit(touchPos.x, touchPos.y)
                    && balance.getBalance() >= GameSettings.TOWER3_COST){
                balance.reduceBalance(GameSettings.TOWER3_COST);
                BaseTowerObject baseTower3 = new BaseTowerObject(
                        x_cord, y_cord,
                        (int) (32 * GameSettings.MAP_SCALE),
                        (int) (32 * GameSettings.MAP_SCALE),
                        GameResources.blue_square, myGdxGame.world);
                TowerArray.add(baseTower3);
                System.out.println("SPAWNED!!!!!");
                isMenuExecuted = false;
            }
            if (hasObjectCoordinates("tower", touchPos) && !isMenuExecuted) {
                    if (tileIsEmpty((int)x_cord, (int)y_cord) && (x_cord != -1 && y_cord != -1)) {
                        isMenuExecuted = true;


                    }

            }

            if (closeButton.isHit(touchPos.x, touchPos.y)){
                isMenuExecuted = false;
            }
        }
    }

    private boolean tileIsEmpty(int x, int y) {
        for(BaseTowerObject tower : TowerArray) {
            float xTower  = tower.getX();
            float yTower  = tower.getY();
            if (xTower== x && yTower == y) {
                return false;
            }
        }
        return true;
    }

    private boolean hasObjectCoordinates(String tower, Vector2 touchPos) {
        MapObjects objects = tiledMap.getLayers().get(tower).getObjects();
        if (objects != null) {
            for (RectangleMapObject object : objects.getByType(RectangleMapObject.class)) {
                if (object.getRectangle().contains(touchPos.x / GameSettings.MAP_SCALE,
                        touchPos.y / GameSettings.MAP_SCALE)) {
                    System.out.println(object.getRectangle().x);
                    x_cord = (object.getRectangle().x  + object.getRectangle().width / 2)
                            * GameSettings.MAP_SCALE;
                    y_cord = (object.getRectangle().y + object.getRectangle().height / 2)
                            * GameSettings.MAP_SCALE;
                    return true;
                }
            }
        }
        return false;
    }

    public void drawMenu() {
        unitMenu.draw(myGdxGame.batch);
        button1.draw(myGdxGame.batch);
        button2.draw(myGdxGame.batch);
        button3.draw(myGdxGame.batch);
        tower1.draw(myGdxGame.batch);
        tower2.draw(myGdxGame.batch);
        tower3.draw(myGdxGame.batch);
        closeButton.draw(myGdxGame.batch);
    }


    private void restartGame() {

    }
    @Override
    public void dispose(){
        unitMenu.dispose();
        tiledMap.dispose();
    }
}
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
import com.mygdx.game.ui.ImageView;
import com.mygdx.game.ui.TextView;
import com.mygdx.game.utility.GameSettings;
import com.mygdx.game.objects.BaseTowerObject;
import com.mygdx.game.utility.GameResources;
import java.util.ArrayList;

public class GameScreen extends ScreenAdapter {
    MyGdxGame myGdxGame;
    Money balance;
    ImageView unitMenu;
    TextView balanceTextView;
    TiledMap tiledMap;
    Path path;
    Vector2 startPos;
    ArrayList<BaseTowerObject> TowerArray;
    float x_cord = 0, y_cord = 0;
    float mapScale;
    boolean isMenuExecuted = false;
    private EnemyObject enemy;
    private OrthogonalTiledMapRenderer tiledMapRenderer;

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




        balanceTextView = new TextView(myGdxGame.commonWhiteFont, 50, 100, "dfdf");

        balance = new Money(2000);
        unitMenu = new ImageView(750, 0, GameResources.WHITE);

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
//        if (isMenuExecuted){
//            unitMenu.draw(myGdxGame.batch);
//        }
        for (BaseTowerObject tower : TowerArray) tower.draw(myGdxGame.batch);
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

        enemy.update(delta);
        enemy.draw(myGdxGame.batch);

        handleInput();
        myGdxGame.batch.end();

    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            if (hasObjectCoordinates("tower", touchPos)) {
                isMenuExecuted = true;

                //System.out.println("click");
                //System.out.println(Gdx.input.getX());
                //System.out.println(x_cord);
                    if (tileIsEmpty((int)x_cord, (int)y_cord) && (x_cord != -1 && y_cord != -1)) {
                        BaseTowerObject baseTower = new BaseTowerObject(
                                x_cord, y_cord,
                                (int) (32 * GameSettings.MAP_SCALE),
                                (int) (32 * GameSettings.MAP_SCALE),
                                GameResources.red_square, myGdxGame.world);
                        TowerArray.add(baseTower);
                        System.out.println("SPAWNED!!!!!");
                    }
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
                if (object.getRectangle().contains(touchPos.x / GameSettings.MAP_SCALE, touchPos.y / GameSettings.MAP_SCALE)) {
                    System.out.println(object.getRectangle().x);
                    x_cord = (object.getRectangle().x  + object.getRectangle().width / 2) * GameSettings.MAP_SCALE;
                    y_cord = (object.getRectangle().y + object.getRectangle().height / 2) * GameSettings.MAP_SCALE;
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
        tiledMap.dispose();
    }
}
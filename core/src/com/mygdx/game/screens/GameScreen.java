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
import com.mygdx.game.ContactManager;
import com.mygdx.game.objects.BulletObject;
import com.mygdx.game.utility.GameSession;
import com.mygdx.game.utility.GameState;
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
import java.util.Iterator;

public class GameScreen extends ScreenAdapter {
    MyGdxGame myGdxGame;
    GameSession gameSession;
    ContactManager contactManager;
    Money balance;
    ButtonView button1, button2, button3, closeButton;
    ImageView unitMenu, tower1, tower2, tower3;
    TextView balanceTextView, balanceRedTextView;
    TextView levelTextView;
    TiledMap tiledMap;
    Path path;
    Vector2 startPos;
    ArrayList<BaseTowerObject> towerArray;
    ArrayList<EnemyObject> enemyArray;
    float x_cord = 0, y_cord = 0;
    float mapScale;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    boolean isMenuExecuted = false;
    private EnemyObject enemy;
    ButtonView pauseButton;

    ImageView fullBlackoutView;
    TextView pauseTextView;
    ButtonView homeButton;
    ButtonView continueButton;

    public GameScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        gameSession = new GameSession();
        gameSession.state = GameState.PLAYING;
        contactManager = new ContactManager(myGdxGame.world);

        towerArray = new ArrayList<>();
        enemyArray = new ArrayList<>();
        loadMap();
        path = new Path(tiledMap);
        MapObjects objects = tiledMap.getLayers().get("enemy").getObjects();
        if (objects != null) {
            for (RectangleMapObject object : objects.getByType(RectangleMapObject.class)) {
                startPos = new Vector2(object.getRectangle().x, object.getRectangle().y);
                break;
            }
        }

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

        balanceTextView = new TextView(myGdxGame.commonWhiteFont, 150, 50, "");
        balanceRedTextView = new TextView(myGdxGame.commonRedFont, 150, 50, "");

        levelTextView = new TextView(myGdxGame.commonWhiteFont, 1000, 50, "");

        balance = new Money(10000);
        unitMenu = new ImageView(1050, 0, GameResources.WHITE, 1000, 1000);

        pauseButton = new ButtonView(
                1200, 50,
                46, 54,
                GameResources.PAUSE_IMG_PATH
        );

        fullBlackoutView = new ImageView(0, 0, GameResources.BLACKOUT);
        pauseTextView = new TextView(myGdxGame.largeWhiteFont, 550, 200, "Pause");
        homeButton = new ButtonView(
                550, 300,
                200, 70,
                myGdxGame.commonBlackFont,
                "button_white.png",
                "Home"
        );
        continueButton = new ButtonView(
                550, 450,
                200, 70,
                myGdxGame.commonBlackFont,
                "button_white.png",
                "Continue"
        );

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
        for (BaseTowerObject tower : towerArray) {
            tower.draw(myGdxGame.batch);
        }
        if (haveMoney()) {
            balanceTextView.draw(myGdxGame.batch);
        } else {
            balanceRedTextView.draw(myGdxGame.batch);
        }

        levelTextView.draw(myGdxGame.batch);

        for (EnemyObject enemy : enemyArray) {
            enemy.update(2);
            enemy.draw(myGdxGame.batch);
        }

        if (isMenuExecuted) {
            drawMenu();
        }

        pauseButton.draw(myGdxGame.batch);

        if (gameSession.state == GameState.PAUSED) {
            fullBlackoutView.draw(myGdxGame.batch);
            pauseTextView.draw(myGdxGame.batch);
            homeButton.draw(myGdxGame.batch);
            continueButton.draw(myGdxGame.batch);
        }
    }

    @Override
    public void render(float delta) {
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);
        myGdxGame.batch.begin();

        if (gameSession.state == GameState.PLAYING) {
            for (BaseTowerObject tower : towerArray) {
                tower.shoot(enemyArray);
                tower.updateBullets();
                tower.putInBox();
            }

            balanceTextView.setText("Money:" + balance.getBalance());
            balanceRedTextView.setText("Money:" + balance.getBalance());
            balanceTextView.setText("Money: " + balance.getBalance());
            balanceRedTextView.setText("Money: " + balance.getBalance());

            levelTextView.setText("Wave: " + gameSession.getLevel());

            if (!gameSession.isRest()) {
                if (gameSession.shouldSpawnEnemy()) {
                    EnemyObject enemy = new EnemyObject("red.png", myGdxGame.world, path,
                            (int) startPos.x, (int) startPos.y, GameSettings.ENEMY_SPEED);
                    enemyArray.add(enemy);
                }
            }

            updateEnemy();

            myGdxGame.stepWorld();
        }

        tiledMapRenderer.setView(myGdxGame.camera);
        tiledMapRenderer.render();

        draw();
        handleInput();

        myGdxGame.batch.end();
    }

    private boolean haveMoney() {
        return balance.getBalance() > 0;
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());

            switch (gameSession.state) {
                case PLAYING:
                    if (pauseButton.isHit(touchPos.x, touchPos.y)) {
                        gameSession.pauseGame();
                    }
                    if (isMenuExecuted && button1.isHit(touchPos.x, touchPos.y)
                            && balance.getBalance() >= GameSettings.TOWER1_COST) {
                        balance.reduceBalance(GameSettings.TOWER1_COST);
                        BaseTowerObject baseTower = new BaseTowerObject(
                                x_cord, y_cord,
                                (int) (32 * GameSettings.MAP_SCALE),
                                (int) (32 * GameSettings.MAP_SCALE),
                                GameResources.yellow_square, myGdxGame.world);
                        towerArray.add(baseTower);
                        isMenuExecuted = false;
                    }
                    if (isMenuExecuted && button2.isHit(touchPos.x, touchPos.y)
                            && balance.getBalance() >= GameSettings.TOWER2_COST) {
                        balance.reduceBalance(GameSettings.TOWER2_COST);
                        BaseTowerObject baseTower2 = new BaseTowerObject(
                                x_cord, y_cord,
                                (int) (32 * GameSettings.MAP_SCALE),
                                (int) (32 * GameSettings.MAP_SCALE),
                                GameResources.green_square, myGdxGame.world);
                        towerArray.add(baseTower2);
                        isMenuExecuted = false;
                    }
                    if (isMenuExecuted && button3.isHit(touchPos.x, touchPos.y)
                            && balance.getBalance() >= GameSettings.TOWER3_COST) {
                        balance.reduceBalance(GameSettings.TOWER3_COST);
                        BaseTowerObject baseTower3 = new BaseTowerObject(
                                x_cord, y_cord,
                                (int) (32 * GameSettings.MAP_SCALE),
                                (int) (32 * GameSettings.MAP_SCALE),
                                GameResources.blue_square, myGdxGame.world);
                        towerArray.add(baseTower3);
                        isMenuExecuted = false;
                    }
                    if (hasObjectCoordinates("tower", touchPos) && !isMenuExecuted) {
                        if (tileIsEmpty((int) x_cord, (int) y_cord) && (x_cord != -1 && y_cord != -1)) {
                            isMenuExecuted = true;
                        }
                    }
                    if (closeButton.isHit(touchPos.x, touchPos.y)) {
                        isMenuExecuted = false;
                    }
                    break;

                case PAUSED:
                    if (continueButton.isHit(touchPos.x, touchPos.y)) {
                        gameSession.resumeGame();
                    }
                    if (homeButton.isHit(touchPos.x, touchPos.y)) {
                        myGdxGame.setScreen(myGdxGame.menuScreen);
                    }
                    break;
            }
        }
    }

    private boolean tileIsEmpty(int x, int y) {
        for (BaseTowerObject tower : towerArray) {
            float xTower = tower.getX();
            float yTower = tower.getY();
            if (xTower == x && yTower == y) {
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
                    x_cord = (object.getRectangle().x + object.getRectangle().width / 2)
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

    private void updateEnemy() {
        Iterator<EnemyObject> enemyObjectIterator = enemyArray.iterator();

        while (enemyObjectIterator.hasNext()) {

            EnemyObject nextEnemy = enemyObjectIterator.next();
            if (!nextEnemy.isAlive()) {
                gameSession.eliminationRegistration(balance);
                myGdxGame.world.destroyBody(nextEnemy.body);
                enemyObjectIterator.remove();
            }
        }

    }

    private void restartGame() {

    }

    @Override
    public void dispose() {
        unitMenu.dispose();
        tiledMap.dispose();
    }
}
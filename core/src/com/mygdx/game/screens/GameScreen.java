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
import com.mygdx.game.utility.ContactManager;
import com.mygdx.game.objects.MainHeroObject;
import com.mygdx.game.utility.GameSession;
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
    ImageView unitMenu, tower1, tower2, tower3, liveImageView;
    TextView balanceTextView, balanceRedTextView, levelTextView, livesTextView;
    TiledMap tiledMap;
    Path path;
    Vector2 startPos;
    MainHeroObject hero;
    ArrayList<BaseTowerObject> towerArray;
    ArrayList<EnemyObject> enemyArray;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    boolean isMenuExecuted = false;
    float x_cord = 0, y_cord = 0;

    public GameScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        gameSession = new GameSession();
        contactManager = new ContactManager(myGdxGame.world);

        towerArray = new ArrayList<>();
        enemyArray = new ArrayList<>();
        hero = new MainHeroObject(1230, 490, 100, 100,
                "blue.png", myGdxGame.world);
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
        closeButton = new ButtonView(1240, 20, 30, 30, GameResources.close_button);

        tower1 = new ImageView(1100, 50, GameResources.yellow_square, 50, 50);
        tower2 = new ImageView(1100, 150, GameResources.green_square, 50, 50);
        tower3 = new ImageView(1100, 250, GameResources.blue_square, 50, 50);

        liveImageView = new ImageView(170, 77, GameResources.LIVE, -22, 25);

        balanceTextView = new TextView(myGdxGame.commonWhiteFont, 1075, 40);
        balanceRedTextView = new TextView(myGdxGame.commonRedFont, 1075, 40);

        levelTextView = new TextView(myGdxGame.commonWhiteFont, 170, 40);
        livesTextView = new TextView(myGdxGame.commonWhiteFont, 200, 75);

        balance = new Money(1000);
        unitMenu = new ImageView(1050, 0, GameResources.BLACK, 1000, 1000);

    }

    @Override
    public void show() {
        restartGame();
    }

    public void loadMap() {
        TmxMapLoader mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load("mapq (2).tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, GameSettings.MAP_SCALE);
    }

    private void draw() {
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        myGdxGame.batch.begin();

        balanceTextView.setText(String.valueOf("money:" + balance.getBalance()));
        balanceRedTextView.setText(String.valueOf("money:" + balance.getBalance()));
        balanceTextView.setText("Money: " + balance.getBalance());
        balanceRedTextView.setText("Money: " + balance.getBalance());

        levelTextView.setText("Wave: " + gameSession.getLevel());
        livesTextView.setText("Lives: " + hero.getLiveLeft());



        tiledMapRenderer.setView(myGdxGame.camera);
        tiledMapRenderer.render();


        levelTextView.draw(myGdxGame.batch);
        livesTextView.draw(myGdxGame.batch);


        for (EnemyObject enemy : enemyArray) {
            enemy.update(2);
            enemy.draw(myGdxGame.batch);
            if (enemy.needToHit()) {
                hero.hit(enemy.maxHealth);
            }
            TextView HPLeft = new TextView(myGdxGame.smallRedFont,
                    enemy.getX() * GameSettings.MAP_SCALE,
                    enemy.getY() * GameSettings.MAP_SCALE,
                    enemy.getLiveLeft() + " / " + enemy.maxHealth);
            HPLeft.draw(myGdxGame.batch);
        }
        liveImageView.draw(myGdxGame.batch);

        for (BaseTowerObject tower : towerArray) {
            tower.draw(myGdxGame.batch);
            tower.shoot(enemyArray);
            tower.updateBullets();
            tower.putInBox();
        }

        hero.draw(myGdxGame.batch);

        if (isMenuExecuted) {
            drawMenu();
        }

        if (haveMoney()) {
            balanceTextView.draw(myGdxGame.batch);
        } else {
            balanceRedTextView.draw(myGdxGame.batch);
        }

        myGdxGame.batch.end();
    }

    @Override
    public void render(float delta) {

        handleInput();
        update();

        if (!gameSession.isRest()) {
            if (gameSession.shouldSpawnEnemy()) {
                EnemyObject enemy = new EnemyObject("robot1.png", myGdxGame.world, path,
                        (int) startPos.x, (int) startPos.y, 64, 64, GameSettings.ENEMY_SPEED);
                enemyArray.add(enemy);
            }
        }

        myGdxGame.stepWorld();

        draw();
    }


    private boolean haveMoney() {
        return balance.getBalance() > 500;
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
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

    private void update() {
        Iterator<EnemyObject> enemyObjectIterator = enemyArray.iterator();

        while (enemyObjectIterator.hasNext()) {

            EnemyObject nextEnemy = enemyObjectIterator.next();
            if (!nextEnemy.isAlive()) {
                gameSession.eliminationRegistration(balance);
                myGdxGame.world.destroyBody(nextEnemy.body);
                enemyObjectIterator.remove();
                System.out.println("DELETED");
            }
        }

        if (!hero.isAlive()) {
            System.out.println("DEAD!");
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
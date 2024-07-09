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
import com.mygdx.game.Managers.ContactManager;
import com.mygdx.game.Managers.MemoryManager;
import com.mygdx.game.objects.MainHeroObject;
import com.mygdx.game.utility.GameSession;
import com.mygdx.game.Managers.AudioManager;
import com.mygdx.game.utility.GameState;
import com.mygdx.game.utility.Path;
import com.mygdx.game.objects.EnemyObject;
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
    AudioManager audioManager;
    
    TiledMap tiledMap;
    Path path;
    Vector2 startPos;
    MainHeroObject hero;
    ArrayList<BaseTowerObject> towerArray;
    ArrayList<EnemyObject> enemyArray;
    BaseTowerObject baseTower;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    boolean isMenuExecuted = false;
    boolean isUpgradeMenuExecuted = false;
    float x_cord = 0, y_cord = 0;

    //Play UI
    ButtonView button1, button2, button3, closeButton, pauseButton, sellButton, upgradeButton;
    ImageView unitMenu, tower1, tower2, tower3, liveImageView;
    TextView balanceTextView, balanceRedTextView, livesTextView, levelTextView;

    //Paused UI
    ImageView fullBlackoutView;
    TextView pauseTextView;
    ButtonView homeButton, continueButton;

    //Ended UI
    TextView resultTextView;
    ButtonView homeButton2;

    public GameScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        gameSession = new GameSession();

        contactManager = new ContactManager(myGdxGame.world);
        audioManager = new AudioManager();

        towerArray = new ArrayList<>();
        enemyArray = new ArrayList<>();
        hero = new MainHeroObject(1230, 490, 100, 100,
                GameResources.blue_square, myGdxGame.world);

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
        sellButton = new ButtonView(1070, 300, 200, 50, myGdxGame.commonWhiteFont,
                GameResources.BUTTON, "SELL");
        upgradeButton = new ButtonView(1070, 200, 200, 50, myGdxGame.commonWhiteFont,
                GameResources.BUTTON, "UPGRADE");
        closeButton = new ButtonView(1240, 20, 20, 20, GameResources.red_square);

        tower1 = new ImageView(1100, 50, GameResources.yellow_square, 50, 50);
        tower2 = new ImageView(1100, 150, GameResources.green_square, 50, 50);
        tower3 = new ImageView(1100, 250, GameResources.blue_square, 50, 50);

        balanceTextView = new TextView(myGdxGame.commonWhiteFont, 1075, 40);
        balanceRedTextView = new TextView(myGdxGame.commonRedFont, 1075, 40);
        notificationTextView = new TextView(myGdxGame.largeRedFont, 380, 60);

        liveImageView = new ImageView(170, 77, GameResources.red_square, -22, 25);
        levelTextView = new TextView(myGdxGame.commonWhiteFont, 170, 40);
        livesTextView = new TextView(myGdxGame.commonWhiteFont, 200, 75);

        unitMenu = new ImageView(1050, 0, GameResources.WHITE, 1000, 1000);

        pauseButton = new ButtonView(
                30, 20,
                92, 92,
                GameResources.red_square
        );
        fullBlackoutView = new ImageView(0, 0, GameResources.BLACKOUT);
        pauseTextView = new TextView(myGdxGame.largeWhiteFont, 560, 200, "Pause");
        homeButton = new ButtonView(
                550, 300,
                200, 70,
                myGdxGame.commonBlackFont,
                GameResources.WHITE_BUTTON,
                "Home"
        );
        continueButton = new ButtonView(
                550, 450,
                200, 70,
                myGdxGame.commonBlackFont,
                GameResources.WHITE_BUTTON,
                "Continue"
        );

        resultTextView = new TextView(myGdxGame.largeWhiteFont, 520, 300, "Game over!");
        homeButton2 = new ButtonView(
                550, 400,
                160, 70,
                myGdxGame.commonBlackFont,
                GameResources.WHITE_BUTTON,
                "Home"
        );
    }

    @Override
    public void show() {
        restartGame();
    }

    public void loadMap() {
        TmxMapLoader mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load("tilemap/mapq (2).tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, GameSettings.MAP_SCALE);
    }

    private void draw() {
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(MyGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        myGdxGame.batch.begin();

        notificationTextView.setText("ENEMY HAS PASSED!!!");
        balanceTextView.setText("Money: " + gameSession.getBalance());
        balanceRedTextView.setText("Money: " + gameSession.getBalance());

        levelTextView.setText("Wave: " + gameSession.getLevel());

        liveImageView.draw(myGdxGame.batch);
        livesTextView.setText("Lives: " + hero.getLiveLeft());

        tiledMapRenderer.setView(myGdxGame.camera);


        tiledMapRenderer.setView(MyGdxGame.camera);
        tiledMapRenderer.render();

        levelTextView.draw(myGdxGame.batch);
        livesTextView.draw(myGdxGame.batch);

        for (EnemyObject enemy : enemyArray) {
            if (!(gameSession.state == GameState.PAUSED || gameSession.state == GameState.ENDED)) {
                enemy.update(2);
            }
            enemy.draw(myGdxGame.batch);
            if (enemy.needToHit()) {
                hero.hit(enemy.getMaxHealth());
            }
            TextView HPLeft = new TextView(myGdxGame.smallRedFont,
                    enemy.getX() * GameSettings.MAP_SCALE,
                    enemy.getY() * GameSettings.MAP_SCALE,
                    enemy.getLiveLeft() + " / " + enemy.getMaxHealth());
            HPLeft.draw(myGdxGame.batch);
        }

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
        if (isUpgradeMenuExecuted) {
            drawMenuUpgrade();
        }

        if (gameSession.getBalance() > 0) {
            balanceTextView.draw(myGdxGame.batch);
        } else {
            balanceRedTextView.draw(myGdxGame.batch);
        }

        pauseButton.draw(myGdxGame.batch);

        if (gameSession.state == GameState.PAUSED) {
            fullBlackoutView.draw(myGdxGame.batch);
            pauseTextView.draw(myGdxGame.batch);
            homeButton.draw(myGdxGame.batch);
            continueButton.draw(myGdxGame.batch);
        } else if (gameSession.state == GameState.ENDED) {
            fullBlackoutView.draw(myGdxGame.batch);
            resultTextView.draw(myGdxGame.batch);
            homeButton2.draw(myGdxGame.batch);
        }

        hero.notifyCheck();
        if (MainHeroObject.needToNotify) {
            notificationTextView.draw(myGdxGame.batch);
        }

        myGdxGame.batch.end();
    }

    @Override
    public void render(float delta) {

        if (gameSession.state == GameState.PLAYING) {
            update();

            if (!gameSession.isRest()) {
                if (gameSession.shouldSpawnEnemy()) {
                    EnemyObject enemy = new EnemyObject("images/robot1.png", myGdxGame.world, path,
                            (int) startPos.x, (int) startPos.y, 32, 32,
                            GameSettings.ENEMY_SPEED, 5);
                    enemyArray.add(enemy);
                }
                if (gameSession.shouldSpawnEnemy2() && gameSession.getLevel() % 3 == 0) {
                    EnemyObject enemy2 = new EnemyObject("images/blue.png",
                            myGdxGame.world, path, (int) startPos.x, (int) startPos.y,
                            64, 64, GameSettings.ENEMY2_SPEED, 8);
                    enemyArray.add(enemy2);
                }
                if (gameSession.shouldSpawnEnemy3() && gameSession.getLevel() % 10 == 0) {
                    EnemyObject enemy3 = new EnemyObject("images/red.png",
                            myGdxGame.world, path, (int) startPos.x, (int) startPos.y,
                            96, 96, GameSettings.ENEMY3_SPEED, 20);
                    enemyArray.add(enemy3);
                }

            }

            if (!hero.isAlive()) {
                gameSession.endGame();
            }

            myGdxGame.stepWorld();
        }

        draw();
        handleInput();
    }

    public int levelCost(float tx, float ty) {
        for (BaseTowerObject towerObject : towerArray) {
            if (tx >= towerObject.getX() - 16 && tx <= towerObject.getX() + 16 && ty >= towerObject.getY() - 16 && ty <= towerObject.getY() + 16) {
                return towerObject.levelNumber * 300;
            }
        }
        return 300;
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
                    if (isUpgradeMenuExecuted && upgradeButton.isHit(touchPos.x, touchPos.y) && gameSession.getBalance() >= levelCost(touchPos.x, touchPos.y)){
                        gameSession.reduceBalance(levelCost(x_cord, y_cord));
                        isUpgradeMenuExecuted = false;
                    }
                    if (isUpgradeMenuExecuted && sellButton.isHit(touchPos.x, touchPos.y) && gameSession.getBalance() >= levelCost(touchPos.x, touchPos.y)){
                        isUpgradeMenuExecuted = false;
                    }
                    if (isUpgradeMenuExecuted && closeButton.isHit(touchPos.x, touchPos.y) && gameSession.getBalance() >= levelCost(touchPos.x, touchPos.y)){
                        isUpgradeMenuExecuted = false;
                    }
                    if (isMenuExecuted && button1.isHit(touchPos.x, touchPos.y)
                            && gameSession.getBalance() >= GameSettings.TOWER1_COST) {
                        gameSession.reduceBalance(GameSettings.TOWER1_COST);
                        BaseTowerObject baseTower = new BaseTowerObject(
                                x_cord, y_cord,
                                (int) (32 * GameSettings.MAP_SCALE),
                                (int) (32 * GameSettings.MAP_SCALE),
                                GameResources.yellow_square, myGdxGame.world, GameSettings.TOWER1_DAMAGE);
                        towerArray.add(baseTower);
                        audioManager.towerCreateSound.play(0.6f * MemoryManager.SoundValue());
                        isMenuExecuted = false;
                    }
                    if (isMenuExecuted && button2.isHit(touchPos.x, touchPos.y)
                            && gameSession.getBalance() >= GameSettings.TOWER2_COST) {
                        gameSession.reduceBalance(GameSettings.TOWER2_COST);
                        BaseTowerObject baseTower2 = new BaseTowerObject(
                                x_cord, y_cord,
                                (int) (32 * GameSettings.MAP_SCALE),
                                (int) (32 * GameSettings.MAP_SCALE),
                                GameResources.green_square, myGdxGame.world, GameSettings.TOWER2_DAMAGE);
                        towerArray.add(baseTower2);
                        audioManager.towerCreateSound.play(0.6f * MemoryManager.SoundValue());
                        isMenuExecuted = false;
                    }
                    if (isMenuExecuted && button3.isHit(touchPos.x, touchPos.y)
                            && gameSession.getBalance() >= GameSettings.TOWER3_COST) {
                        gameSession.reduceBalance(GameSettings.TOWER3_COST);
                        BaseTowerObject baseTower3 = new BaseTowerObject(
                                x_cord, y_cord,
                                (int) (32 * GameSettings.MAP_SCALE),
                                (int) (32 * GameSettings.MAP_SCALE),
                                GameResources.blue_square, myGdxGame.world, GameSettings.TOWER3_DAMAGE);
                        towerArray.add(baseTower3);
                        audioManager.towerCreateSound.play(0.6f * MemoryManager.SoundValue());
                        isMenuExecuted = false;
                    }
                    if (hasObjectCoordinates("tower", touchPos) && !isMenuExecuted && !isUpgradeMenuExecuted) {
                        if (tileIsEmpty((int) x_cord, (int) y_cord) && (x_cord != -1 && y_cord != -1)) {
                            isMenuExecuted = true;
                        }
                        if (!tileIsEmpty((int) x_cord, (int) y_cord) && (x_cord != -1 && y_cord != -1) && gameSession.getBalance() >= levelCost(touchPos.x, touchPos.y)) {
                            isUpgradeMenuExecuted = true;
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
                case ENDED:
                    if (homeButton2.isHit(touchPos.x, touchPos.y)) {
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

    public void drawMenuUpgrade() {
        unitMenu.draw(myGdxGame.batch);
        upgradeButton.draw(myGdxGame.batch);
        sellButton.draw(myGdxGame.batch);
        closeButton.draw(myGdxGame.batch);
    }

    private void update() {
        Iterator<EnemyObject> enemyObjectIterator = enemyArray.iterator();

        while (enemyObjectIterator.hasNext()) {

            EnemyObject nextEnemy = enemyObjectIterator.next();
            if (!nextEnemy.isAlive()) {
                gameSession.addBalance(50);
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
        for (int i = 0; i < enemyArray.size(); i++) {
            myGdxGame.world.destroyBody(enemyArray.get(i).body);
            enemyArray.remove(i--);
        }

        for (int i = 0; i < towerArray.size(); i++) {
            myGdxGame.world.destroyBody(towerArray.get(i).body);
            towerArray.remove(i--);
        }

        hero = new MainHeroObject(1230, 490, 100, 100,
                GameResources.blue_square, myGdxGame.world);

        gameSession.startGame();
    }

    @Override
    public void dispose() {
        unitMenu.dispose();
        tiledMap.dispose();
        notificationTextView.dispose();
    }
}
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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

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

    boolean needToNotify;
    TiledMap tiledMap;
    Path path;
    Vector2 startPos;
    float startNotify;
    MainHeroObject hero;
    ArrayList<BaseTowerObject> towerArray;
    ArrayList<EnemyObject> enemyArray;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    boolean isMenuExecuted = false;
    boolean isClickerSmall = false;
    boolean isUpgradeMenuExecuted = false;
    boolean needToColorRed = false;
    long redStartTime;
    float x_cord = 0, y_cord = 0;
    int clickerLevel = 1;

    //Play UI
    ButtonView button1, button2, button3, closeButton, pauseButton, sellButton, upgradeButton,
            upgradeClickerButton;
    ImageView unitMenu, tower1, tower2, tower3, liveImageView;
    TextView balanceTextView, balanceRedTextView, livesTextView, levelTextView,
            notificationTextView, towerLevelTextView, towerDamageTextView, balanceBlackTextView,
            noMoneyTextView, clickerLevelTextView;
    ImageView clicker, clickerSmall;

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
                GameResources.MAIN_HERO_IMG_PATH, myGdxGame.world);

        loadMap();
        path = new Path(tiledMap);

        MapObjects objects = tiledMap.getLayers().get("enemy").getObjects();
        if (objects != null) {
            for (RectangleMapObject object : objects.getByType(RectangleMapObject.class)) {
                startPos = new Vector2(object.getRectangle().x, object.getRectangle().y);
                break;
            }
        }

        button1 = new ButtonView(1055, 110, 200, 50, myGdxGame.commonWhiteFont,
                GameResources.BUTTON, "500");
        button2 = new ButtonView(1055, 220, 200, 50, myGdxGame.commonWhiteFont,
                GameResources.BUTTON, "600");
        button3 = new ButtonView(1055, 330, 200, 50, myGdxGame.commonWhiteFont,
                GameResources.BUTTON, "700");
        sellButton = new ButtonView(1055, 300, 210, 50, myGdxGame.commonWhiteFont,
                GameResources.BUTTON, "DISASSEMBLE");
        upgradeButton = new ButtonView(1055, 200, 210, 50, myGdxGame.commonWhiteFont,
                GameResources.BUTTON, "UPGRADE");
        upgradeClickerButton = new ButtonView(1025, 400, 260, 50,
                myGdxGame.commonWhiteFont, GameResources.BUTTON, "UPGRADE CLICK");
        closeButton = new ButtonView(1245, 20, 25, 25, GameResources.close_button);

        tower1 = new ImageView(1080, 60, GameResources.TOWER_IMG_PATH, 54, 100);
        tower2 = new ImageView(1080, 170, GameResources.TOWER_IMG_PATH4, 54, 100);
        tower3 = new ImageView(1080, 280, GameResources.TOWER_IMG_PATH7, 54, 100);

        balanceTextView = new TextView(myGdxGame.commonWhiteFont, 1055, 40);
        balanceRedTextView = new TextView(myGdxGame.commonRedFont, 1055, 40);
        balanceBlackTextView = new TextView(myGdxGame.commonBlackFont, 1055, 40);
        noMoneyTextView = new TextView(myGdxGame.largeRedFont, 380, 640, "YOU HAVE NO ROUGH");
        notificationTextView = new TextView(myGdxGame.largeRedFont, 380, 60);
        towerLevelTextView = new TextView(myGdxGame.commonBlackFont, 1050, 100);
        towerDamageTextView = new TextView(myGdxGame.commonBlackFont, 1050, 140);

        liveImageView = new ImageView(170, 77, GameResources.LIVE_IMG_PATH, -22, 25);
        levelTextView = new TextView(myGdxGame.commonWhiteFont, 170, 40);
        livesTextView = new TextView(myGdxGame.commonWhiteFont, 200, 75);

        unitMenu = new ImageView(1030, 0, GameResources.WHITE, 1000, 1000);
        clicker = new ImageView(1080, 490, GameResources.clicker, 150, 150);
        clickerSmall = new ImageView(1090, 500, GameResources.clicker, 130, 130);
        clickerLevelTextView = new TextView(myGdxGame.commonBlackFont, 1050, 180);

        pauseButton = new ButtonView(
                30, 20,
                92, 92,
                GameResources.PAUSE_IMG_PATH
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
        AudioManager.playgmMusic();
        restartGame();
    }

    public void loadMap() {
        TmxMapLoader mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load("tilemap/mapq (2).tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, GameSettings.MAP_SCALE);
        tiledMapRenderer.setView(MyGdxGame.camera.projection, 0, 0, GameSettings.SCREEN_WIDTH, -GameSettings.SCREEN_HEIGHT);
    }

    private void draw() {
        MyGdxGame.camera.update();
        MyGdxGame.batch.setProjectionMatrix(MyGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        MyGdxGame.batch.begin();
        notificationTextView.setText("ENEMY HAS PASSED!!!");
        balanceTextView.setText("Rough: " + gameSession.getBalance());
        balanceBlackTextView.setText("Rough: " + gameSession.getBalance());
        balanceRedTextView.setText("Rough: " + gameSession.getBalance());
        levelTextView.setText("Wave: " + gameSession.getLevel());
        clickerLevelTextView.setText("Clicker lvl: " + clickerLevel);

        liveImageView.draw(MyGdxGame.batch);
        livesTextView.setText("Lives: " + hero.getLiveLeft());

        tiledMapRenderer.setView(MyGdxGame.camera);


        tiledMapRenderer.setView(MyGdxGame.camera);
        tiledMapRenderer.render();

        levelTextView.draw(MyGdxGame.batch);
        livesTextView.draw(MyGdxGame.batch);
        for (EnemyObject enemy : enemyArray) {

            enemy.draw(MyGdxGame.batch);

            TextView HPLeft = new TextView(myGdxGame.smallRedFont,
                    enemy.getX() * GameSettings.MAP_SCALE,
                    enemy.getY() * GameSettings.MAP_SCALE,
                    enemy.getLiveLeft() + " / " + enemy.getMaxHealth());
            HPLeft.draw(MyGdxGame.batch);
        }
        for (BaseTowerObject tower : towerArray) {
            tower.draw(MyGdxGame.batch);

        }
        hero.draw(MyGdxGame.batch);

        if (isMenuExecuted) {
            drawMenu();
        }
        if (isUpgradeMenuExecuted) {
            drawMenuUpgrade();
            for (BaseTowerObject tower : towerArray) {
                towerDamageTextView.setText("Tower damage: " + tower.getDamage());
                towerLevelTextView.setText("Tower level: " + tower.getLevelNumber());
                towerLevelTextView.draw(MyGdxGame.batch);
                towerDamageTextView.draw(MyGdxGame.batch);
                clickerLevelTextView.draw(MyGdxGame.batch);
            }
        }

        pauseButton.draw(MyGdxGame.batch);
        if (needToColorRed) {
            balanceRedTextView.draw(MyGdxGame.batch);
        } else if (redStartTime - TimeUtils.millis() >= GameSettings.wasd) {
            needToColorRed = false;
            balanceTextView.draw(MyGdxGame.batch);
        } else {
            balanceTextView.draw(MyGdxGame.batch);
        }

        if (MainHeroObject.needToNotify) {
            notificationTextView.draw(MyGdxGame.batch);
        }

        if (gameSession.getBalance() >= 500 && !isMenuExecuted && !isUpgradeMenuExecuted) {
            balanceTextView.draw(MyGdxGame.batch);
        }
        if (gameSession.getBalance() >= 500 && (isMenuExecuted || isUpgradeMenuExecuted)) {
            balanceBlackTextView.draw(MyGdxGame.batch);

        }
        if (gameSession.getBalance() < 500) {
            balanceRedTextView.draw(MyGdxGame.batch);
            noMoneyTextView.draw(MyGdxGame.batch);
        }
        switch (gameSession.state) {
            case PLAYING:
                for (EnemyObject enemy : enemyArray) {
                    enemy.update(2);
                    if (enemy.needToHit()) {
                        hero.hit(enemy.getMaxHealth());
                    }

                }
                for (BaseTowerObject tower : towerArray) {
                    tower.shoot(enemyArray);
                    tower.updateBullets();
                    tower.putInBox();
                }
                hero.notifyCheck();
                break;
            case ENDED:
                fullBlackoutView.draw(MyGdxGame.batch);
                resultTextView.draw(MyGdxGame.batch);
                homeButton2.draw(MyGdxGame.batch);
                break;
            case PAUSED:
                fullBlackoutView.draw(MyGdxGame.batch);
                pauseTextView.draw(MyGdxGame.batch);
                homeButton.draw(MyGdxGame.batch);
                continueButton.draw(MyGdxGame.batch);
                break;

        }
        MyGdxGame.batch.end();
    }

    @Override
    public void render(float delta) {

        if (gameSession.state == GameState.PLAYING) {
            update();

            if (!gameSession.isRest()) {
                System.out.println(gameSession.getLevel());
                if (gameSession.getLevel() % 10 == 0){
                    if (gameSession.shouldSpawnEnemy3()) {
                        EnemyObject enemy3 = new EnemyObject("images/red.png",
                                myGdxGame.world, path, (int) startPos.x, (int) startPos.y,
                                32, 32, GameSettings.ENEMY3_SPEED, GameSettings.ENEMY3_HEALTH);
                        enemyArray.add(enemy3);
                    }
                } else if (gameSession.getLevel() % 2 == 0){
                    if (gameSession.shouldSpawnEnemy2()) {
                        EnemyObject enemy2 = new EnemyObject("images/enemy2.png",
                                myGdxGame.world, path, (int) startPos.x, (int) startPos.y,
                                32, 32, GameSettings.ENEMY2_SPEED, GameSettings.ENEMY2_HEALTH);
                        enemyArray.add(enemy2);
                    }
                } else {
                    if (gameSession.shouldSpawnEnemy()) {
                        EnemyObject enemy = new EnemyObject("images/robot1.png", myGdxGame.world, path,
                                (int) startPos.x, (int) startPos.y, 32, 32,
                                GameSettings.ENEMY_SPEED, GameSettings.ENEMY1_HEALTH);
                        enemyArray.add(enemy);
                    }
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
        if (!towerArray.isEmpty()) {
            for (BaseTowerObject towerObject : towerArray) {
                if (tx >= towerObject.getX() - 16 && tx <= towerObject.getX() + 16 &&
                        ty >= towerObject.getY() - 16 && ty <= towerObject.getY() + 16) {

                    return towerObject.getLevelNumber() * 250;
                }
            }
        }
        if (towerArray.isEmpty()) {
            return 300;
        }
        else {
            return 0;
        }
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = MyGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            switch (gameSession.state) {
                case PLAYING:
                    if (pauseButton.isHit(touchPos.x, touchPos.y)) {
                        gameSession.pauseGame();
                    }

                    for (int i = 0; i < towerArray.size(); i++) {
                        if (isUpgradeMenuExecuted && upgradeButton.isHit(touchPos.x, touchPos.y)
                                && gameSession.getBalance() >= levelCost((int) x_cord, (int) y_cord)) {

                            gameSession.reduceBalance(levelCost((int) x_cord, (int) y_cord));
                            towerArray.get(i).setLevelNumber(towerArray.get(i).getLevelNumber() + 1);
                            towerArray.get(i).damage += 2;
                            isUpgradeMenuExecuted = false;
                        }
                        if (isUpgradeMenuExecuted && upgradeButton.isHit(touchPos.x, touchPos.y)
                                && gameSession.getBalance() <= levelCost((int) x_cord, (int) y_cord)) {
                            needToColorRed = true;
                            redStartTime = TimeUtils.millis();
                        }
                    }
                    for (int i = 0; i < towerArray.size(); i++) {
                        if (isUpgradeMenuExecuted && sellButton.isHit(touchPos.x, touchPos.y)) {

                            gameSession.addBalance((towerArray.get(i).getLevelNumber() * GameSettings.TOWER1_COST) / 2);
                            myGdxGame.world.destroyBody(towerArray.get(i).body);
                            towerArray.remove(i--);
                            isUpgradeMenuExecuted = false;
                        }
                    }
                    if (isUpgradeMenuExecuted && closeButton.isHit(touchPos.x, touchPos.y)  || !unitMenu.isHit(touchPos.x, touchPos.y)) {
                        isUpgradeMenuExecuted = false;
                    }

                    if (isMenuExecuted) {
                        if (button1.isHit(touchPos.x, touchPos.y)
                                && gameSession.getBalance() >= GameSettings.TOWER1_COST) {

                            gameSession.reduceBalance(GameSettings.TOWER1_COST);
                            BaseTowerObject baseTower = new BaseTowerObject(
                                    x_cord, y_cord,
                                    (int) (32 * GameSettings.MAP_SCALE),
                                    (int) (32 * GameSettings.MAP_SCALE),
                                    GameResources.TOWER_IMG_PATH, myGdxGame.world, GameSettings.TOWER1_DAMAGE);

                            towerArray.add(baseTower);
                            audioManager.towerCreateSound.play(0.6f * MemoryManager.SoundValue());
                            isMenuExecuted = false;

                        } else if (button2.isHit(touchPos.x, touchPos.y)
                                && gameSession.getBalance() >= GameSettings.TOWER2_COST) {
                            gameSession.reduceBalance(GameSettings.TOWER2_COST);
                            BaseTowerObject baseTower2 = new BaseTowerObject(
                                    x_cord, y_cord,
                                    (int) (32 * GameSettings.MAP_SCALE),
                                    (int) (32 * GameSettings.MAP_SCALE),
                                    GameResources.TOWER_IMG_PATH4, myGdxGame.world, GameSettings.TOWER2_DAMAGE);

                            towerArray.add(baseTower2);
                            audioManager.towerCreateSound.play(0.6f * MemoryManager.SoundValue());
                            isMenuExecuted = false;

                        } else if (button3.isHit(touchPos.x, touchPos.y)
                                && gameSession.getBalance() >= GameSettings.TOWER3_COST) {
                            gameSession.reduceBalance(GameSettings.TOWER3_COST);
                            BaseTowerObject baseTower3 = new BaseTowerObject(
                                    x_cord, y_cord,
                                    (int) (32 * GameSettings.MAP_SCALE),
                                    (int) (32 * GameSettings.MAP_SCALE),
                                    GameResources.TOWER_IMG_PATH7, myGdxGame.world, GameSettings.TOWER3_DAMAGE);

                            towerArray.add(baseTower3);
                            audioManager.towerCreateSound.play(0.6f * MemoryManager.SoundValue());
                            isMenuExecuted = false;

                        } else if (isMenuExecuted && (button1.isHit(touchPos.x, touchPos.y) && gameSession.getBalance() < GameSettings.TOWER1_COST) ||
                                (button2.isHit(touchPos.x, touchPos.y) && gameSession.getBalance() < GameSettings.TOWER2_COST) ||
                                (button3.isHit(touchPos.x, touchPos.y) && gameSession.getBalance() < GameSettings.TOWER3_COST)) {
                            isMenuExecuted = true;
                        } else if (closeButton.isHit(touchPos.x, touchPos.y) || !unitMenu.isHit(touchPos.x, touchPos.y)) {
                            isMenuExecuted = false;
                        }
                    }

                    if (hasObjectCoordinates("tower", touchPos)) {
                        if (tileIsEmpty((int) x_cord, (int) y_cord) && (x_cord != -1 && y_cord != -1)) {
                            isMenuExecuted = true;
                            isUpgradeMenuExecuted = false;
                        }
                        if (!tileIsEmpty((int) x_cord, (int) y_cord) && (x_cord != -1 && y_cord != -1)) {
                            isMenuExecuted = false;
                            isUpgradeMenuExecuted = true;
                        }
                    }
                    if ((isMenuExecuted || isUpgradeMenuExecuted) && clicker.isHit(touchPos.x, touchPos.y)) {
                        gameSession.addBalance(clickerLevel * 2);
                    }
                    if ((isMenuExecuted || isUpgradeMenuExecuted) &&
                            upgradeClickerButton.isHit(touchPos.x, touchPos.y) &&
                            gameSession.getBalance() > (clickerLevel * 500)) {

                        gameSession.reduceBalance((clickerLevel * 500));
                        clickerLevel++;
                    }
                    break;

                case PAUSED:
                    if (continueButton.isHit(touchPos.x, touchPos.y)) {
                        gameSession.resumeGame();
                    }
                    if (homeButton.isHit(touchPos.x, touchPos.y)) {
                        myGdxGame.setScreen(myGdxGame.menuScreen);
                        ArrayList<Integer> recordsTable = MemoryManager.loadRecordsTable();
                        if (recordsTable == null) {
                            recordsTable = new ArrayList<>();
                        }
                        int foundIdx = 0;
                        for (; foundIdx < recordsTable.size(); foundIdx++) {
                            if (recordsTable.get(foundIdx) < gameSession.getLevel()) break;
                        }
                        recordsTable.add(foundIdx, gameSession.getLevel());
                        MemoryManager.saveTableOfRecords(recordsTable);
                    }
                    break;
                case ENDED:
                    if (homeButton2.isHit(touchPos.x, touchPos.y)) {
                        myGdxGame.setScreen(myGdxGame.menuScreen);
                    }
                    break;
            }
        }

        Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        isClickerSmall = Gdx.input.isTouched() && (isMenuExecuted || isUpgradeMenuExecuted) && clicker.isHit(touchPos.x, touchPos.y);
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

    private boolean hasObjectCoordinates(String tower, Vector3 touchPos) {
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
        unitMenu.draw(MyGdxGame.batch);
        button1.draw(MyGdxGame.batch);
        button2.draw(MyGdxGame.batch);
        button3.draw(MyGdxGame.batch);
        tower1.draw(MyGdxGame.batch);
        tower2.draw(MyGdxGame.batch);
        tower3.draw(MyGdxGame.batch);
        closeButton.draw(MyGdxGame.batch);
        upgradeClickerButton.draw(MyGdxGame.batch);
        if (isClickerSmall) {
            clickerSmall.draw(MyGdxGame.batch);
        } else {
            clicker.draw(MyGdxGame.batch);
        }
    }

    public void drawMenuUpgrade() {
        unitMenu.draw(MyGdxGame.batch);
        upgradeButton.draw(MyGdxGame.batch);
        upgradeClickerButton.draw(MyGdxGame.batch);
        sellButton.draw(MyGdxGame.batch);
        closeButton.draw(MyGdxGame.batch);
        if (isClickerSmall) {
            clickerSmall.draw(MyGdxGame.batch);
        } else {
            clicker.draw(MyGdxGame.batch);
        }
    }

    private void update() {
        Iterator<EnemyObject> enemyObjectIterator = enemyArray.iterator();

        while (enemyObjectIterator.hasNext()) {

            EnemyObject nextEnemy = enemyObjectIterator.next();
            if (!nextEnemy.isAlive()) {
                gameSession.addBalance(50);
                myGdxGame.world.destroyBody(nextEnemy.body);
                enemyObjectIterator.remove();
                //System.out.println("DELETED");
            }
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

        hero = new MainHeroObject(1230, 490, -100, -100,
                GameResources.MAIN_HERO_IMG_PATH, myGdxGame.world);

        gameSession.startGame();
    }

    @Override
    public void dispose() {
        unitMenu.dispose();
        tiledMap.dispose();
        notificationTextView.dispose();
        myGdxGame.dispose();
        clicker.dispose();
        clickerSmall.dispose();
    }
}
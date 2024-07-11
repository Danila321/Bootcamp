package com.mygdx.game.screens;

import static com.mygdx.game.Managers.AudioManager.backgroundGameMusic;
import static com.mygdx.game.Managers.AudioManager.backgroundMusic;
import static com.mygdx.game.screens.SettingsScreen.getMusic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Managers.MemoryManager;
import com.mygdx.game.Managers.AudioManager;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.ui.ButtonView;
import com.mygdx.game.ui.ImageView;
import com.mygdx.game.ui.RecordsListView;
import com.mygdx.game.ui.TextView;
import com.mygdx.game.utility.GameResources;
import com.mygdx.game.utility.GameSettings;

public class MenuScreen extends ScreenAdapter {

    MyGdxGame myGdxGame;

    TextView gameName;
    ImageView background;
    ButtonView startGameButton;
    ButtonView settingsButton;
    ButtonView exitButton;

    TextView recordsTextView;
    RecordsListView recordsListView;

    public MenuScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;

        gameName = new TextView(myGdxGame.largeWhiteFont, 100, 167, "Scrap Defense");
        background = new ImageView(0, 720, GameResources.BACKGROUND, -GameSettings.SCREEN_HEIGHT, GameSettings.SCREEN_WIDTH);

        startGameButton = new ButtonView(250, 300, 200, 68,
                myGdxGame.commonBlackFont, GameResources.WHITE_BUTTON, "START");

        settingsButton = new ButtonView(250, 400, 200, 68,
                myGdxGame.commonBlackFont, GameResources.WHITE_BUTTON, "SETTINGS");

        exitButton = new ButtonView(250, 500, 200, 68,
                myGdxGame.commonBlackFont, GameResources.WHITE_BUTTON, "EXIT");

        recordsTextView = new TextView(myGdxGame.largeWhiteFont, 650, 300, "Last records");
        recordsListView = new RecordsListView(myGdxGame.commonWhiteFont, 650, 350);
    }

    @Override
    public void show() {
        AudioManager.playbgMusic();
        backgroundGameMusic.stop();
    }

    @Override
    public void render (float delta) {
        handleInput();
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        myGdxGame.batch.begin();
        background.draw(myGdxGame.batch);
        gameName.draw(myGdxGame.batch);
        startGameButton.draw(myGdxGame.batch);
        settingsButton.draw(myGdxGame.batch);
        exitButton.draw(myGdxGame.batch);

        try {
            recordsListView.setRecords(MemoryManager.loadRecordsTable());
        } catch (Exception e){

        }
        if (MemoryManager.loadRecordsTable() != null){

            recordsListView.draw(myGdxGame.batch);
        }

        recordsTextView.draw(myGdxGame.batch);
        myGdxGame.batch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = MyGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (startGameButton.isHit(touchPos.x, touchPos.y)) {
                myGdxGame.setScreen(myGdxGame.gameScreen);
            }
            if (settingsButton.isHit(touchPos.x, touchPos.y)) {
                myGdxGame.setScreen(myGdxGame.settingsScreen);
            }

            if (exitButton.isHit(touchPos.x, touchPos.y)) {
                Gdx.app.exit();
            }
        }
    }


    @Override
    public void dispose () {
        gameName.dispose();
        myGdxGame.dispose();
    }
}

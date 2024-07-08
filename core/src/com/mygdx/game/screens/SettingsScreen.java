package com.mygdx.game.screens;



import static com.mygdx.game.Managers.MemoryManager.MusicValue;
import static com.mygdx.game.Managers.MemoryManager.SoundValue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Managers.AudioManager;
import com.mygdx.game.Managers.MemoryManager;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.ui.ButtonView;
import com.mygdx.game.ui.ImageView;
import com.mygdx.game.utility.GameResources;

public class SettingsScreen extends ScreenAdapter {
    MyGdxGame myGdxGame;
    ImageView blackoutMid;
    ImageView backgroundView;
    ButtonView returnButton;
    static Slider slider, slider2;
    Stage stage;


    public SettingsScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;

        backgroundView = new ImageView(0, 720, GameResources.BACKGROUND, -720, 1280);
        returnButton = new ButtonView(260, 250, 390, 70,
                myGdxGame.commonBlackFont, GameResources.WHITE_BUTTON, "return");
        blackoutMid = new ImageView(220, 200, GameResources.BUTTON, 400, 460);
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        slider = new Slider(0, 10, 1, false, skin);
        slider2 = new Slider(0, 10, 1, false, skin);
        stage = new Stage();
        Gdx.input.setInputProcessor (stage);
    }

    @Override
    public void render(float delta) {

        handleInput();

        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);



        myGdxGame.batch.begin();

        backgroundView.draw(myGdxGame.batch);
        blackoutMid.draw(myGdxGame.batch);
        returnButton.draw(myGdxGame.batch);


        myGdxGame.batch.end();


        stage.act();
        stage.addActor(slider);
        slider.setWidth(400);
        slider.setX(250);
        slider.setY(335);
        slider.setValue(SoundValue());

        stage.addActor(slider2);
        slider2.setWidth(400);
        slider2.setX(250);
        slider2.setY(300);
        slider.setValue(MusicValue());
        stage.draw();

    }
    public static int getSound(){
        return (int) slider.getValue();
    }
    public static int getMusic(){
        return (int) slider2.getValue();
    }


    void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());

            if (returnButton.isHit(touchPos.x, touchPos.y)) {
                myGdxGame.setScreen(myGdxGame.menuScreen);
            }
            if (slider.isDragging()) {
                AudioManager.updateSoundFlag(getSound());
                MemoryManager.saveSoundSettings(getSound());
            }
            if (slider.isDragging()) {
                MemoryManager.saveMusicSettings(getMusic());
            }
        }
    }

}
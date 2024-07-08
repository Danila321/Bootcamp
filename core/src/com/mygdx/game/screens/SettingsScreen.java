package com.mygdx.game.screens;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Managers.AudioManager;
import com.mygdx.game.Managers.MemoryManager;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.ui.ButtonView;
import com.mygdx.game.ui.ImageView;
import com.mygdx.game.ui.TextView;
import com.mygdx.game.utility.GameResources;
import com.mygdx.game.utility.GameSettings;

public class SettingsScreen extends ScreenAdapter {
    MyGdxGame myGdxGame;
    ImageView blackoutMid;
    ImageView backgroundView;
    ButtonView returnButton;
    static Slider slider, slider2;
    Stage stage;
    TextView soundText, musicText;

    public SettingsScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;

        backgroundView = new ImageView(0, GameSettings.SCREEN_HEIGHT, GameResources.BACKGROUND, -GameSettings.SCREEN_HEIGHT, GameSettings.SCREEN_WIDTH);
        returnButton = new ButtonView(450, 400, 380, 50,
                myGdxGame.commonBlackFont, GameResources.WHITE_BUTTON, "Return");
        blackoutMid = new ImageView(410, 200, GameResources.BUTTON, 300, 460);
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        soundText = new TextView(myGdxGame.commonWhiteFont, 600, 250, "Sounds");
        musicText = new TextView(myGdxGame.commonWhiteFont, 615, 320, "Music");
        slider = new Slider(0, 100, 1, false, skin);

        Container<Slider> container=new Container<Slider>(slider);
        container.setTransform(true);   // for enabling scaling and rotation
        container.size(250, 60);
        container.setOrigin(container.getWidth() / 2, container.getHeight() / 2);
        container.setPosition(640, 440);
        container.setScale(1.5f);  //scale according to your requirement

        slider2 = new Slider(0, 100, 1, false, skin);

        Container<Slider> container2 = new Container<Slider>(slider2);
        container2.setTransform(true);   // for enabling scaling and rotation
        container2.size(250, 60);
        container2.setOrigin(container.getWidth() / 2, container.getHeight() / 2);
        container2.setPosition( 640,370);
        container2.setScale(1.5f);  //scale according to your requirement

        stage = new Stage();
        Gdx.input.setInputProcessor (stage);





        stage.addActor(container);


        stage.addActor(container2);
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

        soundText.draw(myGdxGame.batch);
        musicText.draw(myGdxGame.batch);

        myGdxGame.batch.end();

        slider.setValue(MemoryManager.SoundValue());
        slider2.setValue(MemoryManager.MusicValue());

        stage.act();
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
        }
        if (slider.isDragging()) {
            AudioManager.updateSoundFlag(getSound());
            MemoryManager.saveSoundSettings(getSound());
        }
        if (slider2.isDragging()) {
            AudioManager.updateMusicFlag(getMusic());
            MemoryManager.saveMusicSettings(getMusic());
            AudioManager.backgroundMusic.setVolume(0.1f * getMusic());
        }

    }

}
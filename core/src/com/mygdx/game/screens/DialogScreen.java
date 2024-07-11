package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Managers.MemoryManager;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.utility.GameResources;
import com.mygdx.game.utility.GameSettings;

public class DialogScreen implements Screen {
    private MyGdxGame game;
    private SpriteBatch batch;
    private BitmapFont font;
    private Texture background;

    // Диалоговые реплики
    private String[] dialogLines = {
            "Onboard Computer: Awakening complete. Welcome. You are in the research\n\tfacility on the space station. You have been asleep for 57 years.",
            "Main Character: 57 years? What happened?",
            "Onboard Computer: The complex has been attacked by aggressive robots.\n\tYou need to organize the defense and find out what happened.",
            "Main Character: Why don't I remember anything?",
            "Onboard Computer: Your memories were partially erased for your safety.\n\tUse the metal dropped by robots to improve the defense and build a time machine.",
            "Main Character: What's the plan?",
            "Onboard Computer: First, defend against waves of robots.\n\tYour ultimate goal is to build a time machine and prevent the catastrophe.\n\tThe first wave will start in 10 seconds. Good luck."
    };

    private int currentLine = 0;
    private float charDelay = 0.05f; // Задержка между символами
    private float charTimer = 0;
    private String currentDisplayText = "";

    private boolean fullTextDisplayed = false;

    public DialogScreen(MyGdxGame game) {
        this.game = game;
        this.batch = game.batch;
        this.font = game.commonWhiteFont;
        this.background = new Texture(GameResources.BACKGROUND); // Путь к твоему фоновому изображению
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (fullTextDisplayed) {
                    if (currentLine < dialogLines.length - 1) {
                        currentLine++;
                        currentDisplayText = "";
                        fullTextDisplayed = false;
                    } else {
                        // Переход к следующей сцене (например, первая волна врагов)
                        MemoryManager.saveDialogue(true);
                        game.setScreen(game.menuScreen);
                    }
                } else {
                    currentDisplayText = dialogLines[currentLine];
                    fullTextDisplayed = true;
                }
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.camera.update();
        batch.setProjectionMatrix(game.camera.combined);

        batch.begin();
        batch.draw(background, 0, 0, -GameSettings.SCREEN_WIDTH, -GameSettings.SCREEN_HEIGHT);

        charTimer += delta;
        if (charTimer >= charDelay && !fullTextDisplayed) {
            charTimer = 0;
            if (currentDisplayText.length() < dialogLines[currentLine].length()) {
                currentDisplayText += dialogLines[currentLine].charAt(currentDisplayText.length());
            } else {
                fullTextDisplayed = true;
            }
        }
        font.draw(batch, currentDisplayText, 50, GameSettings.SCREEN_HEIGHT - 150);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        background.dispose();
    }
}

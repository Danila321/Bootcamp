package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.utility.GameSettings;

public class MainHeroObject extends GameObject {
    public static int livesLeft;
    int maxHealth;
    public static int heroDamage;
    public static boolean needToNotify;
    long notifyStartTime;

    public MainHeroObject(int x, int y, int width, int height, String texturePath, World world) {
        super(texturePath, x, y, width, height, GameSettings.MAIN_HERO_BIT, 1000000, world);
        maxHealth = 100;
        livesLeft = 100;
        heroDamage = 25;
        needToNotify = false;
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }

    @Override
    public void hit(int damage) {
        livesLeft -= damage;
        needToNotify = true;
        notifyStartTime = TimeUtils.millis();
    }

    public void notifyCheck() {
        if (TimeUtils.millis() - notifyStartTime >= GameSettings.wasd) {
            notifyStartTime = TimeUtils.millis();
            needToNotify = false;
        }
    }
    public boolean isAlive() {
        return livesLeft > 0;
    }
    public int getLiveLeft() {
        return livesLeft;
    }
    public void setLivesLeft(int lives) {
        livesLeft = lives;
    }
}

package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import utility.GameSettings;

public class MainHeroObject extends objects.GameObject {
    static int livesLeft;
    int maxHealth;

    public MainHeroObject(int x, int y, int width, int height, String texturePath, World world) {
        super(texturePath, x, y, width, height, GameSettings.MAIN_HERO_BIT, world);
        maxHealth = 10;
        livesLeft = 10;
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }

    @Override
    public void hit() {
        livesLeft -= 1;
    }
    public boolean isAlive() {
        return livesLeft > 0;
    }
    public int getLiveLeft() {
        return livesLeft;
    }
}

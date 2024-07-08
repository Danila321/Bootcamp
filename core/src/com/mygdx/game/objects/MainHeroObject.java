package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.utility.GameSettings;

public class MainHeroObject extends GameObject {
    static int livesLeft;
    int maxHealth;
    public static int heroDamage;

    public MainHeroObject(int x, int y, int width, int height, String texturePath, World world) {
        super(texturePath, x, y, width, height, GameSettings.MAIN_HERO_BIT, world);
        maxHealth = 100;
        livesLeft = 100;
        heroDamage = 25;
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }

    @Override
    public void hit(int damage) {
        livesLeft -= damage;
        System.out.println("Ouch!");
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
    public int getHeroDamage() {
        return heroDamage;
    }
}

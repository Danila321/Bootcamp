package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.utility.Path;
import com.mygdx.game.utility.GameSettings;


public class EnemyObject extends GameObject {
    private int currentIndex;
    private float speed;
    private Path path;
    private int livesLeft;
    public static int maxHealth;
    public boolean needToHitPLayer;

    public EnemyObject(String texturePath, World world, Path path, int x, int y, int width, int height, float speed) {
        super(texturePath, x, y, width, height, GameSettings.ENEMY_BIT, world);
        currentIndex = 0;
        this.speed = speed;
        this.path = path;
        maxHealth = 5;
        livesLeft = 5;
        needToHitPLayer = false;
    }

    public void update(float deltaTime) {
        Vector2 position = new Vector2(getX(), getY());
        if (currentIndex < path.getLength()) {
            Vector2 target = path.getPoint(currentIndex);
            Vector2 direction = target.cpy().sub(position).nor();

            position.add(direction.scl(speed));
            setX((int) position.x);
            setY((int) position.y);

            if (position.dst(target) < speed) {
                currentIndex++;
            }
        } else {
            hit(MainHeroObject.heroDamage);
            needToHitPLayer = true;
        }
    }


    public void draw(SpriteBatch batch) {
        batch.draw(getTexture(), getX() * GameSettings.MAP_SCALE,
                getY() * GameSettings.MAP_SCALE, 64, 64);
    }

    public boolean isAlive() {
        return livesLeft > 0;
    }
    public int getLiveLeft() {
        return livesLeft;
    }

    @Override
    public void hit(int damage) {
        livesLeft -= damage;
    }

    public boolean needToHit() {
        return needToHitPLayer;
    }
}

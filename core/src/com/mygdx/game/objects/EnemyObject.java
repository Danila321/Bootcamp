package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.utility.Path;
import com.mygdx.game.utility.GameSettings;


public class EnemyObject extends GameObject {
    private int currentIndex;
    private float speed;
    private Path path;
    private int livesLeft;
    public int maxHealth;
    public boolean needToHitPLayer;
    Vector2 positionT;
    public ShapeRenderer shapeRenderer, shapeRenderer2;

    public EnemyObject(String texturePath, World world, Path path, int x, int y, int width,
                       int height, float speed, int health) {
        super(texturePath, x, y, width, height, GameSettings.ENEMY_BIT, 1000000000, world);
        currentIndex = 0;
        this.speed = speed;
        this.path = path;
        maxHealth = health;
        livesLeft = health;
        needToHitPLayer = false;

    }

    public void update(float deltaTime) {
        Vector2 position = new Vector2(getX(), getY());
        positionT = position;
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

    public void setSpeed(int speed){
        this.speed = speed;
    }

    public void draw(SpriteBatch batch) {
        float barWidth = 50;
        float barHeight = 10;
        float barX = positionT.x - barWidth / 2;
        float barY = positionT.y + 20;

        batch.draw(getTexture(), getX() * GameSettings.MAP_SCALE,
                getY() * GameSettings.MAP_SCALE, 32 * GameSettings.MAP_SCALE,
                32 * GameSettings.MAP_SCALE);


    }

    public boolean isAlive() {
        return livesLeft > 0;
    }

    public int getLiveLeft() {
        return livesLeft;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    @Override
    public void hit(int damage) {
        livesLeft -= damage;
    }

    public boolean needToHit() {
        return needToHitPLayer;
    }
}

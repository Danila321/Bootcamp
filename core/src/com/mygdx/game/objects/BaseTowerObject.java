package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.utility.GameResources;
import com.mygdx.game.utility.GameSettings;

import java.util.ArrayList;

public class BaseTowerObject extends objects.GameObject {

    public int attackCoolDown;
    public int attackRadius;
    long lastShotTime;
    private World world;
    Vector2 direction;

    public BaseTowerObject(float x, float y, int width, int height, String texturePath, World world) {
        super(texturePath, x, y, width, height, GameSettings.BASE_TOWER_BIT, world);
        this.width = width;
        this.height = height;
        attackCoolDown = GameSettings.BASE_TOWER_ATTACK_COOL_DOWN;
        attackRadius = GameSettings.BASE_TOWER_ATTACK_RADIUS;
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }

    private boolean needToShoot() {
        if (TimeUtils.millis() - lastShotTime >= attackCoolDown) {
            lastShotTime = TimeUtils.millis();
            return true;
        }
        return false;
    }

    public void shoot(ArrayList<EnemyObject> enemyArray) {
        if (needToShoot()) {
            int minVal = 1000000000;
            EnemyObject target = null;
            for (EnemyObject enemy : enemyArray) {
                Vector2 posEnemy = new Vector2(enemy.getX(), enemy.getY());
                Vector2 pos = new Vector2(getX(), getY());
                float distancePos = pos.dst(posEnemy);
                if (distancePos < GameSettings.BASE_TOWER_ATTACK_RADIUS && distancePos < minVal) {
                    minVal = (int)distancePos;
                    target = enemy;
                    direction = posEnemy.cpy().sub(pos).nor();
                }
            }
            if (target != null) {
                BulletObject bullet = new BulletObject(getX(), getY(), 50, 50,
                        direction, GameResources.red_square, world);
            }
        }
    }
}

package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.Managers.AudioManager;
import com.mygdx.game.Managers.MemoryManager;
import com.mygdx.game.screens.SettingsScreen;
import com.mygdx.game.utility.GameResources;
import com.mygdx.game.utility.GameSettings;

import java.util.ArrayList;
import java.util.Iterator;

public class BaseTowerObject extends GameObject {
    AudioManager audioManager;
    public int attackCoolDown;
    public int attackRadius;
    long lastShotTime;
    private final World world;
    Vector2 direction;
    public ArrayList<BulletObject> bulletArray;
    private final int tempX, tempY;

    public BaseTowerObject(float x, float y, int width, int height, String texturePath, World world) {
        super(texturePath, x, y, width, height, GameSettings.BASE_TOWER_BIT, world);
        this.width = width;
        this.height = height;
        this.world = world;
        attackCoolDown = GameSettings.BASE_TOWER_ATTACK_COOL_DOWN;
        attackRadius = GameSettings.BASE_TOWER_ATTACK_RADIUS;
        bulletArray = new ArrayList<>();
        tempX = getX();
        tempY = getY();
        audioManager = new AudioManager();
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        for (BulletObject bullet : bulletArray) {
            bullet.draw(batch);
        }
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
                float distancePos = pos.cpy().nor().dst(posEnemy);
                if (distancePos < GameSettings.BASE_TOWER_ATTACK_RADIUS && distancePos < minVal) {
                    minVal = (int)distancePos;
                    target = enemy;
                    direction = posEnemy.sub(pos).cpy().nor();
                }
            }
            if (target != null) {
                audioManager.shootSound.play(0.05f * MemoryManager.SoundValue());
                BulletObject bullet = new BulletObject(getX() - 35, getY() - 35, -15, -15,
                        direction.scl(GameSettings.BULLET_VELOCITY),
                        GameResources.red_square, world);
                bulletArray.add(bullet);
            }
        }
    }

    public void updateBullets() {
        Iterator<BulletObject> bulletObjectIterator = bulletArray.iterator();

        while (bulletObjectIterator.hasNext()) {

            BulletObject nextBullet = bulletObjectIterator.next();
            if (nextBullet.hasToBeDestroyed()) {
                world.destroyBody(nextBullet.body);
                bulletObjectIterator.remove();
            }
        }

    }

    public void putInBox() {
        if (getX() != tempX && getY() != tempY) {
            setX(tempX);
            setY(tempY);
        } else if (getX() != tempX) {
            setX(tempX);
            setY(tempY);
        } else if (getY() != tempY) {
            setY(tempY);
            setX(tempX);
        }
    }
}

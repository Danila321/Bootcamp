package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.utility.GameSettings;

public class BaseTowerObject extends objects.GameObject {

    public int attackCoolDown;
    public int attackRadius;
    long lastShotTime;
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

    public boolean needToShoot() {
        if (TimeUtils.millis() - lastShotTime >= attackCoolDown) {
            lastShotTime = TimeUtils.millis();
            return true;
        }
        return false;
    }
}

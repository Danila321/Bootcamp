package com.mygdx.game.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.utility.GameSettings;

public class BulletObject extends GameObject {

    public boolean wasHit;
    BulletObject(float x, float y, int width, int height, Vector2 vector2, String texturePath, World world) {
        super(texturePath, x, y, width, height, GameSettings.BASE_BULLET_BIT, 1,  world);
        body.setLinearVelocity(vector2);
        body.setBullet(true);
        wasHit = false;
    }


    public boolean hasToBeDestroyed() {
        return wasHit || getX() < GameSettings.BASE_TOWER_ATTACK_RADIUS;
    }

    public void hit(int variable) {
        wasHit = true;
    }
}

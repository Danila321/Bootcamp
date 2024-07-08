package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.utility.Path;
import com.mygdx.game.utility.GameSettings;


public class EnemyObject extends GameObject {
    private int currentIndex;
    private float speed;
    private Path path;
    private int livesLeft;


    public EnemyObject(String texturePath, World world, Path path, int x, int y, float speed) {
        super(texturePath, x, y, 50, 50, GameSettings.ENEMY_BIT, world);
        currentIndex = 0;
        this.speed = speed;
        this.path = path;
        livesLeft = 1;
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
            //System.out.println("end end end");
        }
    }

    public void setSpeed(int speed){
        this.speed = speed;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(getTexture(), getX() * GameSettings.MAP_SCALE,
                getY() * GameSettings.MAP_SCALE, 32, 32);
    }

    public boolean isAlive() {
        return livesLeft > 0;
    }
    public int getLiveLeft() {
        return livesLeft;
    }

    @Override
    public void hit() {
        livesLeft--;
    }

}

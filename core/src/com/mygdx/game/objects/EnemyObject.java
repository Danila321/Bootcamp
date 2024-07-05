package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.utility.Path;
import com.mygdx.game.utility.GameSettings;


public class EnemyObject extends GameObject {
    private int currentIndex;
    private int speed;
    private Path path;

    public EnemyObject(String texturePath, World world, Path path, int x, int y) {
        super(texturePath, x, y, 50, 50, GameSettings.ENEMY_BIT, world);
        currentIndex = 0;
        speed = 2;
        this.path = path;
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


    public void draw(SpriteBatch batch) {
        batch.draw(getTexture(), getX() * GameSettings.MAP_SCALE,
                getY() * GameSettings.MAP_SCALE, 32, 32);
    }
}

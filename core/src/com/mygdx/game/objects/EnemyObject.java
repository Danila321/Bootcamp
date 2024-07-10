package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.utility.Path;
import com.mygdx.game.utility.GameSettings;

import space.earlygrey.shapedrawer.ShapeDrawer;


public class EnemyObject extends GameObject {
    private int currentIndex;
    private float speed;
    ShapeDrawer drawer;

    private Path path;
    public  int livesLeft;
    public  int maxHealth;
    public boolean needToHitPLayer;
    Vector2 positionT;
    public ShapeRenderer shapeRenderer, shapeRenderer2;
    Texture texture;
    public EnemyObject(String texturePath, World world, Path path, int x, int y, int width,
                       int height, float speed, int health) {
        super(texturePath, x, y, width, height, GameSettings.ENEMY_BIT, 1000000000, world);
        currentIndex = 0;
        this.speed = speed;
        this.path = path;
        maxHealth = health;
        livesLeft = health;
        needToHitPLayer = false;
        shapeRenderer = new ShapeRenderer();
        shapeRenderer2 = new ShapeRenderer();
        texture = new Texture("images/background.png");
        TextureRegion region = new TextureRegion(texture);
        drawer = new ShapeDrawer(MyGdxGame.batch, region);
        drawer.setDefaultLineWidth(5);
        drawer.setColor(Color.RED);

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
        float barHeight = 20;
        float barX = getX() - barWidth / 2;
        float barY = getY() + 20;
        float healthPercentage = (float) livesLeft / maxHealth;

        drawer.setColor(Color.WHITE);
        drawer.line(getX() * GameSettings.MAP_SCALE, getY() * GameSettings.MAP_SCALE - 15, getX() * GameSettings.MAP_SCALE + barWidth, getY() * GameSettings.MAP_SCALE - 15);
        drawer.setColor(Color.valueOf("#03fc07 "));
        drawer.filledRectangle(getX() * GameSettings.MAP_SCALE, getY() * GameSettings.MAP_SCALE - 15, barWidth * healthPercentage, barHeight);



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

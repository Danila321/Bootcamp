package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ImageView extends View {

    Texture texture;

    public ImageView(float x, float y, String imagePath) {
        super(x, y);
        texture = new Texture(imagePath);
        this.width = texture.getWidth() ;
        this.height = texture.getHeight() ;
    }
    public ImageView(float x, float y, String imagePath, float height, float widght) {
        super(x, y);
        texture = new Texture(imagePath);
        this.width = widght ;
        this.height = height ;
    }

    @Override public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
    }

    @Override public void dispose() { texture.dispose(); }
}
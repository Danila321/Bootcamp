package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

public class View implements Disposable {

    float x;
    float y;

    float width;

    float height;

    public View(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public View(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(SpriteBatch batch) { }

    @Override
    public void dispose() {
    }
}

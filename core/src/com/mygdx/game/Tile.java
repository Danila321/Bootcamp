package com.mygdx.game;

public class Tile {
    private int x, y;
    private int width, height;
    private boolean player;
    private boolean enemy;
    private boolean tower;
    public String simbol = "/";



    public Tile(int width, int height, int x, int y, boolean player, boolean enemy, boolean tower) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;

        this.player = player;
        this.enemy = enemy;
        this.tower = tower;
    }

    public int getX() {
        return x;
    }

    public int getY(){
        return y;
    }

}

package com.mygdx.game.utility;

public class GameSettings {
    public static final float SCALE = 0.05f;

    // Device settings
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;

    // Physics settings

    public static final float STEP_TIME = 1f / 60f;
    public static final int VELOCITY_ITERATIONS = 6;
    public static final int POSITION_ITERATIONS = 6;
    public static final short ENEMY_BIT = 2;
    public static final short MAIN_HERO_BIT = 4;
    public static final short BASE_TOWER_BIT = 8;
    public static final short BASE_BULLET_BIT = 16;
    public static final int BASE_BULLET_DAMAGE = 5;
    public static final int BASE_TOWER_ATTACK_COOL_DOWN = 500;
    public static final float BASE_TOWER_ATTACK_RADIUS = 200;
    public static final int BULLET_VELOCITY = 100;
    public static final float MAP_SCALE = 2.5f;

    public static int ENEMY_SPAWN_TIME = 2500;
    public static int ENEMY_SPAWN_TIME2 = 3000;
    public static int ENEMY_SPAWN_TIME3 = 11000;
    public static int ENEMY_COUNT = 1;
    public static int ENEMY2_COUNT = 1;
    public static int ENEMY3_COUNT = 1;
    public static float ENEMY_SPEED = 1.0f;
    public static float ENEMY2_SPEED = 2.0f;
    public static float ENEMY3_SPEED = 1.0f;
    public static long wasd = 1500;

    public static long WAVE_REST_TIME = 8000;

    public static final int TOWER1_COST = 500;
    public static final int TOWER2_COST = 600;
    public static final int TOWER3_COST = 700;

    public static final int TOWER1_DAMAGE = 2;
    public static final int TOWER2_DAMAGE = 5;
    public static final int TOWER3_DAMAGE = 10;
}

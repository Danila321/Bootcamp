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
    public static final short BULLET_BIT = 16;
    public static final int BASE_TOWER_ATTACK_COOL_DOWN = 500;
    public static final int BASE_TOWER_ATTACK_RADIUS = 750;
    public static final int BULLET_VELOCITY = 25;
    public static final float MAP_SCALE = 2.5f;

    public static int ENEMY_SPAWN_TIME = 2000;
    public static int ENEMY_COUNT = 1;
    public static float ENEMY_SPEED = 1.0f;

    public static long WAVE_REST_TIME = 8000;

    public static final int TOWER1_COST = 500;
    public static final int TOWER2_COST = 600;
    public static final int TOWER3_COST = 700;
    //public static final int ENEMY_FORCE_RATIO = 5;
}

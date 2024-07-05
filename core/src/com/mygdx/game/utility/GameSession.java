package com.mygdx.game.utility;

import com.badlogic.gdx.utils.TimeUtils;

public class GameSession {
    long nextEnemySpawnTime;

    public GameSession() {

    }

    public boolean shouldSpawnEnemy() {
        if (nextEnemySpawnTime <= TimeUtils.millis()) {
            nextEnemySpawnTime = TimeUtils.millis() + 1000;
            return true;
        }
        return false;
    }
}

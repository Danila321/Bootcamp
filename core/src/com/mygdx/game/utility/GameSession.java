package com.mygdx.game.utility;

import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.ui.Money;

public class GameSession {
    long nextEnemySpawnTime;
    int eliminatedEnemiesNumber = 0;

    public GameSession() {

    }

    public boolean shouldSpawnEnemy() {
        if (nextEnemySpawnTime <= TimeUtils.millis()) {
            nextEnemySpawnTime = TimeUtils.millis() + 1000;
            return true;
        }
        return false;
    }

    public void eliminationRegistration(Money balance) {
        balance.addBalance(50);
    }
}

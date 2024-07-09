package com.mygdx.game.utility;

import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.ui.Money;

public class GameSession {
    public GameState state;

    long nextEnemySpawnTime;
    int countReleasedEnemies = 0;

    boolean levelFlag = true;
    private int level = 0;
    long sessionStartTime;
    long pauseStartTime;
    long startRestTime = 0;

    int eliminatedEnemiesNumber = 0;

    public GameSession() {

    }

    public void startGame() {
        state = GameState.PLAYING;
        sessionStartTime = TimeUtils.millis();
        level = 0;
    }

    public void pauseGame() {
        state = GameState.PAUSED;
        pauseStartTime = TimeUtils.millis();
    }

    public void resumeGame() {
        state = GameState.PLAYING;
        sessionStartTime += TimeUtils.millis() - pauseStartTime;
    }

    public void endGame() {
        state = GameState.ENDED;

    }

    public boolean shouldSpawnEnemy() {
        if (countReleasedEnemies < GameSettings.ENEMY_COUNT) {
            if (nextEnemySpawnTime <= TimeUtils.millis()) {
                System.out.println(GameSettings.ENEMY_COUNT);
                System.out.println(countReleasedEnemies);
                nextEnemySpawnTime = TimeUtils.millis() + GameSettings.ENEMY_SPAWN_TIME;
                countReleasedEnemies++;
                return true;
            }
        } else {
            startRestTime = TimeUtils.millis();
        }
        return false;
    }

    public boolean isRest() {
        if (startRestTime + GameSettings.WAVE_REST_TIME > TimeUtils.millis()) {
            levelFlag = true;
            return true;
        } else {
            if (levelFlag) {
                countReleasedEnemies = 0;
                levelUp();
                levelFlag = false;
            }
            return false;
        }
    }

    public void eliminationRegistration(Money balance) {
        balance.addBalance(50);

    }

    public void levelUp() {
        level++;
        if (GameSettings.ENEMY_SPAWN_TIME >= 700) {
            GameSettings.ENEMY_SPAWN_TIME -= 200;
        }
        GameSettings.ENEMY_SPEED += 0.3f;
        GameSettings.ENEMY_COUNT++;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
